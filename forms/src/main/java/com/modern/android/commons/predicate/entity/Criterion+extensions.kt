package com.modern.commons.predicate.entity

import com.modern.android.commons.predicate.Criterion

fun Criterion.resolvedValues(parameters: Map<String, String>): List<String> = values.map {
    if (it.startsWith("$")) {
        parameters[it.substring(1)] ?: it
    } else {
        it
    }
}