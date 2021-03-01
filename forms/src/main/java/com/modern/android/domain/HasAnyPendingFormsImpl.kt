package com.modern.android.forms.domain

import androidx.annotation.WorkerThread
import com.modern.android.forms.entity.FormAnswers
import com.modern.android.forms.repository.AnswersRepository
import com.modern.android.forms.repository.FormRepository

class HasAnyPendingFormsImpl constructor(
    private val formRepository: FormRepository,
    private val answersRepository: AnswersRepository
) : HasAnyPendingForms {

    @WorkerThread
    override fun execute(formContext: com.modern.android.forms.entity.FormContext): Boolean {
        val form = formRepository.getByContext(formContext) ?: return false
        return answersRepository.getForForm(formContext, form)?.hasAnyAnswers() == true
    }

    private fun FormAnswers.hasAnyAnswers() = answers.isNotEmpty()
}