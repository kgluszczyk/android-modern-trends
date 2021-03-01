package com.modern.android.forms.entity

import android.os.Parcelable
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDate

@Parcelize
data class FormContext(val god: String, val date: LocalDate) : Parcelable {

    @IgnoredOnParcel
    val id = "$god|$date"
}