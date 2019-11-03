package com.opq.script.ast

import com.opq.script.StoneException
import com.opq.script.interpreter.Environment

class Arguments(children: List<ASTree>) : Postfix(children) {

    fun size(): Int {
        return numChildren()
    }

    override fun eval(env: Environment, value: Any?): Any? {
        if (value !is Function) throw StoneException("bad function", this)
        val params: ParameterList = value.parameters()
        if (size() != params.size()) throw StoneException("bad number of arguments", this)
        val newEnv = value.makeEnv()
        for ((num, a: ASTree) in children.withIndex()) {
            params.eval(newEnv, num, a.eval(env))
        }
        return value.body().eval(newEnv)
    }
}