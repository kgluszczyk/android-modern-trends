package com.modern.android.forms.domain

import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.FormAnswers
import com.modern.android.forms.entity.FormContext

interface GetAnswers {

    fun execute(context: FormContext, form: Form): FormAnswers
}