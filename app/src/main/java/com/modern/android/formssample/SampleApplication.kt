package com.modern.android.formssample

import android.content.Context
import androidx.multidex.MultiDex
import com.jakewharton.threetenabp.AndroidThreeTen
import com.modern.android.formssample.di.DaggerAppComponent
import com.modern.android.formssample.di.DatabaseModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class SampleApplication: DaggerApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        DatabaseModule.init(this)
        return DaggerAppComponent.builder()
            .appModule(this)
            .add(DatabaseModule)
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}