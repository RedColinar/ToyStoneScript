package com.opq.script.interpreter

interface Environment {
    fun put(name: String, value: Any?)
    operator fun get(name: String): Any?
    fun contains(name: String): Boolean
}