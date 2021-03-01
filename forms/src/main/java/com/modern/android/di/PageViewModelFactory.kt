package com.modern.android.forms.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.modern.android.forms.presentation.FormAnswerProvider
import com.modern.android.forms.presentation.FormValidationStateProvider
import com.modern.android.forms.ui.page.PageViewModel
import javax.inject.Inject

class PageViewModelFactory @Inject constructor(
    private val validationStateProvider: FormValidationStateProvider,
    private val formAnswerProvider: FormAnswerProvider
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PageViewModel(
            validationStateProvider,
            formAnswerProvider
        ) as T
    }
}