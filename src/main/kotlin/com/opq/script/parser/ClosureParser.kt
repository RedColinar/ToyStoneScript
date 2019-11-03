package com.opq.script.parser

import com.opq.script.Parser.rule
import com.opq.script.ast.Fun

class ClosureParser : FuncParser() {
    init {
        primary.insertChoice(
            rule(Fun::class.java).sep("fun").ast(paramList).ast(block)
        )
    }
}