package com.modern.android.forms.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FormResults(val formContext: FormContext, val formAnswers: FormAnswers): Parcelable