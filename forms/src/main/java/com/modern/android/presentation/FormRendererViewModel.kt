package com.modern.android.forms.presentation

import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.modern.android.forms.domain.GetAnswers
import com.modern.android.forms.domain.LoadForm
import com.modern.android.forms.domain.SaveAnswers
import com.modern.android.forms.domain.Validator
import com.modern.android.forms.entity.Form
import com.modern.android.forms.entity.FormAnswers
import com.modern.android.forms.entity.FormContext
import com.modern.android.forms.entity.FormItem
import com.modern.android.forms.entity.FormResults
import com.modern.android.forms.entity.ItemAnswer
import com.modern.android.forms.ui.applyAnswers
import com.modern.android.forms.ui.toItems
import com.modern.android.forms.wrapUnauthorizedErrorIfNeeded
import com.modern.commons.utils.Resource
import com.modern.commons.utils.ResourceError
import com.modern.commons.utils.ResourceLoading
import com.modern.commons.utils.ResourceSuccess
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit

private const val AUTO_SAVE_DELAY_S = 30L

class FormRendererViewModel constructor(
    private val formContext: FormContext,
    private val loadForm3: LoadForm,
    private val getAnswers: GetAnswers,
    private val saveAnswers: SaveAnswers,
    private val formAnswerProvider: FormAnswerProvider,
    private val answersSavedProvider: FormAnswerSavedProvider,
    private val formValidator: Validator,
    private val formValidationStateProvider: FormValidationStateProvider
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val formSubject = BehaviorSubject.createDefault<Resource<Form, Throwable>>(ResourceLoading)
    val itemsRx: Observable<Resource<List<FormItem>, Throwable>> = formSubject.map { it.toItemsResource() }
        .observeOn(AndroidSchedulers.mainThread())
    val itemsLiveData = MutableLiveData<Resource<List<FormItem>, Throwable>>(ResourceLoading)
    val itemsStateFlow = MutableStateFlow<Resource<List<FormItem>, Throwable>>(ResourceLoading)

    //LB+DB Strumień zapisanych, niepustych odpowiedzi

    private val saveSubject = PublishSubject.create<Boolean>()

    private var formId = 0L
    private val answersMap = mutableMapOf<Long, ItemAnswer>()

    init {
        loadData()
    }

    fun loadData() {
        loadDataCoroutinesStateFlow()
        //loadDataCoroutinesLiveData()
        //loadDataRx()
    }

    private fun loadDataRx() {
        Single.fromCallable { loadForm() }
            .subscribeOn(Schedulers.io())
            .doOnSubscribe { formSubject.onNext(ResourceLoading) }
            .map { it.applyAnswers(getAnswers.execute(formContext, it)) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ form: Form ->
                           formSubject.onNext(ResourceSuccess(form))
                           observeAnswers()
                           observeSaveTriggers()
                       }, {
                           Timber.e(it, "Error while loading form")
                           formSubject.onNext(ResourceError(wrapUnauthorizedErrorIfNeeded(it)))
                       })
            .addTo(compositeDisposable)
    }

    private fun loadDataCoroutinesStateFlow() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                ::loadForm.asFlow().map {
                    it.applyAnswers(getAnswers.execute(formContext, it))
                }.collect { form: Form ->
                    itemsStateFlow.value = ResourceSuccess(form).toItemsResource()
                    withContext(Dispatchers.Main) {
                        runCatching {
                            itemsStateFlow.value = ResourceSuccess(form).toItemsResource()
                            observeAnswers()
                            observeSaveTriggers()
                        }.onFailure {
                            Timber.e(it, "Error while loading form")
                            itemsStateFlow.value = ResourceError(wrapUnauthorizedErrorIfNeeded(it)).toItemsResource()
                        }
                    }
                }
            }
        }
    }

    private fun loadDataCoroutinesLiveData() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                ::loadForm.asFlow().map {
                    it.applyAnswers(getAnswers.execute(formContext, it))
                }.collect { form: Form ->
                    itemsStateFlow.value = ResourceSuccess(form).toItemsResource()
                    withContext(Dispatchers.Main) {
                        runCatching {
                            itemsLiveData.value = ResourceSuccess(form).toItemsResource()
                            observeAnswers()
                            observeSaveTriggers()
                        }.onFailure {
                            Timber.e(it, "Error while loading form")
                            itemsStateFlow.value = ResourceError(wrapUnauthorizedErrorIfNeeded(it)).toItemsResource()
                        }
                    }
                }
            }
        }
    }

    private fun observeSaveTriggers() {
        saveSubject.observeOn(Schedulers.io())
            .startWith(false)
            .map { createFormAnswers() }
            .skip(1) // skip start with value
            .doOnNext {
                //LB+DB Zapisy odpowiedzi
                println("Save me")
                runCatching { saveAnswers.execute(formContext, it) }
                    .onSuccess { answersSavedProvider.update(true) }
                    .onFailure { Timber.e(it, "Failed to save answers") }
            }.subscribe(
                { Timber.d("Answers has been saved") },
                { Timber.e(it, "Error while saving answers") }
            )
            .addTo(compositeDisposable)
    }

    private fun observeAnswers() {
        formAnswerProvider.observe()
            .doOnNext { answers ->
                answers.forEach {
                    answersMap[it.questionId] = it
                }
                saveSubject.onNext(false)
            }
            .subscribe(
                { itemAnswers ->
                    formSubject.value?.data
                        ?.applyAnswers(answersMap)
                        ?.let {
                            formSubject.onNext(ResourceSuccess(it))
                            validateAnswers(itemAnswers.map { it.questionId })
                        }
                }, { Timber.e(it, "Error while updating answers") })
            .addTo(compositeDisposable)
    }

    fun saveAnswers() {
        saveSubject.onNext(true)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    @WorkerThread
    private fun loadForm(): Form {
        return loadForm3.execute(formContext).also {
            formId = it.id
        }
    }

    fun validateAnswers(): Boolean {
        val form = formSubject.value?.data ?: return false
        return formValidator.execute(form).also { errors ->
            formValidationStateProvider.update(errors)
        }.isEmpty()
    }

    private fun validateAnswers(answerIds: List<Long>) {
        val form = formSubject.value?.data ?: return
        formValidator.execute(answerIds, form)
            .let { newErrors ->
                formValidationStateProvider.update(
                    formValidationStateProvider.currentValue().filter {
                        it.id !in answerIds
                    } + newErrors)
                Timber.d("Answer validated")
            }
    }

    fun createFormResults() = FormResults(formContext, createFormAnswers())

    private fun createFormAnswers() = FormAnswers(formId, answersMap.values.toList())

    private val Resource<Form, Throwable>.data: Form?
        get() = (this as? ResourceSuccess<Form>)?.data

    private fun Resource<Form, Throwable>.toItemsResource(): Resource<List<FormItem>, Throwable> = when (this) {
        is ResourceSuccess -> ResourceSuccess(data.toItems())
        ResourceLoading -> ResourceLoading
        is ResourceError -> ResourceError(error)
    }

    private fun Form.applyAnswers(answers: FormAnswers): Form {
        answersMap.putAll(
            answers.answers
                .groupBy { it.questionId }
                .mapValues { it.value.first() }
        )
        return applyAnswers(answersMap)
    }

}



