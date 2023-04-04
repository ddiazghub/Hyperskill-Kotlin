package calculator

object Calculator {
    private val variables: MutableMap<String, Token.Number> = mutableMapOf()

    fun start() {
        for (line in readlns()) {
            try {
                when (val statement = Statement.parse(line)) {
                    is Statement.Exit -> return println("Bye!")
                    is Statement.Help -> println("The program calculates the sum of numbers")
                    is Statement.Expression -> println(statement.eval(variables))
                    is Statement.Assignment -> try {
                        variables[statement.variable] = statement.expression.eval(variables)
                    } catch (e: CalculatorException) {
                        throw CalculatorException.InvalidAssignment
                    }
                }
            } catch (e: CalculatorException) {
                println(e.message)
            }
        }
    }
}