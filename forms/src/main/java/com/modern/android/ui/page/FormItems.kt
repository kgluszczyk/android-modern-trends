package com.modern.android.forms.ui.page

import com.modern.android.forms.R
import com.modern.commons.android.list2.ListItem2

abstract class ItemForm(override val itemType: Int) : ListItem2 {

    abstract val position: Int

    override val itemId: Int
        get() = position

}

data class ItemInputCheckbox(
    override val position: Int,
    val inputId: Long,
    val label: String,
    val subtitle: String,
    val checked: Boolean,
    val errorMessage: String?
) : ItemForm(R.layout.form_item_checkbox)

data class ItemInput(
    override val position: Int,
    val inputId: Long,
    val label: String,
    val value: String,
    val hint: String,
    val inputType: InputType,
    val errorMessage: String?
) : ItemForm(R.layout.form_item_input) {

    enum class InputType { TEXT, NUMBER }
}

data class ItemDropdown(
    override val position: Int,
    val inputId: Long,
    val title: String,
    val value: String,
    val items: List<String>,
    val errorMessage: String?
) : ItemForm(R.layout.form_item_dropdown)

data class ItemText(
    override val position: Int,
    val title: String,
    val value: String
) : ItemForm(R.layout.form_item_value)

data class ItemInputToggle(
    override val position: Int,
    val inputId: Long,
    val title: String,
    val checked: Boolean,
    val validator: Regex? = null,
    val subTitle: String = "",
    val errorMessage: String?
) : ItemForm(R.layout.form_item_toggle)

data class ItemSection(
    override val position: Int,
    val title: String
) : ItemForm(R.layout.form_item_section)

data class ItemRearrangeList(
    override val position: Int,
    val title: String,
    val items: List<ItemRearrangeListItem>
) : ItemForm(R.layout.form_item_rearrange_list)

data class ItemRearrangeListItem(
    override val position: Int,
    val inputId: Long,
    val title: String,
    val value: String
) : ItemForm(R.layout.form_item_rearrange)

data class ItemRow(
    override val position: Int,
    val items: List<ItemForm>
) : ItemForm(R.layout.form_item_row)

data class ItemHeader(
    override val position: Int,
    val title: String
) : ItemForm(R.layout.form_item_header)

data class ItemHeader2(
    override val position: Int,
    val title: String
) : ItemForm(R.layout.form_item_header2)

data class ItemHeader3(
    override val position: Int,
    val title: String
) : ItemForm(R.layout.form_item_header3)

data class ItemErrorPlaceholder(
    val largestError: String,
    val item: ItemForm
) : ItemForm(R.layout.form_item_error_placeholder) {

    override val position: Int = item.position
}

