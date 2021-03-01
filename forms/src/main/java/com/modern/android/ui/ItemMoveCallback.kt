package com.modern.android.forms.ui

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.modern.android.forms.ui.page.RearrangeItemViewHolder

class ItemMoveCallback constructor(private val adapter: ItemTouchHelperContract) : ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled() = true

    override fun isItemViewSwipeEnabled() = false

    override fun onSwiped(viewHolder: ViewHolder, direction: Int) {}

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlags, 0)
    }

    override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
        adapter.onRowMoved(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE
            && viewHolder is RearrangeItemViewHolder
        ) {
            adapter.onRowSelected(viewHolder)
        }
        super.onSelectedChanged(viewHolder, actionState)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (viewHolder is RearrangeItemViewHolder) {
            adapter.onRowClear(viewHolder)
        }
    }
}

interface ItemTouchHelperContract {
    fun onRowMoved(fromPosition: Int, toPosition: Int)
    fun onRowSelected(myViewHolder: RearrangeItemViewHolder)
    fun onRowClear(myViewHolder: RearrangeItemViewHolder)
}