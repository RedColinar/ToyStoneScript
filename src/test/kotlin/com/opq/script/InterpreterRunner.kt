package com.opq.script

import com.opq.script.ast.NullStmnt
import com.opq.script.interpreter.BasicEnv
import com.opq.script.interpreter.Environment

/*
sum = 0
i = 1
while i < 10 {
    sum = sum + i
    i = i + 1
}
sum
*/
fun main() {
    run(BasicParser(), BasicEnv())
}

fun run(bp: BasicParser, env: Environment) {
    val lexer = Lexer(CodeDialog())
    while (lexer.peek(0) !== Token.EOF) {
        val t = bp.parse(lexer)
        if (t !is NullStmnt) {
            val r = t.eval(env)
            println("=> $r")
        }
    }
}