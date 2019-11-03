package com.opq.script.ast

import com.opq.script.StoneException
import com.opq.script.Token
import com.opq.script.interpreter.Environment
import java.util.ArrayList


open class ASTLeaf(protected val token: Token) : ASTree() {
    override fun child(i: Int): ASTree {
        throw IndexOutOfBoundsException()
    }

    override fun numChildren(): Int {
        return 0
    }

    override fun children(): Iterator<ASTree> {
        return empty.iterator()
    }

    override fun toString(): String {
        return token.getText()
    }

    override fun location(): String {
        return "at line " + token.lineNumber
    }

    fun token(): Token {
        return token
    }

    override fun eval(env: Environment): Any {
        throw StoneException("cannot eval: " + toString(), this)
    }

    companion object {
        private val empty = ArrayList<ASTree>()
    }
}
