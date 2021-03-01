package com.modern.android.forms.domain

import com.modern.android.forms.entity.FormAnswers
import com.modern.android.forms.entity.FormContext
import com.modern.android.forms.repository.AnswersRepository

class SaveAnswersImpl constructor(private val answersRepository: AnswersRepository) : SaveAnswers {

    override fun execute(context: FormContext, answers: FormAnswers) = answersRepository.save(context, answers)
}