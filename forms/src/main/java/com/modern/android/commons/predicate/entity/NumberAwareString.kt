package com.modern.commons.predicate.entity

data class NumberAwareString(val value: String) : Comparable<NumberAwareString> {
    private val number = value.toBigDecimalOrNull()

    override fun compareTo(other: NumberAwareString): Int {
        return if (number != null && other.number != null) {
            number.compareTo(other.number)
        } else {
            value.compareTo(other.value)
        }
    }
}