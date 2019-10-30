package com.opq.script

import java.lang.Exception

class ParseException : Exception {
    constructor(token: Token) : this("", token)
    constructor(message: String, token: Token) : super("syntax error around " + location(token) + "." + message)
}

private fun location(token: Token): String {
    return if (token == Token.EOF) {
        "the last line"
    } else {
        "\"${token.getText()}\" at line ${token.lineNumber}"
    }
}