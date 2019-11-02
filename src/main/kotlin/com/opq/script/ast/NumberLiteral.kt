package com.opq.script.ast

import com.opq.script.Token

class NumberLiteral(t: Token) : ASTLeaf(t) {
    fun value(): Int {
        return token().getNumber()
    }
}