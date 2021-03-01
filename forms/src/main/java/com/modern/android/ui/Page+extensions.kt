package com.modern.android.forms.ui

import com.modern.android.forms.entity.Input
import com.modern.android.forms.entity.Page
import com.modern.android.forms.entity.ValidationError
import com.modern.android.forms.entity.flatten
import com.modern.android.forms.ui.page.ItemSection

fun Page.toItems(errors: List<ValidationError>) = listOfNotNull(
    ItemSection(
        -1,
        title
    )
) + items.mapIndexedNotNull { index, item -> item.toFormItem(index, errors) }

fun Page.hasError(errorItemsIds: List<Long>) = items.flatten()
    .filterIsInstance<Input>()
    .any { it.id in errorItemsIds }




