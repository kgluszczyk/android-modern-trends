package com.modern.android.forms.domain

import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.FormAnswers
import com.modern.android.forms.entity.FormContext
import com.modern.android.forms.repository.AnswersRepository

class GetAnswersImpl constructor(private val answersRepository: AnswersRepository) : GetAnswers {

    override fun execute(context: FormContext, form: Form) = answersRepository.getForForm(context, form)
        ?: FormAnswers(form.id, emptyList())
}