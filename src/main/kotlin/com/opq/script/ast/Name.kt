package com.opq.script.ast

import com.opq.script.StoneException
import com.opq.script.Token
import com.opq.script.interpreter.Environment

open class Name(t: Token) : ASTLeaf(t) {
    fun name(): String {
        return token().getText()
    }

    override fun eval(env: Environment): Any? {
        if (!env.contains(name())) {
            throw StoneException("undefined name: " + name(), this)
        } else {
            return env[name()]
        }
    }
}