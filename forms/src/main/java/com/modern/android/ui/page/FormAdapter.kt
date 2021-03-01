package com.modern.android.forms.ui.page

import androidx.recyclerview.widget.RecyclerView
import com.modern.android.forms.R
import com.modern.android.forms.databinding.FormItemCheckboxBinding
import com.modern.android.forms.databinding.FormItemDropdownBinding
import com.modern.android.forms.databinding.FormItemErrorPlaceholderBinding
import com.modern.android.forms.databinding.FormItemHeader2Binding
import com.modern.android.forms.databinding.FormItemHeader3Binding
import com.modern.android.forms.databinding.FormItemHeaderBinding
import com.modern.android.forms.databinding.FormItemInputBinding
import com.modern.android.forms.databinding.FormItemRearrangeListBinding
import com.modern.android.forms.databinding.FormItemRowBinding
import com.modern.android.forms.databinding.FormItemSectionBinding
import com.modern.android.forms.databinding.FormItemToggleBinding
import com.modern.android.forms.databinding.FormItemValueBinding
import com.modern.android.forms.entity.ItemAnswer
import com.modern.commons.android.list2.DiffAdapter2
import com.modern.commons.android.list2.ViewHolderFactory2

class FormAdapter(private val listener: OnValueChangedListener) : DiffAdapter2<ItemForm>() {
    override val factory: ViewHolderFactory2<ItemForm> = { layoutInflater, viewGroup, viewType ->
        when (viewType) {
            R.layout.form_item_section -> SectionViewHolder(
                FormItemSectionBinding.inflate(layoutInflater, viewGroup, false)
            )
            R.layout.form_item_header -> HeaderViewHolder(
                FormItemHeaderBinding.inflate(layoutInflater, viewGroup, false)
            )
            R.layout.form_item_header2 -> Header2ViewHolder(
                FormItemHeader2Binding.inflate(layoutInflater, viewGroup, false)
            )
            R.layout.form_item_header3 -> Header3ViewHolder(
                FormItemHeader3Binding.inflate(layoutInflater, viewGroup, false)
            )
            R.layout.form_item_checkbox -> CheckboxViewHolder(
                FormItemCheckboxBinding.inflate(layoutInflater, viewGroup, false),
                listener
            )
            R.layout.form_item_toggle -> ToggleViewHolder(
                FormItemToggleBinding.inflate(layoutInflater, viewGroup, false),
                listener
            )
            R.layout.form_item_input -> InputViewHolder(
                FormItemInputBinding.inflate(layoutInflater, viewGroup, false),
                listener
            )
            R.layout.form_item_dropdown -> DropdownViewHolder(
                FormItemDropdownBinding.inflate(layoutInflater, viewGroup, false),
                listener
            )
            R.layout.form_item_value -> ValueViewHolder(
                FormItemValueBinding.inflate(layoutInflater, viewGroup, false)
            )
            R.layout.form_item_row -> RowViewHolder(
                FormItemRowBinding.inflate(layoutInflater, viewGroup, false),
                (viewGroup as RecyclerView).recycledViewPool,
                listener
            )
            R.layout.form_item_rearrange_list -> RearrangeItemListViewHolder(
                FormItemRearrangeListBinding.inflate(layoutInflater, viewGroup, false),
                (viewGroup as RecyclerView).recycledViewPool,
                listener
            )
            R.layout.form_item_error_placeholder -> ErrorPlaceholderViewHolder(
                FormItemErrorPlaceholderBinding.inflate(layoutInflater, viewGroup, false),
                viewGroup as RecyclerView
            )
            else -> throw IllegalStateException("Unknown view type $viewType")
        }
    }

    override fun hasChangePayload(old: ItemForm, new: ItemForm): Boolean {
        return old.javaClass == new.javaClass
    }
}

interface OnValueChangedListener {
    fun onValuesChanged(answers: List<ItemAnswer>)

    fun onValueChanged(answer: ItemAnswer) {
        onValuesChanged(listOf(answer))
    }
}