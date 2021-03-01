package com.modern.android.forms.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.modern.android.forms.domain.GetAnswers
import com.modern.android.forms.domain.LoadForm
import com.modern.android.forms.domain.SaveAnswers
import com.modern.android.forms.domain.Validator
import com.modern.android.forms.entity.FormContext
import com.modern.android.forms.presentation.FormAnswerProvider
import com.modern.android.forms.presentation.FormAnswerSavedProvider
import com.modern.android.forms.presentation.FormRendererViewModel
import com.modern.android.forms.presentation.FormValidationStateProvider
import javax.inject.Inject

class FormRendererViewModelFactory @Inject constructor(
    private val formContext: FormContext,
    private val loadForm: LoadForm,
    private val getAnswers: GetAnswers,
    private val saveAnswers: SaveAnswers,
    private val formAnswerProvider: FormAnswerProvider,
    private val answersSavedProvider: FormAnswerSavedProvider,
    private val formValidator: Validator,
    private val formValidationStateProvider: FormValidationStateProvider
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FormRendererViewModel(
            formContext,
            loadForm,
            getAnswers,
            saveAnswers,
            formAnswerProvider,
            answersSavedProvider,
            formValidator,
            formValidationStateProvider
        ) as T
    }
}