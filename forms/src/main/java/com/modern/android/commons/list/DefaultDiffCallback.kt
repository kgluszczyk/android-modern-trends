package com.modern.commons.android.list

import androidx.recyclerview.widget.DiffUtil
import com.modern.android.commons.list.ListItem

class DefaultDiffCallback<T : ListItem> : DiffUtil.ItemCallback<T>() {

    override fun areItemsTheSame(oldItem: T, newItem: T) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem
}