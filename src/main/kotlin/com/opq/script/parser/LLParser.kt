package com.opq.script.parser

import com.opq.script.Lexer
import com.opq.script.ParseException
import com.opq.script.Token
import com.opq.script.ast.*

class LLParser(private val lexer: Lexer) {

    private fun token(name: String) {
        val token: Token = lexer.read()
        if (!token.isIdentifier() || name != token.getText()) throw ParseException(token)
    }

    private fun isToken(name: String): Boolean {
        return isTokenWithIndex(name, 0)
    }

    private fun isTokenWithIndex(name: String, tokenIndex: Int): Boolean {
        val token: Token = lexer.peek(tokenIndex)
        return token.isIdentifier() && name == token.getText()
    }


//    fun program(): ASTree {
//
//    }

    private fun statement(): ASTree {
        if (isToken("if")) {
            token("if")
            val condition = expression()
            val ifBlock = block()
            if (isToken("else")) {
                token("else")
                val elseBlock = block()
                return IfStmnt(arrayListOf(condition, ifBlock, elseBlock))
            }
            return IfStmnt(arrayListOf(condition, ifBlock))
        } else if (isToken("while")) {
            token("while")
            val condition = expression()
            val block = block()
            return WhileStmnt(arrayListOf(condition, block))
        }
        return expression()
    }

    private fun isBlock(tokenIndex: Int): Boolean {
        return isTokenWithIndex("{", tokenIndex)
    }

    private fun isStatement(tokenIndex: Int): Boolean {
        if (isTokenWithIndex("if", tokenIndex)) {
            if (isExpression(tokenIndex + 1) && isBlock(tokenIndex + 2)) {
                if (isTokenWithIndex("else", tokenIndex + 3)) {
                    return isBlock(tokenIndex + 4)
                }
                return true
            }
        } else if (isTokenWithIndex("while", tokenIndex)) {
            return isExpression(tokenIndex + 1) && isBlock(tokenIndex + 2)
        }

        return isExpression(tokenIndex)
    }

    private fun isExpression(tokenIndex: Int): Boolean {
        return (isFactor(tokenIndex) && BinaryExpr.isOperater(lexer.peek(tokenIndex + 1)) && isFactor(tokenIndex + 2))
                || isFactor(tokenIndex)
    }

    private fun isFactor(tokenIndex: Int): Boolean {
        return (isToken("-") && isPrimary(tokenIndex + 1)) || isPrimary(tokenIndex)
    }

    private fun isPrimary(tokenIndex: Int): Boolean {
        val token = lexer.peek(tokenIndex)
        return isExpression(tokenIndex) || token.isNumber() || token.isIdentifier() || token.isString()
    }

    private fun block(): ASTree {
        if (isToken("{")) {
            token("{")
            val statement = statement()

        }

        throw ParseException(lexer.peek(0))
    }

    private fun expression(): ASTree {
        var left = factor()
        while (BinaryExpr.isOperater(lexer.peek(0))) {
            val op = ASTLeaf(lexer.read())
            val right = factor()
            left = BinaryExpr(arrayListOf(left, op, right))
        }
        return left
    }

    private fun factor(): ASTree {
        if (isToken("-")) {
            val op = ASTLeaf(lexer.read())
            val primary = primary()
            return NegativeExpr(arrayListOf(op, primary))
        }

        return primary()
    }

    private fun primary(): ASTree {
        if (isToken("(")) {
            token("(")
            val t: ASTree = expression()
            token(")")
            return t
        }

        val t: Token = lexer.read()
        when {
            t.isNumber() -> return NumberLiteral(t)
            t.isString() -> return StringLiteral(t)
            t.isIdentifier() -> return Name(t)
        }
        throw ParseException(t)
    }
}