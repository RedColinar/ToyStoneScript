package com.opq.script.parser

import com.opq.script.Lexer
import com.opq.script.ast.ASTLeaf
import com.opq.script.ast.ASTree
import java.util.*

fun rule(): Parser {
    return Parser(null as Class<out ASTree>?)
}

fun rule(clazz: Class<out ASTree>): Parser {
    return Parser(clazz)
}

open class Parser {
    private var elements: MutableList<Element> = ArrayList()
    private lateinit var factory: Factory

    constructor(clazz: Class<out ASTree>?) {
        reset(clazz)
    }

    constructor(p: Parser) {
        elements = p.elements
        factory = p.factory
    }

    private fun reset(): Parser {
        elements = ArrayList()
        return this
    }

    private fun reset(clazz: Class<out ASTree>?): Parser {
        elements = ArrayList()
        factory = createFactoryForASTList(clazz)
        return this
    }

    fun parse(lexer: Lexer): ASTree {
        val results = ArrayList<ASTree>()
        for (e in elements) {
            e.parse(lexer, results)
        }

        return factory.make(results)
    }

    fun match(lexer: Lexer): Boolean {
        return if (elements.size == 0) {
            true
        } else {
            // 预读第一个规则
            val e = elements[0]
            e.match(lexer)
        }
    }

    fun number(clazz: Class<out ASTLeaf>?): Parser {
        elements.add(NumToken(clazz))
        return this
    }

    fun identifier(reserved: HashSet<String>): Parser {
        return identifier(null, reserved)
    }

    fun identifier(clazz: Class<out ASTLeaf>?, reserved: HashSet<String>): Parser {
        elements.add(IdToken(clazz, reserved))
        return this
    }

    fun string(): Parser {
        return string(null)
    }

    fun string(clazz: Class<out ASTLeaf>?): Parser {
        elements.add(StrToken(clazz))
        return this
    }

    fun token(vararg pat: String): Parser {
        elements.add(Leaf(arrayOf(*pat)))
        return this
    }

    fun sep(vararg pat: String): Parser {
        elements.add(Skip(arrayOf(*pat)))
        return this
    }

    fun ast(p: Parser): Parser {
        elements.add(Tree(p))
        return this
    }

    fun or(vararg p: Parser): Parser {
        elements.add(OrTree(arrayOf(*p)))
        return this
    }

    fun maybe(p: Parser): Parser {
        val p2 = Parser(p)
        p2.reset()
        elements.add(OrTree(arrayOf(p, p2)))
        return this
    }

    fun option(p: Parser): Parser {
        elements.add(Repeat(p, true))
        return this
    }

    fun repeat(p: Parser): Parser {
        elements.add(Repeat(p, false))
        return this
    }

    fun expression(subexp: Parser, operators: Operators): Parser {
        elements.add(Expr(null, subexp, operators))
        return this
    }

    fun expression(
        clazz: Class<out ASTree>, subexp: Parser,
        operators: Operators
    ): Parser {
        elements.add(Expr(clazz, subexp, operators))
        return this
    }

    fun insertChoice(p: Parser): Parser {
        val e = elements[0]
        if (e is OrTree)
            e.insert(p)
        else {
            val otherwise = Parser(this)
            reset(null)
            or(p, otherwise)
        }
        return this
    }
}