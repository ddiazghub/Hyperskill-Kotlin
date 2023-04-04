package calculator

sealed class Statement {
    class Expression(private val tokens: List<Token.ExpressionToken>) : Statement() {
        fun eval(variables: Map<String, Token.Number>): Token.Number {
            val stack = mutableListOf<Token.Number>()

            for (token in this.tokens) {
                when (token) {
                    is Token.Number -> stack.add(token)
                    is Token.Variable -> stack.add(token.sign * (variables[token.name] ?: throw CalculatorException.UnknownVariable))
                    is Token.Operator -> if (stack.size > 1) {
                        (stack.removeLast() to stack.removeLast()).let {
                            stack.add(token.apply(it.second, it.first))
                        }
                    } else throw CalculatorException.InvalidExpression
                    else -> throw CalculatorException.InvalidExpression
                }
            }

            return stack.last()
        }

        companion object {
            fun parse(tokens: List<Token>): Expression {
                val expression = reduce(tokens)
                val stack = mutableListOf<Token.ExpressionToken>()
                val result = mutableListOf<Token.ExpressionToken>()

                for (token in expression) {
                    when (token) {
                        is Token.Operand -> result.add(token)
                        is Token.OpenParenth -> stack.add(token)
                        is Token.Operator -> {
                            while ((stack.lastOrNull() ?: Token.OpenParenth).let { it is Token.Operator && token.priority <= it.priority })
                                result.add(stack.removeLast())

                            stack.add(token)
                        }
                        is Token.CloseParenth -> {
                            while ((stack.lastOrNull() ?: throw CalculatorException.InvalidExpression) !is Token.OpenParenth)
                                result.add(stack.removeLast())

                            stack.removeLast()
                        }
                        is Token.Assign -> throw CalculatorException.InvalidAssignment
                        else -> throw CalculatorException.InvalidExpression
                    }
                }

                while (stack.isNotEmpty()) {
                    when (val top = stack.removeLast()) {
                        is Token.OpenParenth -> throw CalculatorException.InvalidExpression
                        else -> result.add(top)
                    }
                }

                return Expression(result)
            }

            private fun reduce(tokens: List<Token>): List<Token> {
                val result = mutableListOf<Token>()

                for (token in tokens) {
                    val last = result.lastOrNull()

                    when (token) {
                        is Token.Add -> if (last is Token.Operand) result.add(token)
                        is Token.Sub -> when (last) {
                            is Token.AddSubOperator -> result.removeLast().let { result.add(last.inv()) }
                            else -> result.add(token)
                        }
                        is Token.Operand -> when (last to result.elementAtOrNull(result.size - 2)) {
                            Token.Sub to null, Token.Sub to Token.OpenParenth -> result.removeLast().also {
                                result.add(token.inv())
                            }
                            else -> result.add(token)
                        }
                        else -> result.add(token)
                    }
                }

                return result
            }
        }
    }

    class Assignment(val variable: String, val expression: Expression) : Statement() {
        companion object {
            fun parse(tokens: List<Token>): Assignment? {
                return when (tokens.indexOf(Token.Assign)) {
                    -1 -> null
                    1 -> tokens[0].let {
                        if (it is Token.Variable)
                            Assignment(it.name, Expression.parse(tokens.subList(2, tokens.size)))
                        else throw CalculatorException.InvalidIdentifier
                    }
                    else -> throw CalculatorException.InvalidAssignment
                }
            }
        }
    }

    sealed class Command : Statement() {
        companion object {
            fun parse(token: Token): Command = when (token) {
                Token.Exit -> Exit
                Token.Help -> Help
                else -> throw CalculatorException.UnknownCommand
            }
        }
    }

    object Exit : Command()
    object Help : Command()

    companion object {
        private val TOKEN_REGEX = Regex("\\d+|[a-zA-Z]+|\\++|\\S")

        private fun lex(statement: String): List<Token> {
            return TOKEN_REGEX
                .findAll(statement)
                .map { Token.parse(it.groupValues[0]) }
                .toList()
        }

        fun parse(string: String): Statement = lex(string).let {
            if (it.size == 2 && it[0] === Token.Div) Command.parse(it[1])
            else Assignment.parse(it) ?: Expression.parse(it)
        }
    }
}