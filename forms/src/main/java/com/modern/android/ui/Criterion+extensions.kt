package com.modern.android.forms.ui

import com.modern.android.commons.predicate.Criterion
import com.modern.android.forms.entity.ItemAnswer
import com.modern.commons.predicate.Predicate

fun Criterion.calculate(answersMap: Map<Long, ItemAnswer>): String {
    return Predicate(answersMap.mapKeys { it.key.toString() }.mapValues { it.value.answer })
        .evaluate(this)
        .toString()
}