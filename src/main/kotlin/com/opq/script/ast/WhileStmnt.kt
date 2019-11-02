package com.opq.script.ast

class WhileStmnt(c: List<ASTree>) : ASTList(c) {
    fun condition(): ASTree {
        return child(0)
    }

    fun body(): ASTree {
        return child(1)
    }

    override fun toString(): String {
        return "(while " + condition() + " " + body() + ")"
    }
}