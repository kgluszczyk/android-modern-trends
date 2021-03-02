package com.modern.android.presentation

import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject

class NavigationSummaryViewModel : ViewModel() {

    private val navigationSubject = BehaviorSubject.create<NavigateAction>()
    val navigationActions = navigationSubject.observeOn(AndroidSchedulers.mainThread())

    fun setNavigationAction(navigatioNavigateAction: NavigateAction){
        navigationSubject.onNext(navigatioNavigateAction)
    }


    sealed class NavigateAction {
        data class ScrollToPage(val page: Int) : NavigateAction()
        object OnSendClick : NavigateAction()

    }

}