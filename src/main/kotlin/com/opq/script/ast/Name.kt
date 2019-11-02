package com.opq.script.ast

import com.opq.script.Token

class Name(t: Token) : ASTLeaf(t) {
    fun name(): String {
        return token().getText()
    }
}