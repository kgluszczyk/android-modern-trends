package com.modern.commons.predicate.operator

import android.os.PatternMatcher
import com.modern.commons.predicate.entity.NumberAwareString
import timber.log.Timber

class Between : CompareOperator() {
    override val type: OperatorType = OperatorType.Between

    override fun evaluate(parameterValue: NumberAwareString, values: List<NumberAwareString>): Boolean {
        if (values.size != 2) {
            Timber.e("Expected two arguments for ${javaClass.name}")
            return false
        }

        val rangeStart = values[0]
        val rangeEnd = values[1]
        return parameterValue > rangeStart && parameterValue < rangeEnd
    }
}

/**
 * This operator verifies if the given parameter is equal to ANY value in criteria
 */
open class Eq : CompareOperator() {
    override val type: OperatorType = OperatorType.Eq

    override fun evaluate(parameterValue: NumberAwareString, values: List<NumberAwareString>): Boolean {
        return parameterValue in values
    }
}

/**
 * This operator verifies if given parameter is greater or equal than any of the values in the Swrve rule
 */
class Ge : CompareOperator() {
    override val type: OperatorType = OperatorType.Ge

    override fun evaluate(parameterValue: NumberAwareString, values: List<NumberAwareString>): Boolean {
        return values.any { value -> parameterValue >= value }
    }
}

/**
 * This operator verifies if given parameter is greater than any of the values in the Swrve rule
 */
class Gt : CompareOperator() {
    override val type: OperatorType = OperatorType.Gt

    override fun evaluate(parameterValue: NumberAwareString, values: List<NumberAwareString>): Boolean {
        return values.any { value -> parameterValue > value }
    }
}

class Le : CompareOperator() {
    override val type: OperatorType = OperatorType.Le

    override fun evaluate(parameterValue: NumberAwareString, values: List<NumberAwareString>): Boolean {
        return values.any { value -> parameterValue <= value }
    }
}

class Like : CompareOperator() {
    override val type: OperatorType = OperatorType.Like

    override fun evaluate(
        parameterValue: NumberAwareString,
        values: List<NumberAwareString>
    ): Boolean = values.any { value ->
        val m = PatternMatcher(value.value, PatternMatcher.PATTERN_SIMPLE_GLOB)
        m.match(parameterValue.value)
    }
}

class Lt : CompareOperator() {
    override val type: OperatorType = OperatorType.Lt

    override fun evaluate(parameterValue: NumberAwareString, values: List<NumberAwareString>): Boolean {
        return values.any { value -> parameterValue < value }
    }
}

/**
 * This operator verifies if the given parameter is not equal to all values in criteria
 */
class Neq : Eq() {
    override val type: OperatorType = OperatorType.Neq

    override fun evaluate(parameterValue: NumberAwareString, values: List<NumberAwareString>): Boolean {
        return !super.evaluate(parameterValue, values)
    }
}