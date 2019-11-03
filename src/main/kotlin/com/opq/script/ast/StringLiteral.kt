package com.opq.script.ast

import com.opq.script.Token
import com.opq.script.interpreter.Environment

open class StringLiteral(t: Token) : ASTLeaf(t) {
    fun value(): String {
        return token().getText()
    }

    override fun eval(e: Environment): Any {
        return value()
    }
}
