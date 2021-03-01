package com.modern.android.forms.ui.summary

import com.modern.android.commons.list.ListItem
import com.modern.android.forms.R

abstract class SummaryItem(val layout: Int) : ListItem {
    override fun getId(): Long = hashCode().toLong()

    override fun getViewHolderType(): Int = layout
}

data class PageItem(val page: Int, val title: String, val hasError: Boolean) : SummaryItem(
    layout = R.layout.form_item_page
)

data class HeaderItem(val header: String) : SummaryItem(
    layout = R.layout.form_item_section
)

data class ButtonItem(val enabled: Boolean) : SummaryItem(layout = R.layout.form_item_send_button)