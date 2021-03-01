package com.modern.android.forms.entity

sealed class FormItem {

    data class PageItem(val page: Page) : FormItem()

    data class SummaryItem(val form: Form) : FormItem()

}