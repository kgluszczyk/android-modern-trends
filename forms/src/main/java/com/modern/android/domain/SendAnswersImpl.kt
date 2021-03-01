package com.modern.android.forms.domain

import timber.log.Timber

class SendAnswersImpl constructor(
    private val sendForm: SendForm,
    private val loadForm: LoadForm,
    private val deleteForm: DeleteForm,
    private val formInteractionsConfig: FormInteractionsConfig,
    ) : SendAnswers {

    override fun execute(formResults: com.modern.android.forms.entity.FormResults) {
        sendForm.execute(formResults.formContext, formResults.formAnswers)

        runCatching { deleteForm.execute(formResults.formContext, formResults.formAnswers.formId) }
            .onFailure { Timber.e(it, "Failed to remove form and any associated data after successful send") }
            .onSuccess {
                if (formInteractionsConfig.shouldLoadAfterSubmission()) {
                    loadForm.execute(formResults.formContext)
                }
            }
    }
}