package com.opq.script.ast

import com.opq.script.StoneException
import com.opq.script.interpreter.Environment

class Arguments(children: List<ASTree>) : Postfix(children) {

    fun size(): Int {
        return numChildren()
    }

    override fun eval(env: Environment, value: Any?): Any? {
        if (value is Function) {
            return evalFunction(env, value)
        } else if (value is NativeFunction) {
            return evalNativeFunction(env, value)
        }

        throw StoneException("bad function", this)
    }

    private fun evalFunction(env: Environment, value: Function): Any? {
        val params: ParameterList = value.parameters()
        if (size() != params.size()) throw StoneException("bad number of arguments", this)
        val newEnv = value.makeEnv()
        for ((num, a: ASTree) in children.withIndex()) {
            params.eval(newEnv, num, a.eval(env))
        }
        return value.body().eval(newEnv)
    }

    private fun evalNativeFunction(env: Environment, value: NativeFunction): Any? {
        val numOfParameters = value.numOfParameters()
        if (size() != numOfParameters) throw StoneException("bad number of arguments", this)
        val args: Array<Any?> = arrayOfNulls(numOfParameters)
        var num = 0
        for (a in children) {
            args[num++] = a.eval(env)
        }
        return value.invoke(this, *args)
    }
}