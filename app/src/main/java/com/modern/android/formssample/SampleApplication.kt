package com.modern.android.formssample

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.jakewharton.threetenabp.AndroidThreeTen
import com.modern.android.formssample.di.DatabaseModule
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class SampleApplication: Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        DatabaseModule.init(this)
        AndroidThreeTen.init(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}