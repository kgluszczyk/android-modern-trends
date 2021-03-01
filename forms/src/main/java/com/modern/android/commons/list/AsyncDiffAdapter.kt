package com.modern.commons.android.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.modern.android.commons.list.ListItem
import com.modern.android.commons.list.ViewHolder
import com.modern.android.commons.list.ViewHolderFactory

abstract class AsyncDiffAdapter<T : ListItem>(diffCallback: DiffUtil.ItemCallback<T> = DefaultDiffCallback()) :
    ListAdapter<T, ViewHolder<T>>(diffCallback) {

    lateinit var factory: ViewHolderFactory<ListItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder<T> {
        return this.factory.create(LayoutInflater.from(parent.context), parent, viewType) as ViewHolder<T>
    }

    override fun onBindViewHolder(holder: ViewHolder<T>, p1: Int) {
        holder.bind(getItem(p1))
    }

    override fun getItemId(position: Int): Long = (getItem(position) as ListItem).id

    override fun getItemViewType(position: Int): Int = (this.getItem(position) as ListItem).viewHolderType
}