package com.modern.android.forms.presentation

import com.modern.android.forms.entity.ItemAnswer
import dagger.hilt.android.scopes.ActivityRetainedScoped
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@ActivityRetainedScoped
class FormAnswerProvider @Inject constructor() {
    private val subject = PublishSubject.create<List<ItemAnswer>>()

    fun observe() : Observable<List<ItemAnswer>> = subject.observeOn(AndroidSchedulers.mainThread())

    fun update(answers: List<ItemAnswer>) = subject.onNext(answers)
}