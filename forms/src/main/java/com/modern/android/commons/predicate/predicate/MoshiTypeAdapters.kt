package com.modern.commons.predicate

import com.modern.commons.predicate.operator.Operator
import com.modern.commons.predicate.operator.OperatorType
import com.squareup.moshi.FromJson
import com.squareup.moshi.Moshi
import com.squareup.moshi.ToJson

class MoshiOperatorAdapter {
    @ToJson
    fun toJson(operator: Operator): String? {
        return OperatorType.findKey(operator)
    }

    @FromJson
    fun fromJson(operator: String): Operator? {
        return OperatorType.create(operator)
    }
}

fun Moshi.Builder.registerCriterion(): Moshi.Builder = add(MoshiOperatorAdapter())
