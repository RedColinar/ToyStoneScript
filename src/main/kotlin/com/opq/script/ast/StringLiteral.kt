package com.opq.script.ast

import com.opq.script.Token

class StringLiteral(t: Token) : ASTLeaf(t) {
    fun value(): String {
        return token().getText()
    }
}
