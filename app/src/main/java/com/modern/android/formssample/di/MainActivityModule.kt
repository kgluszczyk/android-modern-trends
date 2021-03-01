package com.modern.android.formssample.di

import com.modern.android.di.scopes.FragmentScope
import com.modern.android.forms.FormRendererFragment
import com.modern.android.forms.di.FormRendererFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityModule {
    @FragmentScope
    @ContributesAndroidInjector(modules = [FormRendererFragmentModule::class])
    abstract fun contributeFormRendererFragment(): FormRendererFragment
}