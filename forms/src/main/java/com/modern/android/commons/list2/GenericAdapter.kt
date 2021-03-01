package com.modern.commons.android.list2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import com.modern.commons.android.extensions.view.runSafely
import io.reactivex.subjects.PublishSubject
import java.util.Collections.emptyList
import kotlin.properties.Delegates.observable

interface ListItem2 {

    val itemType: Int

    val itemId: Int
}

abstract class ViewHolder2<T : ListItem2>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    protected val context: Context
        get() = itemView.context

    abstract fun bind(item: T)

    @Deprecated("Use bindChange(old: T, new: T) instead")
    open fun bindChange(payloads: List<Any>) {
        val diffPayloads = payloads.filterIsInstance<DiffPayload<T>>()
            .takeIf { it.isNotEmpty() } ?: return
        // if update happens to fast then list can have multiple payloads, in this case the oldest update will be
        // in first element in list and the newest in last, so the oldest item will be first().oldItem and the newest
        // will be last().newItem
        bindChange(diffPayloads.first().oldItem, diffPayloads.last().newItem)
    }

    open fun bindChange(oldItem: T, newItem: T) {
        bind(newItem)
    }

    open fun onAttachedToWindow() {}

    open fun onDetachedFromWindow() {}

    /**
     * Register a callback [block] to be invoked when this view is clicked.
     * Makes use of [runSafely] extension to avoid posting notifications when the view is being removed.
     */
    protected inline fun View.setSafeOnClickListener(crossinline block: () -> Unit) {
        setOnClickListener {
            runSafely {
                block()
            }
        }
    }

    /**
     * Registers a callback that posts [getAdapterPosition] to [subject] when the view is clicked.
     * This is done in a safe manner to avoid posting index when the view is being removed.
     */
    protected fun View.setSafePositionClickListener(subject: PublishSubject<Int>) {
        setSafeOnClickListener {
            subject.onNext(adapterPosition)
        }
    }
}

abstract class BindingViewHolder2<T : ListItem2, K : ViewDataBinding>(protected val binding: K) :
    ViewHolder2<T>(binding.root)

typealias ViewHolderFactory2<T> = (inflater: LayoutInflater, parent: ViewGroup, viewType: Int) -> ViewHolder2<out T>

abstract class Adapter2<T : ListItem2> : RecyclerView.Adapter<ViewHolder2<T>>() {

    abstract val factory: ViewHolderFactory2<T>

    open var items: List<T> by observable(emptyList<T>()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long = getItem(position).itemId.toLong()

    override fun getItemViewType(position: Int): Int = getItem(position).itemType

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder2<T> =
        factory(LayoutInflater.from(parent.context), parent, viewType) as ViewHolder2<T>

    override fun onBindViewHolder(holder: ViewHolder2<T>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onViewAttachedToWindow(holder: ViewHolder2<T>) {
        holder.onAttachedToWindow()
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder2<T>) {
        holder.onDetachedFromWindow()
    }

    open fun getItem(position: Int): T = items[position]
}

abstract class DiffAdapter2<T : ListItem2> : Adapter2<T>() {

    private var _items: List<T> = emptyList()

    override var items: List<T>
        get() = _items
        set(newItems) {
            val diffResult = DiffUtil.calculateDiff(createDiffCallback(_items, newItems))
            diffResult.dispatchUpdatesTo(createUpdateCallback(_items, newItems))
            _items = newItems
        }

    fun setItemsNoDiff(items: List<T>) {
        _items = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder2<T>, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.bindChange(payloads)
        }
    }

    protected open fun createDiffCallback(oldItems: List<T>, newItems: List<T>): DiffUtil.Callback =
        DiffCallback(oldItems = oldItems, newItems = newItems)

    protected open fun createUpdateCallback(oldItems: List<T>, newItems: List<T>): ListUpdateCallback =
        AdapterListUpdateCallback(this)

    open fun areItemsTheSame(item1: T, item2: T): Boolean = item1.itemId == item2.itemId

    open fun areContentsTheSame(item1: T, item2: T): Boolean = item1 == item2

    @Deprecated(message = "Use hasChangePayload(old: T, new: T) instead")
    open fun getChangePayload(old: T, new: T): Any? =
        if (hasChangePayload(old, new)) DiffPayload(old, new) else null

    open fun hasChangePayload(old: T, new: T): Boolean = false

    private inner class DiffCallback(private val oldItems: List<T>, private val newItems: List<T>) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areItemsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            areContentsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? =
            getChangePayload(oldItems[oldItemPosition], newItems[newItemPosition])
    }
}

data class DiffPayload<T>(val oldItem: T, val newItem: T)
