package com.modern.android.di

import android.content.Context
import com.modern.android.forms.domain.DeleteExpiredData
import com.modern.android.forms.domain.DeleteExpiredFormData
import com.modern.android.forms.domain.DeleteForm
import com.modern.android.forms.domain.DeleteFormImpl
import com.modern.android.forms.domain.FormInteractionsConfig
import com.modern.android.forms.domain.FormValidator
import com.modern.android.forms.domain.GetAnswers
import com.modern.android.forms.domain.GetAnswersImpl
import com.modern.android.forms.domain.GetForm
import com.modern.android.forms.domain.HasAnyForm
import com.modern.android.forms.domain.HasAnyFormImpl
import com.modern.android.forms.domain.HasAnyPendingForms
import com.modern.android.forms.domain.HasAnyPendingFormsImpl
import com.modern.android.forms.domain.LoadForm
import com.modern.android.forms.domain.LoadFormImpl
import com.modern.android.forms.domain.MarkFormAsStarted
import com.modern.android.forms.domain.MarkFormAsStartedImpl
import com.modern.android.forms.domain.SaveAnswers
import com.modern.android.forms.domain.SaveAnswersImpl
import com.modern.android.forms.domain.SendAnswers
import com.modern.android.forms.domain.SendAnswersImpl
import com.modern.android.forms.domain.SendForm
import com.modern.android.forms.domain.Validator
import com.modern.android.forms.repository.AnswersRepository
import com.modern.android.forms.repository.FormRepository
import com.modern.commons.network.InternetStateLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
open class DefaultFormInternalsModule {

    @Provides
    open fun provideDeleteExpiredData(
        answersRepository: AnswersRepository,
        formsRepository: FormRepository
    ): DeleteExpiredData = DeleteExpiredFormData(answersRepository, formsRepository)

    @Provides
    open fun provideValidator(context: Context): Validator = FormValidator(context)

    @Provides
    open fun provideDeleteForm(
        answersRepository: AnswersRepository,
        formsRepository: FormRepository
    ): DeleteForm = DeleteFormImpl(formsRepository, answersRepository)

    @Provides
    open fun provideGetAnswers(answersRepository: AnswersRepository): GetAnswers = GetAnswersImpl(answersRepository)

    @Provides
    open fun provideHasAnyPendingFormsImpl(
        formsRepository: FormRepository,
        answersRepository: AnswersRepository
    ): HasAnyPendingForms = HasAnyPendingFormsImpl(formsRepository, answersRepository)

    @Provides
    open fun provideHasAnyFormImpl(
        formsRepository: FormRepository
    ): HasAnyForm = HasAnyFormImpl(formsRepository)

    @Provides
    open fun provideLoadForm(
        getForm: GetForm,
        formsRepository: FormRepository,
        answersRepository: AnswersRepository,
        internetStateLiveData: InternetStateLiveData
    ): LoadForm = LoadFormImpl(
        getForm,
        formsRepository,
        internetStateLiveData,
        answersRepository
    )

    @Provides
    open fun provideMarkFormAsStarted(
        formsRepository: FormRepository,
        answersRepository: AnswersRepository
    ): MarkFormAsStarted = MarkFormAsStartedImpl(formsRepository, answersRepository)

    @Provides
    open fun provideSaveAnswers(answersRepository: AnswersRepository): SaveAnswers = SaveAnswersImpl(answersRepository)

    @Provides
    open fun provideSendAnswers(
        sendForm: SendForm,
        deleteForm: DeleteForm,
        loadForm: LoadForm,
        formInteractionsConfig: FormInteractionsConfig
    ): SendAnswers = SendAnswersImpl(sendForm, loadForm, deleteForm, formInteractionsConfig)
}