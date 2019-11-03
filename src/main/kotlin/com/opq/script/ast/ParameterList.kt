package com.opq.script.ast

import com.opq.script.interpreter.Environment
import com.opq.script.interpreter.NestedEnv

class ParameterList(c: List<ASTree>) : ASTList(c) {
    fun name(i: Int): String {
        return (child(i) as ASTLeaf).token().getText()
    }

    fun size(): Int {
        return numChildren()
    }

    fun eval(env: Environment, index: Int, value: Any?) {
        (env as NestedEnv).putNew(name(index), value)
    }
}