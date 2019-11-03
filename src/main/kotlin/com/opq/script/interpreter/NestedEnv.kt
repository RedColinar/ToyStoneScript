package com.opq.script.interpreter

import java.util.*

class NestedEnv : Environment {

    protected var values: HashMap<String, Any?> = HashMap()
    protected var outer: Environment? = null

    constructor() : this(null)

    constructor(e: Environment?) {
        outer = e
    }

    override fun get(name: String): Any? {
        val v = values[name]
        return if (v == null && outer != null)
            outer!![name]
        else
            v
    }

    fun putNew(name: String, value: Any?) {
        values[name] = value
    }

    override fun put(name: String, value: Any?) {
        var e = where(name)
        if (e == null) e = this
        (e as NestedEnv).putNew(name, value)
    }

    fun where(name: String): Environment? {
        return if (values.containsKey(name))
            this
        else if (outer == null)
            null
        else
            (outer as NestedEnv).where(name)
    }

    override fun contains(name: String): Boolean {
        return values.contains(name) || outer?.contains(name) ?: false
    }
}