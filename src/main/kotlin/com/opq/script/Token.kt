package com.opq.script

abstract class Token(val lineNumber: Int) {

    val isIdentfier: Boolean
        get() = false

    val isNumber: Boolean
        get() = false

    val isString: Boolean
        get() = false

    val number: Int
        get() = throw RuntimeException()

    val text: String
        get() = ""

    companion object {
        val EOF: Token = object : Token(-1) {

        }
        val EOL = "\\n"
    }
}
