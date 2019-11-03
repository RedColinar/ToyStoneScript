package com.opq.script.ast

import com.opq.script.interpreter.Environment
import com.opq.script.interpreter.FALSE

open class WhileStmnt(c: List<ASTree>) : ASTList(c) {
    fun condition(): ASTree {
        return child(0)
    }

    fun body(): ASTree {
        return child(1)
    }

    override fun toString(): String {
        return "(while " + condition() + " " + body() + ")"
    }

    override fun eval(env: Environment): Any? {
        var result: Any? = 0
        while (true) {
            val c = condition().eval(env)
            if (c is Int && c.toInt() == FALSE)
                return result
            else
                result = body().eval(env)
        }
    }
}