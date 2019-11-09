package com.opq.script.parser

import com.opq.script.ast.ClosureFun

class ClosureParser : FuncParser() {
    init {
        primary.insertChoice(
            rule(ClosureFun::class.java).sep("fun").ast(paramList).ast(block)
        )
    }
}