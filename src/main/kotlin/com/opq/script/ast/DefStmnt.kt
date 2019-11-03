package com.opq.script.ast

import com.opq.script.interpreter.Environment
import com.opq.script.interpreter.NestedEnv

class DefStmnt(c: List<ASTree>) : ASTList(c) {
    fun name(): String {
        return (child(0) as ASTLeaf).token().getText()
    }

    fun parameters(): ParameterList {
        return child(1) as ParameterList
    }

    fun body(): BlockStmnt {
        return child(2) as BlockStmnt
    }

    override fun toString(): String {
        return "(def " + name() + " " + parameters() + " " + body() + ")"
    }

    override fun eval(env: Environment): Any {
        (env as NestedEnv).putNew(name(), Function(parameters(), body(), env))
        return name()
    }
}

class Function(
    protected var parameters: ParameterList,
    protected var body: BlockStmnt,
    protected var env: Environment
) {
    fun parameters(): ParameterList {
        return parameters
    }

    fun body(): BlockStmnt {
        return body
    }

    fun makeEnv(): Environment {
        return NestedEnv(env)
    }

    override fun toString(): String {
        return "<fun:" + hashCode() + ">"
    }
}
