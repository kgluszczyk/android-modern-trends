package com.modern.android.forms.domain

import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.FormContext

interface GetForm {

    fun execute(context: FormContext): Form
}