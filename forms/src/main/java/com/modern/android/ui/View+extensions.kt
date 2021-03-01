package com.modern.android.forms.ui

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible

private const val DEFAULT_NOTIFICATION_DISMISS_TIME = 3000L

fun View.showAsNotification(dismissTime: Long = DEFAULT_NOTIFICATION_DISMISS_TIME) {
    if (isInvisible) {
        translationY = measuredHeight.toFloat()
        isVisible = true
    }
    animate()
        .setStartDelay(0)
        .translationY(0f)
        .withEndAction {
            animate()
                .setStartDelay(dismissTime)
                .translationY(measuredHeight.toFloat())
        }
}

fun View.hideNotification() {
    animate()
        .setStartDelay(0)
        .translationY(measuredHeight.toFloat())
}