package com.modern.android.forms.entity

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class FormAnswers(
    val formId: Long,
    val answers: List<ItemAnswer>
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class ItemAnswer(
    val questionId: Long,
    val answer: String,
    val type: AnswerType = AnswerType.STRING
) : Parcelable {

    enum class AnswerType { BOOLEAN, NUMBER, STRING }
}