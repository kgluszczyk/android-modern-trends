package com.modern.android.forms.ui.page

import androidx.lifecycle.ViewModel
import com.modern.android.forms.entity.ItemAnswer
import com.modern.android.forms.entity.Page
import com.modern.android.forms.presentation.FormAnswerProvider
import com.modern.android.forms.presentation.FormValidationStateProvider
import com.modern.android.forms.ui.toItems
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class PageViewModel constructor(
    private val validationStateProvider: FormValidationStateProvider,
    private val formAnswerProvider: FormAnswerProvider
) : ViewModel() {

    private val pageSubject = BehaviorSubject.create<Page>()
    val items: Observable<List<ItemForm>> = Observables.combineLatest(pageSubject, validationStateProvider.observe())
        .observeOn(Schedulers.io())
        .map { (page, errors) -> page.toItems(errors) }
        .observeOn(AndroidSchedulers.mainThread())

    fun updateData(page: Page) {
        pageSubject.onNext(page)
    }

    fun updateAnswer(answers: List<ItemAnswer>) {
        formAnswerProvider.update(answers)
        validationStateProvider.clearError(answers.map { it.questionId })
    }
}
