package com.opq.script

import com.opq.script.ast.ASTree
import com.opq.script.ast.NullStmnt
import com.opq.script.interpreter.BasicEnv
import com.opq.script.interpreter.Environment
import com.opq.script.interpreter.NestedEnv
import com.opq.script.parser.BasicParser
import com.opq.script.parser.ClosureParser
import com.opq.script.parser.FuncParser
import org.junit.Assert
import org.junit.Test
import java.io.StringReader

class InterpreterTest {
    lateinit var code: String

    @Test
    fun basicInterpreterRunner() {
        code = """
            sum = 0
            i = 1
            while i < 10 {
                sum = sum + i
                i = i + 1
            }
            sum
        """.trimIndent()
        run(BasicParser(), BasicEnv(), code)
    }

    @Test
    fun functionInterpreterRunner() {
        code = """
            def fib (n) {
                if n < 2 {
                    n
                } else {
                    fib(n - 1) + fib(n - 2)
                }
            }
            fib(10)
        """.trimIndent()
        run(FuncParser(), NestedEnv(), code)
    }

    @Test
    fun closureInterpreterRunner() {
        code = """
            inc = fun (x) { x + 1 }
            inc(2)
        """.trimIndent()
        val res = run(ClosureParser(), NestedEnv(), code)
        Assert.assertEquals(res, 3)
    }

    @Test
    fun nativeInterpreterRunner() {
        code = """
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
        """.trimIndent()
        run(ClosureParser(), Natives().environment(NestedEnv()), code)
    }
}

fun run(bp: BasicParser, env: Environment, code: String): Any? {
    val lexer = Lexer(StringReader(code))
    var res : Any? = null
    while (lexer.peek(0) !== Token.EOF) {
        val t: ASTree = bp.parse(lexer)
        if (t !is NullStmnt) {
            res = t.eval(env)
        }
    }
    return res
}