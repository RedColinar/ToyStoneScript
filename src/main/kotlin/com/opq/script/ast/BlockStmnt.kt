package com.opq.script.ast

import com.opq.script.interpreter.Environment

open class BlockStmnt(c: List<ASTree>) : ASTList(c) {
    override fun eval(env: Environment): Any {
        var result: Any = 0
        for (t in this) {
            if (t !is NullStmnt)
                result = t.eval(env)
        }
        return result
    }
}
