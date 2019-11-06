package com.opq.script.ast

import com.opq.script.StoneException
import com.opq.script.interpreter.Environment
import com.opq.script.interpreter.NestedEnv
import java.lang.reflect.Method

class DefStmnt(c: List<ASTree>) : ASTList(c) {
    fun name(): String {
        return (child(0) as ASTLeaf).token().getText()
    }

    fun parameters(): ParameterList {
        return child(1) as ParameterList
    }

    fun body(): BlockStmnt {
        return child(2) as BlockStmnt
    }

    override fun toString(): String {
        return "(def " + name() + " " + parameters() + " " + body() + ")"
    }

    override fun eval(env: Environment): Any {
        (env as NestedEnv).putNew(name(), Function(name(), parameters(), body(), env))
        return name()
    }
}

class Function(
    val name: String,
    var parameters: ParameterList,
    var body: BlockStmnt,
    var env: Environment
) {
    fun parameters(): ParameterList {
        return parameters
    }

    fun body(): BlockStmnt {
        return body
    }

    fun makeEnv(): Environment {
        return NestedEnv(env)
    }

    override fun toString(): String {
        return "<fun:$name ${hashCode()}>"
    }
}

class NativeFunction(var name: String, var method: Method) {

    var numParams: Int = method.parameterTypes.size

    override fun toString(): String {
        return "<native:$name ${hashCode()}>"
    }

    fun numOfParameters(): Int {
        return numParams
    }

    operator fun invoke(tree: ASTree, vararg args: Any?): Any? {
        try {
            return method.invoke(null, *args)
        } catch (e: Exception) {
            throw StoneException("bad native function call: $name", tree)
        }
    }
}
