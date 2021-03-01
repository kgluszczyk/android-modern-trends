package com.modern.android.forms.di

import androidx.lifecycle.LiveDataReactiveStreams
import com.modern.android.forms.ui.summary.FormSummaryFragment
import com.modern.commons.network.InternetStateLiveData
import dagger.Module
import dagger.Provides
import io.reactivex.Observable

@Module
class FormSummaryFragmentModule {

    @Provides
    fun provideInternetStateObservable(
        formSummaryFragment: FormSummaryFragment,
        hasInternetConnectionLiveData: InternetStateLiveData
    ) = Observable.fromPublisher(
        LiveDataReactiveStreams.toPublisher(
            formSummaryFragment,
            hasInternetConnectionLiveData
        )
    )
}