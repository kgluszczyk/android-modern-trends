package com.modern.commons.predicate

import com.modern.android.commons.predicate.Criterion
import timber.log.Timber
import java.math.BigDecimal

class Predicate(private val parameters: Map<String, String>) {
    fun evaluate(criterion: Criterion): BigDecimal {
        return runCatching {
            val operator = criterion.operator
            operator.execute(this, criterion, parameters)
        }.onFailure { Timber.e(it, "Failed to calculate predicate") }
            .getOrDefault(BigDecimal.ZERO)
    }

    fun evaluateBoolean(criterion: Criterion): Boolean = evaluate(criterion).toInt() != 0
}

