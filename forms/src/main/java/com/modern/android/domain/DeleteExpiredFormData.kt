package com.modern.android.forms.domain

import androidx.annotation.WorkerThread
import com.modern.android.forms.repository.AnswersRepository
import com.modern.android.forms.repository.FormRepository
import org.threeten.bp.LocalDate
import timber.log.Timber

class DeleteExpiredFormData constructor(
    private val answersRepository: AnswersRepository,
    private val formsRepository: FormRepository
) : DeleteExpiredData {

    @WorkerThread
    override fun execute() {
        val date = LocalDate.now().minusDays(1)
        runCatching { answersRepository.deleteOlderThan(date) }
            .onFailure { Timber.e(it, "Error while removing expired forms") }
        runCatching { formsRepository.deleteOlderThan(date) }
            .onFailure { Timber.e(it, "Error while removing expired answers") }
    }
}