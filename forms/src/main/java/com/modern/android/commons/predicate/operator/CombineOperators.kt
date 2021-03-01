package com.modern.commons.predicate.operator

import java.math.BigDecimal

class Add : CombineOperator() {
    override val type: OperatorType = OperatorType.Add

    override fun combine(acc: BigDecimal, value: BigDecimal): BigDecimal {
        return acc.add(value)
    }

    companion object {
        const val NAME = "add"
    }
}

class And : CombineOperator() {
    override val type: OperatorType = OperatorType.And

    override fun combine(acc: BigDecimal, value: BigDecimal): BigDecimal {
        return acc.toBigInteger().and(value.toBigInteger()).toBigDecimal()
    }

    companion object {
        const val NAME = "and"
    }
}

class Divide : CombineOperator() {
    override val type: OperatorType = OperatorType.Divide

    override fun combine(acc: BigDecimal, value: BigDecimal): BigDecimal {
        return acc.divide(value)
    }

    companion object {
        const val NAME = "divide"
    }
}

class Multiply : CombineOperator() {
    override val type: OperatorType = OperatorType.Multiply

    override fun combine(acc: BigDecimal, value: BigDecimal): BigDecimal {
        return acc.multiply(value)
    }

    companion object {
        const val NAME = "multiply"
    }
}

class Or : CombineOperator() {
    override val type: OperatorType = OperatorType.Or

    override fun combine(acc: BigDecimal, value: BigDecimal): BigDecimal {
        return acc.toBigInteger().or(value.toBigInteger()).toBigDecimal()
    }

    companion object {
        const val NAME = "or"
    }
}

class Subtract : CombineOperator() {
    override val type: OperatorType = OperatorType.Subtract

    override fun combine(acc: BigDecimal, value: BigDecimal): BigDecimal {
        return acc.subtract(value)
    }

    companion object {
        const val NAME = "subtract"
    }
}