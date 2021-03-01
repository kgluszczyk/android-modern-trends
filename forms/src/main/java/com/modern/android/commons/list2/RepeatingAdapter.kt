package com.modern.commons.android.list2

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class RepeatingDiffAdapter2<T : ListItem2>(private val maxItemsCount: Int = MAX_ITEMS) : DiffAdapter2<T>() {

    override fun createDiffCallback(oldItems: List<T>, newItems: List<T>): DiffUtil.Callback =
        RepeatingDiffCallback(oldItems = oldItems, newItems = newItems)

    override fun getItemCount(): Int = items.repeatingItemCount

    override fun getItem(position: Int): T =
        items.getItem(position)

    protected open val List<T>.repeatingItemCount: Int
        get() = if (size == 0) 0 else maxItemsCount

    private fun List<T>.getItem(position: Int): T =
        get(resolveRepeatingPosition(position))

    private fun List<T>.resolveRepeatingPosition(position: Int): Int =
        if (isNotEmpty()) position % size else RecyclerView.NO_POSITION

    private inner class RepeatingDiffCallback(private val oldItems: List<T>, private val newItems: List<T>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.repeatingItemCount

        override fun getNewListSize(): Int = newItems.repeatingItemCount

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsTheSame(oldItems.getItem(oldItemPosition), newItems.getItem(newItemPosition))

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areContentsTheSame(oldItems.getItem(oldItemPosition), newItems.getItem(newItemPosition))

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
            getChangePayload(oldItems.getItem(oldItemPosition), newItems.getItem(newItemPosition))

    }

    companion object {

        const val MAX_ITEMS = 200
    }

}
