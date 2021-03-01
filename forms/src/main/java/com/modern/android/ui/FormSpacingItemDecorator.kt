package com.modern.android.forms.ui

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.modern.android.forms.R

class FormSpacingItemDecorator(context: Context) : RecyclerView.ItemDecoration() {

    private val noPaddingTypes = listOf(
        R.layout.form_item_rearrange_list,
        R.layout.form_item_page
    )
    private val defaultMargin = context.resources.getDimensionPixelSize(R.dimen.margin_normal_plus)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val leftRightPadding = defaultMargin.takeIf { parent.shouldDrawPaddingForChild(view) } ?: 0
        outRect.set(
            leftRightPadding,
            0,
            leftRightPadding,
            0
        )
    }

    private fun RecyclerView.shouldDrawPaddingForChild(view: View): Boolean =
        adapter?.getItemViewType(layoutManager?.getPosition(view) ?: 0) !in noPaddingTypes

}