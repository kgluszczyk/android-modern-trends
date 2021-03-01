package com.modern.android.forms.domain

import com.modern.android.forms.entity.FormResults

interface SendAnswers {

    fun execute(formResults: FormResults)
}