package com.modern.android.commons.list;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;

public abstract class BindingViewHolder<T extends ListItem, K extends ViewDataBinding> extends ViewHolder<T> {
    protected K binding;

    public BindingViewHolder(K binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @Override
    public final void bind(@NonNull T item) {
        if (updateBinding(item)) {
            binding.executePendingBindings();
        }
    }

    /**
     * Method that is used to update data in binding
     *
     * @param item
     * @return true if data in binding has been updated, false otherwise
     */
    protected boolean updateBinding(T item) {
        return false;
    }
}