package com.modern.android.forms.presentation

import com.modern.android.di.scopes.FragmentScope
import com.modern.android.forms.entity.ItemAnswer
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

@FragmentScope
class FormAnswerProvider @Inject constructor() {
    private val subject = PublishSubject.create<List<ItemAnswer>>()

    fun observe() : Observable<List<ItemAnswer>> = subject.observeOn(AndroidSchedulers.mainThread())

    fun update(answers: List<ItemAnswer>) = subject.onNext(answers)
}