package calculator

sealed class CalculatorException(message: String) : RuntimeException(message) {
    object InvalidExpression : CalculatorException("Invalid expression")
    object UnknownCommand : CalculatorException("Unknown command")
    object InvalidAssignment : CalculatorException("Invalid assignment")
    object InvalidIdentifier : CalculatorException("Invalid identifier")
    object UnknownVariable : CalculatorException("Unknown variable")
}