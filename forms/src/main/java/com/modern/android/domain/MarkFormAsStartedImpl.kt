package com.modern.android.forms.domain

import androidx.annotation.WorkerThread
import com.modern.android.forms.entity.FormAnswers
import com.modern.android.forms.entity.FormContext
import com.modern.android.forms.entity.Input
import com.modern.android.forms.entity.ItemAnswer
import com.modern.android.forms.repository.AnswersRepository
import com.modern.android.forms.repository.FormRepository

class MarkFormAsStartedImpl constructor(
    private val formRepository: FormRepository,
    private val answersRepository: AnswersRepository
) : MarkFormAsStarted {

    @WorkerThread
    override fun execute(formContext: FormContext) {
        val form = formRepository.getByContext(formContext) ?: return
        //todo improve performance
        val answers = form.pages
            .map { it.items }
            .flatten().filterIsInstance<Input>().take(1).map {
                ItemAnswer(
                    questionId = it.id,
                    answer = it.value ?: "",
                    type = ItemAnswer.AnswerType.STRING
                )
            }
            .toList()
        answersRepository.save(
            formContext, FormAnswers(formId = form.id, answers = answers)
        )
    }

}