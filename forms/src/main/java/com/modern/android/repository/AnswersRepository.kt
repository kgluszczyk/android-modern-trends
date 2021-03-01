package com.modern.android.forms.repository

import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.FormAnswers
import com.modern.android.forms.entity.FormContext
import org.threeten.bp.LocalDate

interface AnswersRepository {

    fun save(context: FormContext, answers: FormAnswers)

    fun delete(context: FormContext, formId: Long)

    fun getForForm(context: FormContext, form: Form): FormAnswers?

    fun deleteOlderThan(date: LocalDate)
}