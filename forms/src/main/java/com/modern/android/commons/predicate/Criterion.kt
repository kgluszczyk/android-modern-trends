package com.modern.android.commons.predicate

import android.os.Parcelable
import com.modern.commons.predicate.operator.Operator
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.TypeParceler

@Parcelize
@JsonClass(generateAdapter = true)
data class Criterion @JvmOverloads constructor(
    @Json(name = "operator") @TypeParceler<Operator, OperatorParceler>() val operator: Operator,
    @Json(name = "criteria") val criteria: List<Criterion> = emptyList(),
    @Json(name = "values") val values: List<String> = emptyList()
) : Parcelable