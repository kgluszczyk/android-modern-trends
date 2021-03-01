package com.modern.android.formssample.common;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.AnimRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Simple builder for handling fragment transactions.
 * It reuses every fragment from FragmentManager by attaching the ones that already exist
 * and detaching the ones that are no longer being used.
 * <p>
 * This default behavior can be overridden by setting
 * {@link #removeCurrentFromContainer()}} and {{@link #replaceOldInFragmentManager()}}
 *
 * @param <T> fragment class that will be attached to given container
 */
public class TransactionBuilder<T extends Fragment> {

    private Bundle args;
    private @AnimRes
    int currentFragmentAnimation;
    private final String fragmentName;
    private final String fragmentTag;
    private boolean removeCurrentFromContainer;
    private boolean replaceOldInFragmentManager;
    private @AnimRes
    int targetFragmentAnimation;

    public TransactionBuilder(Class<T> clazz) {
        fragmentTag = clazz.getName();
        fragmentName = clazz.getName();
    }

    public TransactionBuilder(String fragmentClassName) {
        fragmentTag = fragmentClassName;
        fragmentName = fragmentClassName;
    }

    /**
     * Commits fragment into the given fragment placeholder
     *
     * @param activity            app compat activity
     * @param fragmentPlaceholder fragment placeholder res id
     * @return new fragment in container
     */
    public T commit(AppCompatActivity activity, @IdRes int fragmentPlaceholder) {
        return commit(activity, activity.getSupportFragmentManager(), fragmentPlaceholder);
    }

    /**
     * Commits fragment into the container
     * using given fragment manager
     *
     * @param context             context
     * @param manager             fragment manager
     * @param fragmentPlaceholder fragment placeholder res id
     * @return new fragment in container
     */
    public T commit(Context context, FragmentManager manager, @IdRes int fragmentPlaceholder) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(targetFragmentAnimation, currentFragmentAnimation);
        //handle fragment that is currently in the container

        T fragment = handleCurrentFragment(context, manager, transaction, fragmentPlaceholder);
        if (fragment == null) {
            //in case current fragment isn't the one we want to add handle this process
            fragment = handleNewFragment(context, manager, transaction, fragmentPlaceholder);
        }
        transaction.commitAllowingStateLoss();
        return fragment;
    }

    public T commit(Fragment fragment, @IdRes int fragmentPlaceholder) {
        return commit(fragment.getContext(), fragment.getChildFragmentManager(), fragmentPlaceholder);
    }

    /**
     * Mark transaction to remove current fragment that is attached to container
     *
     * @return current TransactionBuilder instance
     */
    public TransactionBuilder<T> removeCurrentFromContainer() {
        this.removeCurrentFromContainer = true;
        return this;
    }

    /**
     * Mark transaction to force create new instance of fragment
     * if its already attached to fragment manager
     *
     * @return current TransactionBuilder instance
     */
    public TransactionBuilder<T> replaceOldInFragmentManager() {
        this.replaceOldInFragmentManager = true;
        return this;
    }

    /**
     * Binds animations to fragments in transaction
     *
     * @param currentFragmentAnimation - animation for fragment currently attached to container
     * @param targetFragmentAnimation  - animation for fragment, which will be added to container
     * @return current TransactionBuilder instance
     */
    public TransactionBuilder<T> withAnimation(@AnimRes int currentFragmentAnimation, @AnimRes int targetFragmentAnimation) {
        this.currentFragmentAnimation = currentFragmentAnimation;
        this.targetFragmentAnimation = targetFragmentAnimation;
        return this;
    }

    /**
     * Add arguments to the fragment
     *
     * @param args arguments Bundle
     * @return current TransactionBuilder instance
     */
    public TransactionBuilder<T> withArgs(Bundle args) {
        this.args = args;
        return this;
    }

    /**
     * Applies arguments to fragment if it's possible:
     * - fragment not active
     * - not after state saved
     *
     * @param fragment fragment to apply arguments
     */
    private void applyArguments(Fragment fragment) {
        if (!fragment.isAdded() && !fragment.isStateSaved()) {
            fragment.setArguments(args);
        }
    }

    private Fragment createNewInstance(Context context) {
        return Fragment.instantiate(context, fragmentName, args);
    }

    /**
     * Handles current fragment in container:
     * - dispatches to {@link #handleSameFragmentInTheContainer(Context, FragmentTransaction, int, Fragment)}
     * in case current fragment is the one we want to add
     * - detaches or removes old fragment otherwise
     *
     * @param context             context
     * @param manager             fragment manager
     * @param transaction         current fragment transaction
     * @param fragmentPlaceholder res id of fragments placeholder
     * @return null if current fragment in container isn't the one we want to create
     */
    @SuppressWarnings("unchecked")
    @Nullable
    private T handleCurrentFragment(Context context, FragmentManager manager,
                                    final FragmentTransaction transaction, @IdRes int fragmentPlaceholder) {
        Fragment fragmentToChange = manager.findFragmentById(fragmentPlaceholder);
        //there is no fragment in container right now
        if (fragmentToChange == null) {
            return null;
        }

        //current fragment in container is the one we want to add
        if (fragmentTag.equals(fragmentToChange.getTag())) {
            return handleSameFragmentInTheContainer(context, transaction, fragmentPlaceholder, fragmentToChange);
        }

        //remove or detach current fragment
        if (removeCurrentFromContainer) {
            transaction.remove(fragmentToChange);
        } else {
            transaction.detach(fragmentToChange);
        }
        return null;
    }

    /**
     * Handles process of adding new fragment.
     * In case the fragment is in the fragment manager attaches it.
     * In case fragment isn't in the fragment manager creates it.
     *
     * @param context             context
     * @param manager             fragment manager
     * @param transaction         fragment transaction
     * @param fragmentPlaceholder id of fragments placeholder
     * @return fragment added to the container
     */
    @SuppressWarnings("unchecked")
    @NonNull
    private T handleNewFragment(Context context, FragmentManager manager,
                                FragmentTransaction transaction, @IdRes int fragmentPlaceholder) {
        Fragment currentInstance = manager.findFragmentByTag(fragmentTag);
        //if the current fragment is removing we want to treat is an already dead instance
        if (currentInstance != null && currentInstance.isRemoving()) {
            currentInstance = null;
        }

        //fragment already exists and we want to remove it
        if (currentInstance != null && replaceOldInFragmentManager) {
            transaction.remove(currentInstance);
        }

        //add new instance to fragment manager
        if (currentInstance == null || replaceOldInFragmentManager) {
            currentInstance = createNewInstance(context);
            transaction.add(fragmentPlaceholder, currentInstance, fragmentTag);
        } else {
            //attach old fragment with new arguments
            applyArguments(currentInstance);
            transaction.attach(currentInstance);
        }
        return (T) currentInstance;
    }

    /**
     * Handles case when current fragment in container is the one we want to add.
     * Creates new instances in case we want to replace the one in fragment manager.
     *
     * @param context             application context
     * @param transaction         current fragment transaction
     * @param fragmentPlaceholder res id of fragments placeholder
     * @param currentFragment     current fragment in container
     * @return instance of fragment that will be attached
     */
    @SuppressWarnings("unchecked")
    @NonNull
    private T handleSameFragmentInTheContainer(Context context, FragmentTransaction transaction,
                                               @IdRes int fragmentPlaceholder, @NonNull Fragment currentFragment) {
        //in case we want to force create new instance - replace old fragment
        if (replaceOldInFragmentManager) {
            Fragment newInstance = createNewInstance(context);
            transaction.replace(fragmentPlaceholder, newInstance, fragmentTag);
            return (T) newInstance;
        } else {
            applyArguments(currentFragment);
            //check if it's detached so we can attach it again
            //this can happen in a case when you attach fragment and then detach it without adding new fragment
            //findFragmentById will then give you fragment even though it is detached
            if (currentFragment.isDetached()) {
                transaction.attach(currentFragment);
            }
            return (T) currentFragment;
        }
    }

}
