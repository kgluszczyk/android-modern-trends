package com.modern.android.forms.ui

import com.modern.android.forms.entity.Header
import com.modern.android.forms.entity.Header2
import com.modern.android.forms.entity.Header3
import com.modern.android.forms.entity.Input
import com.modern.android.forms.entity.InputCheckbox
import com.modern.android.forms.entity.InputDropdown
import com.modern.android.forms.entity.InputNumeric
import com.modern.android.forms.entity.InputText
import com.modern.android.forms.entity.Item
import com.modern.android.forms.entity.ItemAnswer
import com.modern.android.forms.entity.RearrangeList
import com.modern.android.forms.entity.RearrangeListItem
import com.modern.android.forms.entity.Row
import com.modern.android.forms.entity.Text
import com.modern.android.forms.entity.Toggle
import com.modern.android.forms.entity.ValidationError
import com.modern.android.forms.fromApiBoolean
import com.modern.android.forms.ui.page.ItemDropdown
import com.modern.android.forms.ui.page.ItemErrorPlaceholder
import com.modern.android.forms.ui.page.ItemForm
import com.modern.android.forms.ui.page.ItemHeader
import com.modern.android.forms.ui.page.ItemHeader2
import com.modern.android.forms.ui.page.ItemHeader3
import com.modern.android.forms.ui.page.ItemInput
import com.modern.android.forms.ui.page.ItemInputCheckbox
import com.modern.android.forms.ui.page.ItemInputToggle
import com.modern.android.forms.ui.page.ItemRearrangeList
import com.modern.android.forms.ui.page.ItemRearrangeListItem
import com.modern.android.forms.ui.page.ItemRow
import com.modern.android.forms.ui.page.ItemText

fun Item.toFormItem(position: Int, errors: List<ValidationError>): ItemForm? {
    return when (this) {
        is Header -> ItemHeader(
            position = position,
            title = title
        )
        is Header2 -> ItemHeader2(
            position = position,
            title = title
        )
        is Header3 -> ItemHeader3(
            position = position,
            title = title
        )
        is InputCheckbox -> ItemInputCheckbox(
            position = position,
            inputId = id,
            label = title ?: "",
            subtitle = subtitle ?: "",
            checked = value?.fromApiBoolean() ?: false,
            errorMessage = errors.findErrorMessage(id)
        )
        is InputText -> ItemInput(
            position = position,
            inputId = id,
            label = title ?: "",
            hint = hint ?: "",
            inputType = ItemInput.InputType.TEXT,
            value = value ?: "",
            errorMessage = errors.findErrorMessage(id)
        )
        is InputNumeric -> ItemInput(
            position = position,
            inputId = id,
            label = title ?: "",
            hint = hint ?: "",
            inputType = ItemInput.InputType.NUMBER,
            value = value ?: "",
            errorMessage = errors.findErrorMessage(id)
        )
        is InputDropdown -> ItemDropdown(
            position = position,
            inputId = id,
            title = title ?: "",
            value = value ?: "",
            items = items.mapIndexedNotNull { index, item -> item.toFormItem(index, errors) }
                .filterIsInstance<ItemText>()
                .map { it.title },
            errorMessage = errors.findErrorMessage(id)
        )
        is Text -> ItemText(
            position = position,
            title = title ?: "",
            value = value ?: ""
        )
        is Toggle -> ItemInputToggle(
            position = position,
            inputId = id,
            title = title,
            checked = value?.fromApiBoolean() ?: false,
            subTitle = subtitle ?: "",
            errorMessage = errors.findErrorMessage(id)
        )
        is RearrangeList -> ItemRearrangeList(
            position = position,
            title = title,
            items = items.mapIndexedNotNull { index, item -> item.toFormItem(index, errors) }
                .filterIsInstance<ItemRearrangeListItem>()
        )
        is RearrangeListItem -> ItemRearrangeListItem(
            position = position,
            inputId = id,
            title = title,
            value = value
        )
        is Row -> ItemRow(
            position = position,
            items = items.toRowItems(errors)
                .ifEmpty { return null }
        )
    }
}

fun List<Item>.toRowItems(errors: List<ValidationError>): List<ItemForm> {
    val errorIds = errors.map { it.id }
    val fieldErrors = map { (it as? Input)?.id in errorIds }
    val largestError = filterIsInstance<Input>()
        .mapNotNull { input -> errors.find { input.id == it.id }?.message }
        .maxBy { it.length }

    return mapIndexedNotNull { index, item -> item.toFormItem(index, errors) }
        .mapIndexed { index, itemForm ->
            if (largestError.isNullOrBlank() || fieldErrors[index]) {
                itemForm
            } else {
                ItemErrorPlaceholder(largestError, itemForm)
            }
        }

}

fun Item.updateValues(answersMap: Map<Long, ItemAnswer>): Item {
    return when (this) {
        is InputCheckbox -> copy(value = answersMap[id]?.answer ?: value)
        is InputText -> copy(value = answersMap[id]?.answer ?: value)
        is InputNumeric -> copy(value = answersMap[id]?.answer ?: value)
        is Toggle -> copy(value = answersMap[id]?.answer ?: value)
        is RearrangeListItem -> copy(value = answersMap[id]?.answer ?: value)
        is InputDropdown -> copy(value = answersMap[id]?.answer ?: value)
        is RearrangeList -> copy(items = items.map { it.updateValues(answersMap) }
            .filterIsInstance<RearrangeListItem>())
        is Row -> copy(items = items.map { it.updateValues(answersMap) })
        is Text -> copy(value = calculation?.calculate(answersMap) ?: value)
        else -> this
    }
}