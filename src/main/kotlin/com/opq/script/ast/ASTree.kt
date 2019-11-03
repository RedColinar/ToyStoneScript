package com.opq.script.ast

import com.opq.script.interpreter.Evaluator

abstract class ASTree : Iterable<ASTree>, Evaluator {
    abstract fun child(i: Int): ASTree
    abstract fun numChildren(): Int
    abstract fun children(): Iterator<ASTree>
    abstract fun location(): String?
    override fun iterator(): Iterator<ASTree> {
        return children()
    }
}
