package com.modern.android.forms.domain

import com.modern.android.forms.entity.FormAnswers
import com.modern.android.forms.entity.FormContext

interface SendForm {

    fun execute(formContext: FormContext, answers: FormAnswers)
}