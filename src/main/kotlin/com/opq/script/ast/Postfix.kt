package com.opq.script.ast

import com.opq.script.interpreter.Environment

abstract class Postfix(children: List<ASTree>) : ASTList(children) {
    abstract fun eval(env: Environment, value: Any?): Any?
}