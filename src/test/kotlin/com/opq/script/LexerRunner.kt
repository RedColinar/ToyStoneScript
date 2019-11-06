package com.opq.script

import org.junit.Test

class LexerTest {
    @Test
    fun lexerRunner() {
        val l = Lexer(CodeDialog())
        var t: Token = l.read()
        while (t != Token.EOF) {
            println("=> ${t.getText()}")
            t = l.read()
        }
    }
}
