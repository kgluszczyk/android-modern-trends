package com.modern.commons.android.extensions.view

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

/**
 * Performs given action only if the adapter position of view holder is different than [RecyclerView.NO_POSITION].
 * This is a safety precaution necessary when the item is being removed.
 */
inline fun ViewHolder.runSafely(crossinline block: () -> Unit) {
    if (adapterPosition != RecyclerView.NO_POSITION) {
        block()
    }
}