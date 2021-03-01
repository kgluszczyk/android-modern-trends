package com.modern.android.forms.ui

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ViewHolder.ifValidPosition(lambda: (Int) -> Unit) {
    adapterPosition
        .takeIf { it != RecyclerView.NO_POSITION }
        ?.let {
            lambda(it)
        }
}