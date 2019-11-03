package com.opq.script.ast

import com.opq.script.StoneException
import com.opq.script.interpreter.Environment
import com.opq.script.interpreter.FALSE
import com.opq.script.interpreter.TRUE

open class BinaryExpr(c: List<ASTree>) : ASTList(c) {
    fun left(): ASTree {
        return child(0)
    }

    fun operator(): String {
        return (child(1) as ASTLeaf).token().getText()
    }

    fun right(): ASTree {
        return child(2)
    }

    override fun eval(env: Environment): Any {
        val op = operator()
        if ("=" == op) {
            val right = right().eval(env)
            return computeAssign(env, right)
        } else {
            val left = left().eval(env)
            val right = right().eval(env)
            return computeOp(left, op, right)
        }
    }

    private fun computeAssign(env: Environment, rvalue: Any): Any {
        val l = left()
        if (l is Name) {
            env.put(l.name(), rvalue)
            return rvalue
        } else
            throw StoneException("bad assignment", this)
    }

    private fun computeOp(left: Any?, op: String, right: Any?): Any {
        return if (left is Int && right is Int) {
            computeNumber(left as Int?, op, right as Int?)
        } else if (op == "+")
            left.toString() + right.toString()
        else if (op == "==") {
            if (left == null)
                if (right == null) TRUE else FALSE
            else
                if (left == right) TRUE else FALSE
        } else
            throw StoneException("bad type", this)
    }

    private fun computeNumber(left: Int?, op: String, right: Int?): Any {
        val a = left!!.toInt()
        val b = right!!.toInt()
        return if (op == "+")
            a + b
        else if (op == "-")
            a - b
        else if (op == "*")
            a * b
        else if (op == "/")
            a / b
        else if (op == "%")
            a % b
        else if (op == "==")
            if (a == b) TRUE else FALSE
        else if (op == ">")
            if (a > b) TRUE else FALSE
        else if (op == "<")
            if (a < b) TRUE else FALSE
        else
            throw StoneException("bad operator", this)
    }
}
