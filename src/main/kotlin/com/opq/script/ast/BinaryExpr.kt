package com.opq.script.ast

class BinaryExpr(c: List<ASTree>) : ASTList(c) {
    fun left(): ASTree {
        return child(0)
    }

    fun operator(): String {
        return (child(1) as ASTLeaf).token().getText()
    }

    fun right(): ASTree {
        return child(2)
    }
}
