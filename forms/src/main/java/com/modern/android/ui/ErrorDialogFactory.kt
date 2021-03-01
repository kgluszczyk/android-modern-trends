package com.modern.android.forms.ui

import android.content.Context
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import com.modern.android.forms.databinding.FormErrorDialogBinding

fun Context.showError(
    @StringRes titleRes: Int,
    @StringRes messageRes: Int,
    onCancel: () -> Unit,
    onRefresh: () -> Unit
) {
    val viewBinding = FormErrorDialogBinding.inflate(LayoutInflater.from(this))
    AlertDialog.Builder(this)
        .setView(viewBinding.root)
        .setCancelable(true)
        .setOnCancelListener { onCancel() }
        .create()
        .also { dialog ->
            viewBinding.apply {
                title.setText(titleRes)
                message.setText(messageRes)
                refresh.setOnClickListener {
                    onRefresh()
                    dialog.dismiss()
                }
            }
        }
        .show()
}