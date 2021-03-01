package com.modern.android.forms.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.modern.android.forms.presentation.FormAnswerSavedProvider
import com.modern.android.forms.presentation.FormValidationStateProvider
import com.modern.android.forms.ui.summary.FormSummaryViewModel
import io.reactivex.Observable
import javax.inject.Inject

class FormSummaryViewModelFactory @Inject constructor(
    private val answersSavedProvider: FormAnswerSavedProvider,
    private val hasInternetConnectionObservable: Observable<Boolean>,
    private val validationStateProvider: FormValidationStateProvider
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FormSummaryViewModel(
            answersSavedProvider,
            hasInternetConnectionObservable,
            validationStateProvider
        ) as T
    }
}