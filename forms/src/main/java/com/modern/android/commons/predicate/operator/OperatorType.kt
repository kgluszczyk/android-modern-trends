package com.modern.commons.predicate.operator

enum class OperatorType(val key: String, private val factory: () -> Operator) {
    Between("between", ::Between),
    Eq("eq", ::Eq),
    Ge("ge", ::Ge),
    Gt("gt", ::Gt),
    Le("le", ::Le),
    Like("like", ::Like),
    Lt("lt", ::Lt),
    Neq("neq", ::Neq),
    Add("add", ::Add),
    And("and", ::And),
    Divide("divide", ::Divide),
    Multiply("multiply", ::Multiply),
    Or("or", ::Or),
    Subtract("subtract", ::Subtract);

    companion object {
        fun create(name: String): Operator? = values()
            .find { it.key.equals(name, ignoreCase = true) }
            ?.factory
            ?.invoke()

        fun findKey(operator: Operator?) = operator?.type?.key
    }
}