package com.modern.android.forms.domain

import androidx.annotation.WorkerThread
import com.modern.android.forms.repository.FormRepository

class HasAnyFormImpl constructor(
    private val formRepository: FormRepository,
) : HasAnyForm {

    @WorkerThread
    override fun execute(formContext: com.modern.android.forms.entity.FormContext): Boolean {
        return formRepository.getByContext(formContext) != null
    }
}