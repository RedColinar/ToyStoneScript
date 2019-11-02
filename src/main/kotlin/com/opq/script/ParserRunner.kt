package com.opq.script
/*
even = 0
odd = 0
while i < 10 {
    if i % 2 == 0 {
        even = even + I
    } else {
        odd = odd + i
    }
    i = i + 1
}
even + odd
 */
fun main() {
    val l = Lexer(CodeDialog())
    val bp = BasicParser()
    while (l.peek(0) !== Token.EOF) {
        val ast = bp.parse(l)
        println("=> $ast")
    }
}
