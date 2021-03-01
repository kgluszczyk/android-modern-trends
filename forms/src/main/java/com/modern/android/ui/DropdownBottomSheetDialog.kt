package com.modern.android.forms.ui

import android.content.Context
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog

import com.modern.android.forms.R
import com.modern.android.forms.databinding.FormDropDownBinding
import com.modern.android.forms.databinding.FormDropDownItemBinding

class DropdownBottomSheetDialog(
    context: Context,
    private val options: List<String>,
    private val selectedOption: String,
    private val listener: (String) -> Unit
) : BottomSheetDialog(context, R.style.Form_BottomSheetDialog) {

    init {
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setContentView(FormDropDownBinding.inflate(layoutInflater).apply {
            container.addOptions()
            close.setOnClickListener { dismiss() }
        }.root)
    }

    private fun ViewGroup.addOptions() {
        options.forEach { option ->
            FormDropDownItemBinding.inflate(layoutInflater, this, true).item.apply {
                text = option
                setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.form_selected.takeIf { option == selectedOption } ?: 0,
                    0
                )
                setOnClickListener {
                    listener(option)
                    dismiss()
                }
            }
        }
    }
}

