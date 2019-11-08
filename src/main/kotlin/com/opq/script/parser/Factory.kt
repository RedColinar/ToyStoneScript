package com.opq.script.parser

import com.opq.script.ast.ASTList
import com.opq.script.ast.ASTree

const val factoryName = "create"

abstract class Factory {

    abstract fun make0(arg: Any): ASTree

    fun make(arg: Any): ASTree {
        try {
            return make0(arg)
        } catch (e1: IllegalArgumentException) {
            throw e1
        } catch (e2: Exception) {
            throw RuntimeException(e2) // this compiler is broken.
        }
    }
}

fun createFactoryForASTList(clazz: Class<out ASTree>?): Factory {
    var f: Factory? = createFactory(clazz, List::class.java)
    if (f == null) {
        f = object : Factory() {
            override fun make0(arg: Any): ASTree {
                val results = arg as List<ASTree>
                return if (results.size == 1)
                    results[0]
                else
                    ASTList(results)
            }
        }
    }
    return f
}

fun createFactory(clazz: Class<out ASTree>?, argType: Class<*>): Factory? {
    if (clazz == null) return null

    try {
        val m = clazz.getMethod(factoryName, argType)
        return object : Factory() {
            override fun make0(arg: Any): ASTree {
                return m.invoke(null, arg) as ASTree
            }
        }
    } catch (e: NoSuchMethodException) {
    }

    try {
        val c = clazz.getConstructor(argType)
        return object : Factory() {
            override fun make0(arg: Any): ASTree {
                return c.newInstance(arg)
            }
        }
    } catch (e: NoSuchMethodException) {
        throw RuntimeException(e)
    }
}