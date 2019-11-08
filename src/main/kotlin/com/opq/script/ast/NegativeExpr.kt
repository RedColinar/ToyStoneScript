package com.opq.script.ast

import com.opq.script.StoneException
import com.opq.script.interpreter.Environment

open class NegativeExpr(c: List<ASTree>) : ASTList(c) {
    private fun operand(): ASTree {
        return child(0)
    }

    override fun toString(): String {
        return "-" + operand()
    }

    override fun eval(env: Environment): Any {
        val v = operand().eval(env)
        return if (v is Int)
            -v.toInt()
        else
            throw StoneException("bad type for -", this)
    }
}
