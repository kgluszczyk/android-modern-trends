package com.modern.android.forms.domain

import androidx.annotation.WorkerThread
import com.modern.android.forms.entity.FormContext

interface MarkFormAsStarted {

    @WorkerThread
    fun execute(formContext: FormContext)
}