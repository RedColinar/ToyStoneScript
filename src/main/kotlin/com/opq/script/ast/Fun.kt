package com.opq.script.ast

import com.opq.script.interpreter.Environment

class Fun(c: List<ASTree>) : ASTList(c) {
    fun parameters(): ParameterList {
        return child(0) as ParameterList
    }

    fun body(): BlockStmnt {
        return child(1) as BlockStmnt
    }

    override fun toString(): String {
        return "(fun " + parameters() + " " + body() + ")"
    }

    override fun eval(env: Environment): Any {
        return Function(parameters(), body(), env)
    }
}
