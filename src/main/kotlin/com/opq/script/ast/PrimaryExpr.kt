package com.opq.script.ast

class PrimaryExpr(c: List<ASTree>) : ASTList(c) {
    companion object {
        @JvmStatic
        fun create(c: List<ASTree>): ASTree {
            return if (c.size == 1) c[0] else PrimaryExpr(c)
        }
    }
}