package com.opq.script.ast

class NegativeExpr(c: List<ASTree>) : ASTList(c) {
    fun operand(): ASTree {
        return child(0)
    }

    override fun toString(): String {
        return "-" + operand()
    }
}
