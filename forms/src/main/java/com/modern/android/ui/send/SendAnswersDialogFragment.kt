package com.modern.android.forms.ui.send

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.modern.android.forms.R
import com.modern.android.forms.databinding.FormSendFragmentBinding
import com.modern.android.forms.di.SendAnswersViewModelFactory
import dagger.android.support.DaggerAppCompatDialogFragment
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import javax.inject.Inject

private const val DEFAULT_ANIMATION_TIME = 200L

class SendAnswersDialogFragment : DaggerAppCompatDialogFragment() {

    private val defaultTranslateSize by lazy {
        requireContext().resources.getDimensionPixelSize(R.dimen.padding_small_plus).toFloat()
    }
    private var animatedChildren = emptyList<View>()
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var viewModelFactory: SendAnswersViewModelFactory
    private val viewModel: SendAnswersViewModel by lazy {
        ViewModelProvider(requireActivity(), viewModelFactory).get(SendAnswersViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.Form_FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FormSendFragmentBinding.inflate(inflater).apply {
            bind()
            setLoadingState()
            animatedChildren = body.children.toList()
            sendAnswers()
        }.root
        //todo async each answer infinit
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearAnimations()
        animatedChildren = emptyList()
        compositeDisposable.clear()
    }

    private fun FormSendFragmentBinding.sendAnswers() {
        viewModel.sendAnswers(arguments!!.getParcelable(ANSWERS_ARG)!!)
            .doOnSubscribe { setLoadingState() }
            .subscribe({ setSuccessState() }, {
                Timber.e(it, "Error while sending form answers")
                setErrorState()
            })
            .addTo(compositeDisposable)
    }

    private fun FormSendFragmentBinding.bind() {
        circle.scaleX = 0f
        circle.scaleY = 0f
        back.setOnClickListener { dismiss() }
    }

    private fun FormSendFragmentBinding.setLoadingState() {
        clearAnimations()
        ViewPropertyAnimatorSet {
            startViewAnimator(
                visible = false,
                view = button
            )
        }
            .then {
                startViewAnimator(
                    visible = false,
                    animateTranslate = false,
                    views = listOf(title, description)
                )
                startViewAnimator(view = processing)
            }
            .then {
                circle.animate()
                    .alpha(0f)
                    .scaleX(0f)
                    .scaleY(0f)
                    .setDuration(DEFAULT_ANIMATION_TIME * 2)
            }
            .start()
    }

    private fun FormSendFragmentBinding.setErrorState() {
        title.setText(R.string.form_send_error_title)
        description.setText(R.string.form_send_error)
        button.setText(R.string.form_retry)
        button.setOnClickListener { sendAnswers() }
        circle.setBackgroundResource(R.drawable.form_red_circle)
        circle.setImageResource(R.drawable.form_error)
        startRevealAnimation()
    }

    private fun FormSendFragmentBinding.setSuccessState() {
        title.setText(R.string.form_send_success_title)
        description.setText(R.string.form_send_success)
        button.setText(R.string.form_ok_close)
        button.setOnClickListener { activity?.finish() }
        circle.setBackgroundResource(R.drawable.form_blue_circle)
        circle.setImageResource(R.drawable.form_success)
        startRevealAnimation()
    }

    private fun FormSendFragmentBinding.startRevealAnimation() {
        clearAnimations()
        ViewPropertyAnimatorSet {
            circle.alpha = 1f
            circle.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(DEFAULT_ANIMATION_TIME * 2)
                .setInterpolator(android.view.animation.OvershootInterpolator())
        }
            .then {
                startViewAnimator(visible = false, view = processing)
                startViewAnimator(views = listOf(title, description))
            }
            .then { startViewAnimator(view = button) }
            .start()
    }

    private fun clearAnimations() {
        animatedChildren.forEach { it.animate().cancel() }
    }

    private fun startViewAnimator(
        visible: Boolean = true,
        animateTranslate: Boolean = true,
        views: List<View>
    ): ViewPropertyAnimator = views.map { startViewAnimator(visible, animateTranslate, it) }.first()

    private fun startViewAnimator(
        visible: Boolean = true,
        animateTranslate: Boolean = true,
        view: View
    ): ViewPropertyAnimator {
        val translationSize = if (visible) defaultTranslateSize else defaultTranslateSize / 2f
        view.translationY = if (visible && animateTranslate) translationSize else 0f
        return view.animate()
            .alpha(if (visible) 1f else 0f)
            .translationY(if (visible || animateTranslate) 0f else translationSize)
            .setDuration(if (visible) DEFAULT_ANIMATION_TIME else DEFAULT_ANIMATION_TIME / 2)
            .setInterpolator(AccelerateDecelerateInterpolator())
    }

    private class ViewPropertyAnimatorSet(init: AnimationBuilder) {

        private val animations = mutableListOf(init)

        fun then(animation: AnimationBuilder): ViewPropertyAnimatorSet {
            animations.add(animation)
            return this
        }

        fun start() {
            if (animations.isEmpty()) return
            animations.removeAt(0)()
                .withEndAction { start() }
        }

    }

    companion object {

        private const val ANSWERS_ARG = "answers"

        fun create(answers: com.modern.android.forms.entity.FormResults) = SendAnswersDialogFragment().apply {
            arguments = bundleOf(ANSWERS_ARG to answers)
        }
    }
}

typealias AnimationBuilder = () -> ViewPropertyAnimator
