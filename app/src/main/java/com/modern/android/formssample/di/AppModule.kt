package com.modern.android.formssample.di

import android.app.Application
import android.content.Context
import com.modern.commons.network.InternetStateLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideContxt(application: Application): Context =
        application.applicationContext

    @Provides
    @Singleton
    fun provideInternetStateLiveData(context: Context): InternetStateLiveData {
        return InternetStateLiveData(context)
    }
}