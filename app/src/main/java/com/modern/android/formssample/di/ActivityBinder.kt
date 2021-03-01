package com.modern.android.formssample.di

import com.modern.android.formssample.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBinder {
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindDashboardActivity(): MainActivity
}