package com.modern.android.forms.ui.send

import androidx.lifecycle.ViewModel
import com.modern.android.forms.domain.SendAnswers
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SendAnswersViewModel constructor(private val sendForm: SendAnswers) : ViewModel() {

    fun sendAnswers(formResults: com.modern.android.forms.entity.FormResults): Completable = Completable.fromCallable {
        sendForm.execute(
            formResults
        )
    }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}