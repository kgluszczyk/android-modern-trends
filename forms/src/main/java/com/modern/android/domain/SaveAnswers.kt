package com.modern.android.forms.domain

import com.modern.android.forms.entity.FormAnswers
import com.modern.android.forms.entity.FormContext

interface SaveAnswers {

    fun execute(context: FormContext, answers: FormAnswers)
}