package com.modern.android.commons.predicate

import android.os.Parcel
import com.modern.commons.predicate.operator.Operator
import com.modern.commons.predicate.operator.OperatorType
import kotlinx.android.parcel.Parceler

object OperatorParceler : Parceler<Operator> {

    override fun create(parcel: Parcel): Operator {
        val operator = requireNotNull(parcel.readString())
        return requireNotNull(OperatorType.create(operator))
    }

    override fun Operator.write(parcel: Parcel, flags: Int) {
        parcel.writeString(OperatorType.findKey(this))
    }
}