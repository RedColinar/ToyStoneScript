package com.opq.script.parser

import com.opq.script.Lexer
import com.opq.script.ParseException
import com.opq.script.Parser
import com.opq.script.Parser.Operators
import com.opq.script.Parser.rule
import com.opq.script.Token
import com.opq.script.ast.*
import java.util.*

open class BasicParser {
    var reserved = HashSet<String>()
    var operators = Operators()
    var expr0 = rule()
    var primary = rule(PrimaryExpr::class.java)
        .or(
            rule().sep("(").ast(expr0).sep(")"),
            rule().number(NumberLiteral::class.java),
            rule().identifier(Name::class.java, reserved),
            rule().string(StringLiteral::class.java)
        )
    var factor = rule().or(
        rule(NegativeExpr::class.java).sep("-").ast(primary),
        primary
    )
    var expr = expr0.expression(BinaryExpr::class.java, factor, operators)

    var statement0 = rule()
    var block = rule(BlockStmnt::class.java)
        .sep("{").option(statement0)
        .repeat(rule().sep(";", Token.EOL).option(statement0))
        .sep("}")
    var simple = rule(PrimaryExpr::class.java).ast(expr)
    var statement = statement0.or(
        rule(IfStmnt::class.java)
            .sep("if").ast(expr).ast(block)
            .option(rule().sep("else").ast(block)),
        rule(WhileStmnt::class.java)
            .sep("while").ast(expr).ast(block),
        simple
    )

    var program = rule().or(statement, rule(NullStmnt::class.java))
        .sep(";", Token.EOL)

    init {
        reserved.add(";")
        reserved.add("}")
        reserved.add(Token.EOL)

        operators.add("=", 1, Parser.Operators.RIGHT)
        operators.add("==", 2, Operators.LEFT)
        operators.add(">", 2, Operators.LEFT)
        operators.add("<", 2, Operators.LEFT)
        operators.add("+", 3, Operators.LEFT)
        operators.add("-", 3, Operators.LEFT)
        operators.add("*", 4, Operators.LEFT)
        operators.add("/", 4, Operators.LEFT)
        operators.add("%", 4, Operators.LEFT)
    }

    @Throws(ParseException::class)
    fun parse(lexer: Lexer): ASTree {
        return program.parse(lexer)
    }
}