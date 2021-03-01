package com.modern.android.forms.ui.summary

import androidx.core.content.ContextCompat
import com.modern.android.commons.list.BindingViewHolder
import com.modern.android.forms.R
import com.modern.android.forms.databinding.FormItemPageBinding
import com.modern.android.forms.databinding.FormItemSectionBinding
import com.modern.android.forms.databinding.FormItemSendButtonBinding

import com.modern.android.forms.ui.ifValidPosition

class HeaderViewHolder(binding: FormItemSectionBinding) :
    BindingViewHolder<HeaderItem, FormItemSectionBinding>(binding) {

    override fun updateBinding(item: HeaderItem): Boolean {
        binding.header.text = item.header
        return false
    }
}

class PageViewHolder(
    binding: FormItemPageBinding,
    private val listener: SummaryAdapter.SummaryListener
) : BindingViewHolder<PageItem, FormItemPageBinding>(binding) {

    override fun updateBinding(item: PageItem): Boolean {
        binding.name.text = item.title
        binding.name.setTextColor(
            if (item.hasError) ContextCompat.getColor(context, R.color.form_carnation)
            else ContextCompat.getColor(context, R.color.form_text_color_link)
        )
        binding.root.setOnClickListener {
            ifValidPosition {
                listener.onPageClicked(item.page)
            }
        }
        return false
    }
}

class ButtonViewHolder(
    binding: FormItemSendButtonBinding,
    private val listener: SummaryAdapter.SummaryListener
) : BindingViewHolder<ButtonItem, FormItemSendButtonBinding>(binding) {

    init {
        binding.button.setOnClickListener {
            ifValidPosition {
                listener.onSendClicked()
            }
        }
    }

    override fun updateBinding(item: ButtonItem): Boolean {
        binding.button.isEnabled = item.enabled
        return false
    }
}