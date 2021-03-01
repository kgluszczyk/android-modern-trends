package com.modern.commons.android.extensions.view

import androidx.annotation.IdRes
import androidx.constraintlayout.motion.widget.MotionLayout

fun MotionLayout.doOnComplete(block: () -> Unit) {
    setTransitionListener(object : TransitionListener {
        override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
            setTransitionListener(null)
            block()
        }
    })
}

fun MotionLayout.animateIntoTransition(@IdRes transitionId: Int, onFinish: () -> Unit = {}) {
    val transition = getTransition(transitionId)
    val isExpanded = currentState == startState

    doOnComplete {
        setTransition(transitionId)
        progress = if (isExpanded) 0f else 1f
        onFinish()
    }
    transitionToState(if (isExpanded) transition.startConstraintSetId else transition.endConstraintSetId)
}

interface TransitionListener : MotionLayout.TransitionListener {
    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
    }

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
    }

    override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
    }

    override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
    }

}