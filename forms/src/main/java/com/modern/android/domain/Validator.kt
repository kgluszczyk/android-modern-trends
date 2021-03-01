package com.modern.android.forms.domain

import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.ValidationError

interface Validator {

    fun execute(form: Form): List<ValidationError>
    fun execute(questionIds: List<Long>, contextForm: Form): List<ValidationError>
}