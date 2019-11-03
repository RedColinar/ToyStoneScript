package com.opq.script.interpreter

import java.util.*

class BasicEnv : Environment {
    protected var values: HashMap<String, Any?> = HashMap()

    override fun put(name: String, value: Any?) {
        values[name] = value
    }

    override fun get(name: String): Any? {
        return values[name]
    }
}