package com.modern.android.forms.domain

import com.modern.android.forms.entity.FormContext

interface DeleteForm {

    fun execute(formContext: FormContext)
    fun execute(formContext: FormContext, formId: Long)
}