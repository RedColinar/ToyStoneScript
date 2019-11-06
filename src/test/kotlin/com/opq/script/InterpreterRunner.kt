package com.opq.script

import com.opq.script.ast.ASTree
import com.opq.script.ast.NullStmnt
import com.opq.script.interpreter.BasicEnv
import com.opq.script.interpreter.Environment
import com.opq.script.interpreter.NestedEnv
import com.opq.script.parser.BasicParser
import com.opq.script.parser.ClosureParser
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

    @Test
    fun closureInterpreterRunner() {
        /*
        def counter (c) {
            fun () {
                c = c + 1
            }
        }
        c1 = counter(0)
        c2 = counter(0)
        c1()
        c1()
        c2()
        */
        run(ClosureParser(), NestedEnv())
    }

    @Test
    fun nativeInterpreterRunner() {
        /*
        def fib (n) {
            if n < 2 {
                n
            } else {
                fib(n - 1) + fib(n - 2)
            }
        }
        t = currentTime()
        fib 15
        print currentTime() - t + " msec"
        */
        run(ClosureParser(), Natives().environment(NestedEnv()))
    }
}

fun run(bp: BasicParser, env: Environment) {
    val lexer = Lexer(CodeDialog())
    while (lexer.peek(0) !== Token.EOF) {
        val t: ASTree = bp.parse(lexer)
        if (t !is NullStmnt) {
            t.eval(env)
        }
    }
}