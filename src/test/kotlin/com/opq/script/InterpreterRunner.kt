package com.opq.script

import com.opq.script.ast.NullStmnt
import com.opq.script.interpreter.BasicEnv
import com.opq.script.interpreter.Environment
import com.opq.script.interpreter.NestedEnv
import com.opq.script.parser.BasicParser
import com.opq.script.parser.FuncParser
import org.junit.Test

class InterpreterTest {

    @Test
    fun basicInterpreterRunner() {
        /*
        sum = 0
        i = 1
        while i < 10 {
            sum = sum + i
            i = i + 1
        }
        sum
        */
        run(BasicParser(), BasicEnv())
    }

    @Test
    fun functionInterpreterRunner() {
        /*
        def fib (n) {
            if n < 2 {
                n
            } else {
                fib(n - 1) + fib(n - 2)
            }
        }
        fib(10)
        */
        run(FuncParser(), NestedEnv())
    }
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