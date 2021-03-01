package com.modern.android.forms.domain

import androidx.annotation.WorkerThread
import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.FormContext

interface LoadForm {

    @WorkerThread
    fun execute(formContext: FormContext): Form
}