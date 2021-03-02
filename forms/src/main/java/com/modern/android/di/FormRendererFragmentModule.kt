package com.modern.android.forms.di

import com.modern.android.forms.FormRendererFragment
import com.modern.android.forms.ui.SummaryCallback
import com.modern.android.forms.ui.page.FormPageFragment
import com.modern.android.forms.ui.send.SendAnswersDialogFragment
import com.modern.android.forms.ui.summary.FormSummaryFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector

@Module
abstract class FormRendererFragmentModule {

    @ContributesAndroidInjector(modules = [PageFragmentModule::class])
    abstract fun contributePageFragment(): FormPageFragment

    @ContributesAndroidInjector(modules = [FormSummaryFragmentModule::class])
    abstract fun contributeFormSummaryFragment(): FormSummaryFragment

    @ContributesAndroidInjector
    abstract fun contributeSendAnswersDialogFragment(): SendAnswersDialogFragment

    @Binds
    abstract fun summaryCallback(fragment: FormRendererFragment): SummaryCallback

    companion object {

        @Provides
        fun provideForContext(fragment: Fragment) = fragment.formContext

    }

}