package com.modern.android.forms.domain

import com.modern.android.forms.entity.FormContext
import com.modern.android.forms.repository.AnswersRepository
import com.modern.android.forms.repository.FormRepository

class DeleteFormImpl constructor(
    private val formRepository: FormRepository,
    private val answersRepository: AnswersRepository
) : DeleteForm {

    override fun execute(formContext: FormContext) {
        execute(formContext, formRepository.getByContext(formContext)?.id ?: return)
    }

    override fun execute(formContext: FormContext, formId: Long) {
        answersRepository.delete(formContext, formId)
        formRepository.deleteByContext(formContext, formId)
    }
}