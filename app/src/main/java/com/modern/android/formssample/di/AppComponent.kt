package com.modern.android.formssample.di

import android.app.Application
import com.modern.android.di.DefaultFormInternalsModule
import com.modern.android.formssample.SampleApplication
import com.modern.android.formssample.common.AppScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector

@AppScope
@Component(
    modules = [AppModule::class,
        DatabaseModule::class,
        ActivityBinder::class,
        AndroidInjectionModule::class,
        FormModule::class,
        DefaultFormInternalsModule::class]
)
interface AppComponent : AndroidInjector<SampleApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun appModule(@AppScope app: Application): Builder
        fun add(dbModule: DatabaseModule): Builder
        fun build(): AppComponent
    }
}
