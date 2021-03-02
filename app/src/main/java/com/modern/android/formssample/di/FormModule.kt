package com.modern.android.formssample.di

import android.content.Context
import com.modern.android.forms.domain.FormInteractionsConfig
import com.modern.android.forms.domain.GetForm
import com.modern.android.forms.domain.SendForm
import com.modern.android.formssample.repository.GetFormImpl
import com.modern.android.formssample.repository.SendFormImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class FormModule {

    @Provides
    fun provideGetForm(context: Context): GetForm = GetFormImpl(context)

    @Provides
    fun provideSendForm(): SendForm = SendFormImpl()

    @Provides
    fun provideFormInteractionsConfig() = object : FormInteractionsConfig {
        override fun shouldLoadAfterSubmission() = true
    }

}