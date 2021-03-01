package com.modern.android.forms.repository

import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.FormContext
import org.threeten.bp.LocalDate

interface FormRepository {

    fun replaceWithinContext(context: FormContext, form: Form): Long

    fun deleteByContext(context: FormContext, formId: Long)

    fun getByContext(context: FormContext): Form?

    fun deleteOlderThan(date: LocalDate)
}