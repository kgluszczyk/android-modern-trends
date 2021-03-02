package com.modern.android.forms.ui.summary

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.ViewModel
import com.modern.android.forms.entity.Form
import com.modern.android.forms.presentation.FormAnswerSavedProvider
import com.modern.android.forms.presentation.FormValidationStateProvider
import com.modern.android.forms.ui.toItems
import com.modern.commons.network.InternetStateLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class FormSummaryViewModel @Inject constructor(
    hasInternetConnectionLiveData: InternetStateLiveData,
    answersSavedProvider: FormAnswerSavedProvider,
    validationStateProvider: FormValidationStateProvider
) : ViewModel(), LifecycleOwner {

    private val compositeDisposable = CompositeDisposable()
    private val lifecycleRegistry = LifecycleRegistry(this)

    private val formSubject = BehaviorSubject.create<Form>()

    private val hasInternetConnectionObservable = Observable.fromPublisher(
        LiveDataReactiveStreams.toPublisher(
            this,
            hasInternetConnectionLiveData
        )
    )
    val items: Observable<List<SummaryItem>> = Observables.combineLatest(
        formSubject,
        hasInternetConnectionObservable,
        validationStateProvider.observe().map { errors -> errors.map { it.id } }
    )
        .observeOn(AndroidSchedulers.mainThread())
        .map { (form, hasInternetConnection, errorItemsIds) -> form.toItems(hasInternetConnection, errorItemsIds) }

    private val fragmentVisibilitySubject = BehaviorSubject.createDefault(false)
    private val showNotificationSubject = BehaviorSubject.createDefault(false)
    val showNotification: Observable<Boolean> = Observables.combineLatest(
        showNotificationSubject,
        fragmentVisibilitySubject
    ) { showNotification, visible -> showNotification && visible }
        .observeOn(AndroidSchedulers.mainThread())

    init {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        answersSavedProvider.observe()
            .onErrorReturn { false }
            .filter { it }
            .subscribe {
                showNotificationSubject.onNext(true)
                answersSavedProvider.update(false)
            }
            .addTo(compositeDisposable)
    }

    fun updateFragmentVisibility(visible: Boolean) {
        fragmentVisibilitySubject.onNext(visible)
    }

    fun notificationShown() = showNotificationSubject.onNext(false)

    fun updateData(form: Form) {
        formSubject.onNext(form)
    }

    override fun onCleared() {
        super.onCleared()
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        compositeDisposable.clear()
    }

    override fun getLifecycle() = lifecycleRegistry
}