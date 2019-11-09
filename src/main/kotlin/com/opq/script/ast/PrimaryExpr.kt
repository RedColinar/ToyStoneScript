package com.opq.script.ast

import com.opq.script.interpreter.Environment

class PrimaryExpr(c: List<ASTree>) : ASTList(c) {
    companion object {
        @JvmStatic
        fun create(c: List<ASTree>): ASTree {
            return if (c.size == 1) c[0] else PrimaryExpr(c)
        }
    }
    // 操作数
    fun operand(): ASTree {
        return child(0)
    }

    fun postfix(nest: Int): Postfix {
        return child(numChildren() - nest - 1) as Postfix
    }

    fun hasPostfix(nest: Int): Boolean {
        return numChildren() - nest > 1
    }

//    override fun eval(env: Environment): Any? {
//        var res : Any? = operand().eval(env)
//        val n = numChildren()
//        for (i in 1 until n) {
//            res = postfix(i).eval(env, res)
//        }
//        return res
//    }

    override fun eval(env: Environment): Any? {
        return evalSubExpr(env, 0)
    }

    fun evalSubExpr(env: Environment, nest: Int): Any? {
        if (hasPostfix(nest)) {
            val target = evalSubExpr(env, nest + 1)
            return postfix(nest).eval(env, target)
        } else {
            return operand().eval(env)
        }
    }
}