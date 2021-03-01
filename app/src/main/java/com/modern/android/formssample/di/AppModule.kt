package com.modern.android.formssample.di

import android.app.Application
import android.content.Context
import com.modern.android.formssample.common.AppScope
import com.modern.commons.network.InternetStateLiveData
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Provides
    @AppScope
    fun provideContxt(application: Application): Context =
        application.applicationContext

    @Provides
    @AppScope
    fun provideInternetStateLiveData(context: Context): InternetStateLiveData {
        return InternetStateLiveData(context)
    }
}