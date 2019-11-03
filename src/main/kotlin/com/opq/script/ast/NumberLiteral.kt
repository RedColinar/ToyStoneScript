package com.opq.script.ast

import com.opq.script.Token
import com.opq.script.interpreter.Environment

open class NumberLiteral(t: Token) : ASTLeaf(t) {
    fun value(): Int {
        return token().getNumber()
    }

    override fun eval(env: Environment): Any {
        return value()
    }
}