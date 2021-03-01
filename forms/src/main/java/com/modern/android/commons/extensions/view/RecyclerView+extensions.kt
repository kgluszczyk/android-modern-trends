package com.modern.commons.android.extensions.view

import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.smoothScrollToPosition(
    position: Int,
    alignVertical: Int = LinearSmoothScroller.SNAP_TO_ANY,
    alignHorizontal: Int = LinearSmoothScroller.SNAP_TO_ANY
) {
    object : LinearSmoothScroller(this.context) {
        override fun getVerticalSnapPreference() = alignVertical
        override fun getHorizontalSnapPreference() = alignHorizontal
    }.let { scroller ->
        scroller.targetPosition = position
        this.layoutManager?.startSmoothScroll(scroller)
    }
}