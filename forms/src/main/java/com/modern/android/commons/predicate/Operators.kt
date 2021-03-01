package com.modern.commons.predicate.operator

import com.modern.android.commons.predicate.Criterion
import com.modern.commons.predicate.Predicate
import com.modern.commons.predicate.entity.NumberAwareString
import com.modern.commons.predicate.entity.resolvedValues
import timber.log.Timber
import java.math.BigDecimal

interface Operator {

    val type: OperatorType

    fun execute(
        predicate: Predicate,
        criterion: Criterion,
        parameters: Map<String, String>
    ): BigDecimal
}

abstract class CompareOperator : Operator {

    override fun execute(predicate: Predicate, criterion: Criterion, parameters: Map<String, String>): BigDecimal {
        if (criterion.values.size < 2) {
            Timber.e("At least 2 values are required for ${this.javaClass.name}")
            return BigDecimal.ZERO
        }

        val resolvedValues = criterion.resolvedValues(parameters)
            .map { NumberAwareString(it) }
        return evaluate(resolvedValues.first(), resolvedValues.drop(1)).toBigDecimal()
    }

    protected abstract fun evaluate(parameterValue: NumberAwareString, values: List<NumberAwareString>): Boolean

    private fun Boolean.toBigDecimal() = if (this) BigDecimal.ONE else BigDecimal.ZERO
}

abstract class CombineOperator : Operator {

    override fun execute(predicate: Predicate, criterion: Criterion, parameters: Map<String, String>): BigDecimal {
        val criteria = criterion.criteria.map { predicate.evaluate(it) }
        val values = criterion.resolvedValues(parameters)
            .map { it.toBigDecimalOrNull() ?: BigDecimal.ZERO }
        return (values + criteria).combine(this::combine)
    }

    protected abstract fun combine(acc: BigDecimal, value: BigDecimal): BigDecimal

    private fun List<BigDecimal>.combine(operation: (acc: BigDecimal, value: BigDecimal) -> BigDecimal): BigDecimal =
        fold(null as BigDecimal?) { acc, number ->
            acc?.let { operation(acc, number) } ?: number
        } ?: BigDecimal.ZERO
}