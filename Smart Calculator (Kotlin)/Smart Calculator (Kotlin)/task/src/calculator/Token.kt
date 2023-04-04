package calculator

import java.math.BigInteger

sealed class Token {
    object Assign : Token()
    object Exit : Token()
    object Help : Token()

    sealed class ExpressionToken : Token()
    object OpenParenth : ExpressionToken()
    object CloseParenth : ExpressionToken()

    sealed class Operand : ExpressionToken() {
        abstract fun inv(): Operand
    }

    class Variable(val name: String, val sign: Number = Number(BigInteger.ONE)) : Operand() {
        override fun inv(): Variable = Variable(name, -sign)
    }

    class Number(val value: BigInteger) : Operand() {
        operator fun unaryMinus(): Number = Number(-this.value)
        operator fun plus(other: Number): Number = Number(this.value + other.value)
        operator fun minus(other: Number): Number = Number(this.value - other.value)
        operator fun times(other: Number): Number = Number(this.value * other.value)
        operator fun div(other: Number): Number = Number(this.value / other.value)
        fun pow(other: Number): Number = Number(this.value.pow(other.value.toInt()))
        override fun inv(): Number = Number(-this.value)
        override fun toString(): String = value.toString()
    }

    sealed class Operator(val priority: Int, val operation: (Number, Number) -> Number) : ExpressionToken() {
        fun apply(number1: Number, number2: Number): Number = operation(number1, number2)
    }

    sealed class AddSubOperator(priority: Int, operation: (Number, Number) -> Number) : Operator(priority, operation) {
        abstract fun inv(): AddSubOperator
    }

    object Mul : Operator(1, Number::times)
    object Div : Operator(1, Number::div)
    object Pow : Operator(2, Number::pow)

    object Add : AddSubOperator(0, Number::plus) {
        override fun inv(): Sub = Sub
    }

    object Sub : AddSubOperator(0, Number::minus) {
        override fun inv(): Add = Add
    }

    companion object {
        fun parse(string: String): Token = when (string.last()) {
            in 'a'..'z', in 'A'..'Z' -> when (string) {
                "exit" -> Exit
                "help" -> Help
                else -> Variable(string)
            }
            in '0'..'9' -> Number(string.toBigInteger())
            '+' -> Add
            '-' -> Sub
            '*' -> Mul
            '/' -> Div
            '^' -> Pow
            '(' -> OpenParenth
            ')' -> CloseParenth
            '=' -> Assign
            else -> throw CalculatorException.InvalidExpression
        }
    }
}