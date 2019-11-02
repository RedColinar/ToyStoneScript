package com.opq.script.ast

class IfStmnt(c: List<ASTree>) : ASTList(c) {
    fun condition(): ASTree {
        return child(0)
    }

    fun thenBlock(): ASTree {
        return child(1)
    }

    fun elseBlock(): ASTree? {
        return if (numChildren() > 2) child(2) else null
    }

    override fun toString(): String {
        return ("(if " + condition() + " " + thenBlock()
                + " else " + elseBlock() + ")")
    }
}