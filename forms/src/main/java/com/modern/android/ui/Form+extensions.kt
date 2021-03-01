package com.modern.android.forms.ui

import androidx.annotation.WorkerThread
import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.FormItem
import com.modern.android.forms.entity.ItemAnswer
import com.modern.android.forms.ui.summary.ButtonItem
import com.modern.android.forms.ui.summary.HeaderItem
import com.modern.android.forms.ui.summary.PageItem

fun Form.toItems(hasInternetConnection: Boolean, errorItemsIds: List<Long>) = listOf(HeaderItem(title)) +
        pages.mapIndexed { index, page -> PageItem(index, page.title, page.hasError(errorItemsIds)) } +
        ButtonItem(hasInternetConnection)

fun Form.toItems() = listOf(FormItem.SummaryItem(this)) + pages.map { FormItem.PageItem(it) }

@WorkerThread
fun Form.applyAnswers(answersMap: Map<Long, ItemAnswer>): Form {
    return copy(pages = pages.map { page ->
        page.copy(items = page.items.map { item -> item.updateValues(answersMap) })
    })
}