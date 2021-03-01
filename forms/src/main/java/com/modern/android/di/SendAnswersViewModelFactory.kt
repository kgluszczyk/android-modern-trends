package com.modern.android.forms.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.modern.android.forms.domain.SendAnswers
import com.modern.android.forms.ui.send.SendAnswersViewModel
import javax.inject.Inject

class SendAnswersViewModelFactory @Inject constructor(
    private val sendForm: SendAnswers
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SendAnswersViewModel(
            sendForm
        ) as T
    }
}