package com.opq.script.interpreter

const val TRUE = 1
const val FALSE = 0

interface Evaluator {
    fun eval(env: Environment): Any?
}