package com.opq.script

import java.io.LineNumberReader
import java.io.Reader
import java.util.regex.Matcher
import java.util.regex.Pattern

class Lexer(reader: Reader) {
    private val integerPat = "[0-9]+"
    private val stringPat = "\"(\\\\\"|\\\\\\\\|\\\\n|[^\"])*\""
    private val identifierPat = "[A-Z_a-z][A-Z_a-z0-9]*|==|<=|>=|&&|\\|\\||\\p{Punct}"

    private val blankSpace = "\\s*"
    private val note = "//.*"
    private val regexPat = "$blankSpace(($note)|($integerPat)|($stringPat)|$identifierPat)?"

    private val pattern = Pattern.compile(regexPat)
    private val queue = ArrayList<Token>()
    private var hasMore: Boolean = true

    private val reader: LineNumberReader = LineNumberReader(reader)

    fun read(): Token {
        return if (fillQueue(0)) queue.removeAt(0) else Token.EOF
    }

    fun peek(i: Int): Token {
        return if (fillQueue(i)) queue[i] else Token.EOF
    }

    private fun fillQueue(i: Int): Boolean {
        while (i >= queue.size) {
            if (hasMore) readLine() else return false
        }
        return true
    }

    fun readLine() {
        val line: String? = reader.readLine()

        if (line == null) {
            hasMore = false
            return
        }

        val lineNo = reader.lineNumber
        val matcher = pattern.matcher(line)
        matcher.useTransparentBounds(true).useAnchoringBounds(false)

        var pos = 0
        val endPos = line.length
        while (pos < endPos) {
            matcher.region(pos, endPos)
            if (matcher.lookingAt()) {
                addToken(lineNo, matcher)
                pos = matcher.end()
            } else {
                throw RuntimeException("bad token at $lineNo")
            }
        }
        queue.add(IdToken(lineNo, Token.EOL))
    }

    fun addToken(lineNo: Int, matcher: Matcher) {
        // group是针对（）来说的，group（0）就是指的整个串，group（1） 指的是第一个括号里的东西，group（2）指的第二个括号里的东西。
        val m = matcher.group(1) ?: return
        if (matcher.group(2) == null) {
            val token = when {
                matcher.group(3) != null -> NumToken(lineNo, Integer.parseInt(m))
                matcher.group(4) != null -> StrToken(lineNo, toStringLiteral(m))
                else -> IdToken(lineNo, m)
            }
            queue.add(token)
        }
    }

    private fun toStringLiteral(s: String): String {
        val sb = StringBuilder()
        val len = s.length - 1
        var i = 1
        while (i < len) {
            var c = s[i]
            if (c == '\\' && i + 1 < len) {
                val c2 = s[i + 1]
                if (c2 == '"' || c2 == '\\')
                    c = s[++i]
                else if (c2 == 'n') {
                    ++i
                    c = '\n'
                }
            }
            sb.append(c)
            i++
        }
        return sb.toString()
    }
}
