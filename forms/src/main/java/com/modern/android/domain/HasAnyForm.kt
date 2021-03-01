package com.modern.android.forms.domain

import androidx.annotation.WorkerThread
import com.modern.android.forms.entity.FormContext

interface HasAnyForm {

    @WorkerThread
    fun execute(formContext: FormContext) : Boolean
}