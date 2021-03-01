package com.modern.android.forms.presentation

import com.modern.android.di.scopes.FragmentScope
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@FragmentScope
class FormAnswerSavedProvider @Inject constructor() {
    private val subject = BehaviorSubject.create<Boolean>()

    fun observe() : Observable<Boolean> = subject.observeOn(Schedulers.io())

    fun update(value: Boolean) = subject.onNext(value)
}