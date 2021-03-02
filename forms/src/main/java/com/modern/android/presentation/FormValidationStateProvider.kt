package com.modern.android.forms.presentation

import com.modern.android.forms.entity.ValidationError
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@ActivityRetainedScoped
class FormValidationStateProvider @Inject constructor() {
    private val subject = BehaviorSubject.createDefault(emptyList<ValidationError>())

    fun observe() : Observable<List<ValidationError>> = subject.observeOn(Schedulers.io())
        .distinctUntilChanged()

    fun update(value: List<ValidationError>) = subject.onNext(value)

    fun clearError(ids: List<Long>) = subject.onNext(
        subject.value?.filter { it.id !in ids }.orEmpty()
    )

    fun currentValue(): List<ValidationError> = subject.value ?: emptyList()
}