package com.opq.script

abstract class Token(val lineNumber: Int) {

    open fun isIdentifier(): Boolean = false

    open fun isNumber(): Boolean = false

    open fun isString(): Boolean = false

    open fun getNumber(): Int = throw RuntimeException()

    open fun getText(): String = ""

    companion object {
        val EOF: Token = object : Token(-1) {}
        const val EOL = "\\n"
    }
}

class NumToken(line: Int, private val value: Int) : Token(line) {

    override fun isNumber(): Boolean = true

    override fun getText(): String = value.toString()

    override fun getNumber(): Int = value
}

class IdToken(line: Int, private val id: String): Token(line) {

    override fun isIdentifier(): Boolean = true

    override fun getText(): String = id
}

class StrToken(line: Int, private val literal : String): Token(line) {

    override fun isString(): Boolean = true

    override fun getText(): String = literal
}
