package com.modern.android.forms.ui.summary

import com.modern.android.commons.list.ViewHolderFactory
import com.modern.android.forms.R
import com.modern.android.forms.databinding.FormItemPageBinding
import com.modern.android.forms.databinding.FormItemSectionBinding
import com.modern.android.forms.databinding.FormItemSendButtonBinding
import com.modern.commons.android.list.AsyncDiffAdapter

class SummaryAdapter(private val listener: SummaryListener) : AsyncDiffAdapter<SummaryItem>() {

    init {
        factory = ViewHolderFactory { layoutInflater, viewGroup, viewType ->
            when (viewType) {
                R.layout.form_item_section -> HeaderViewHolder(
                    FormItemSectionBinding.inflate(layoutInflater, viewGroup, false)
                )
                R.layout.form_item_page -> PageViewHolder(
                    FormItemPageBinding.inflate(layoutInflater, viewGroup, false),
                    listener
                )
                R.layout.form_item_send_button -> ButtonViewHolder(
                    FormItemSendButtonBinding.inflate(layoutInflater, viewGroup, false),
                    listener
                )
                else -> throw IllegalStateException("Unknown view type $viewType")
            }
        }
    }

    interface SummaryListener {
        fun onPageClicked(page: Int)
        fun onSendClicked()
    }
}