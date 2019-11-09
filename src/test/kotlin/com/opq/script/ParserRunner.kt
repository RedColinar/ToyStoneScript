package com.opq.script

import com.opq.script.parser.FuncParser
import com.opq.script.parser.Parser
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.StringReader

class ParserTest {

    private lateinit var p: FuncParser
    private var code = ""

    @Before
    fun setup() {
        p = FuncParser()
    }

    private fun check(code: String, parser: Parser, result: String) {
        val ast = parser.parse(Lexer(StringReader(code)))
        Assert.assertEquals(ast.toString(), result)
        println(ast)
    }

    @Test
    fun testPrimaryParser() {
        code = """
            ((1))
        """.trimIndent()
        check(code, p.primary, "1")

        code = """
            (("hi"))
        """.trimIndent()
        check(code, p.primary, "hi")

        code = """
            ((num))
        """.trimIndent()
        check(code, p.primary, "num")
    }

    @Test
    fun testFactorParser() {
        code = """
            ((-1))
        """.trimIndent()
        check(code, p.factor, "-1")

        code = """
            ((1 + 1))
        """.trimIndent()
        check(code, p.factor, "(1 + 1)")
    }

    @Test
    fun testExpressionParser() {
        code = """
            (((1+2)))
        """.trimIndent()
        check(code, p.expr, "(1 + 2)")

        code = """
            (((1-2)))
        """.trimIndent()
        check(code, p.expr, "(1 - 2)")

        code = """
            (((1-2) * (3 + 4)))
        """.trimIndent()
        check(code, p.expr, "((1 - 2) * (3 + 4))")

        code = """
            ((1 - 2 * 3 + 4 / 5)))
        """.trimIndent()
        check(code, p.expr, "((1 - (2 * 3)) + (4 / 5))")


        code = """
            num = 0
        """.trimIndent()
        check(code, p.expr, "(num = 0)")
    }

    @Test
    fun testBlockParser() {
        code = """
            {
                1
            }
        """.trimIndent()
        check(code, p.block, "(1)")
        code = """
            {
                (((1+2)))
            }
        """.trimIndent()
        check(code, p.block, "((1 + 2))")

        code = """
            {
            ;
            ;
            ;;;
            }
        """.trimIndent()
        check(code, p.block, "()")

        code = """
            {
                if 1 < 2 {
                    1
                }
            }
        """.trimIndent()
        check(code, p.block, "((if (1 < 2) (1) else null))")

        code = """
            {
                if 1 < 2 {
                    1
                } else {
                    2
                }
            }
        """.trimIndent()
        check(code, p.block, "((if (1 < 2) (1) else (2)))")
    }

    @Test
    fun testStatementParser() {
        code = """
            if 1 < 2 {
                1
            } else {
                2
            }
        """.trimIndent()
        check(code, p.statement, "(if (1 < 2) (1) else (2))")

        code = """
            while 1 < 2 {
                3
            }
        """.trimIndent()
        check(code, p.statement, "(while (1 < 2) (3))")
    }

    @Test
    fun testProgramParser() {
        code = """
        while i < 10 {
            if i % 2 == 0 {
                even = even + i
            } else {
                odd = odd + i
            }
            i = i + 1
        }
        ;;;
        
        ;;;
        """.trimIndent()
        check(
            code,
            p.program,
            "(while (i < 10) ((if ((i % 2) == 0) ((even = (even + i))) else ((odd = (odd + i)))) (i = (i + 1))))"
        )
    }

    @Test
    fun testParamsParser() {
        code = """
            num1, num2, num3, num4
        """.trimIndent()
        check(code, p.params, "(num1 num2 num3 num4)")

        code = """
            ()
        """.trimIndent()
        check(code, p.paramList, "()")

        code = """
            (num1, num2, num3, num4)
        """.trimIndent()
        check(code, p.paramList, "(num1 num2 num3 num4)")
    }

    @Test
    fun testArgumentsParser() {
        code = """
            num1, num2, num3, num4
        """.trimIndent()
        check(code, p.args, "(num1 num2 num3 num4)")

        code = """
            num1 + 1, num2 + 2, num3 + num1, num4 + num4
        """.trimIndent()
        check(code, p.args, "((num1 + 1) (num2 + 2) (num3 + num1) (num4 + num4))")
    }

    @Test
    fun testPostfixParser() {
        code = """
            (num1 + 1, num2 + 2, num3 + num1, num4 + num4)
        """.trimIndent()
        check(code, p.postfix, "((num1 + 1) (num2 + 2) (num3 + num1) (num4 + num4))")

        code = """
            fib(num1 + 1, num2 + 2, num3 + num1, num4 + num4)
        """.trimIndent()
        check(code, p.primary, "(fib ((num1 + 1) (num2 + 2) (num3 + num1) (num4 + num4)))")

        code = """
            fib()
        """.trimIndent()
        check(code, p.primary, "(fib ())")
    }

    @Test
    fun testDefParser() {
        code = """
            def fib (n) {
                if n < 2 {
                    n
                } else {
                    fib(n - 1) + fib(n - 2)
                }
            }
        """.trimIndent()
        check(code, p.def, "(def fib (n) ((if (n < 2) (n) else (((fib ((n - 1))) + (fib ((n - 2))))))))")
    }
}
