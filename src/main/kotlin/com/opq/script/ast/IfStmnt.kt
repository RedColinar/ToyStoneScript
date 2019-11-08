package com.opq.script.ast

import com.opq.script.interpreter.Environment
import com.opq.script.interpreter.FALSE

open class IfStmnt(c: List<ASTree>) : ASTList(c) {
    private fun condition(): ASTree {
        return child(0)
    }

    private fun thenBlock(): ASTree {
        return child(1)
    }

    private fun elseBlock(): ASTree? {
        return if (numChildren() > 2) child(2) else null
    }

    override fun toString(): String {
        return ("(if " + condition() + " " + thenBlock()
                + " else " + elseBlock() + ")")
    }

    override fun eval(env: Environment): Any? {
        val c = condition().eval(env)
        return if (c is Int && c.toInt() != FALSE) {
            thenBlock().eval(env)
        } else {
            val b = elseBlock()
            if (b == null) 0 else b.eval(env)
        }
    }
}