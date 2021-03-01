package com.modern.android.forms.domain

import androidx.annotation.WorkerThread
import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.FormContext
import com.modern.android.forms.repository.AnswersRepository
import com.modern.android.forms.repository.FormRepository
import com.modern.commons.network.InternetStateLiveData

class LoadFormImpl constructor(
    private val getForm: GetForm,
    private val formRepository: FormRepository,
    private val internetStateLiveData: InternetStateLiveData,
    private val answersRepository: AnswersRepository
) : LoadForm {

    @WorkerThread
    override fun execute(formContext: FormContext): Form {
        return formRepository.getByContext(formContext)?.let {
            val isFormPartiallyFilled = answersRepository.getForForm(formContext, it)?.answers?.isNotEmpty() ?: false
            if (!isFormPartiallyFilled && internetStateLiveData.hasInternetConnection()) {
                return@let fetchAndSave(formContext)
            } else {
                return@let it
            }
        }?: fetchAndSave(formContext)
    }


    private fun fetchAndSave(formContext: FormContext) = getForm.execute(formContext).also { formRepository.replaceWithinContext(formContext, it) }
}