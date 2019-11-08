package com.opq.script.parser

import com.opq.script.Lexer
import com.opq.script.ParseException
import com.opq.script.Token
import com.opq.script.ast.ASTLeaf
import com.opq.script.ast.ASTList
import com.opq.script.ast.ASTree
import java.util.*

interface Element {
    fun parse(lexer: Lexer, res: MutableList<ASTree>)
    fun match(lexer: Lexer): Boolean
}

class Tree(var parser: Parser) : Element {

    override fun parse(lexer: Lexer, res: MutableList<ASTree>) {
        res.add(parser.parse(lexer))
    }

    override fun match(lexer: Lexer): Boolean {
        return parser.match(lexer)
    }
}

class OrTree(var parsers: Array<Parser>) : Element {

    override fun parse(lexer: Lexer, res: MutableList<ASTree>) {
        val p = choose(lexer)
        if (p == null)
            throw ParseException(lexer.peek(0))
        else
            res.add(p.parse(lexer))
    }

    override fun match(lexer: Lexer): Boolean {
        return choose(lexer) != null
    }

    private fun choose(lexer: Lexer): Parser? {
        for (p in parsers) {
            if (p.match(lexer)) return p
        }

        return null
    }

    fun insert(p: Parser) {
        // 查到第一位
        parsers = arrayOf(p, *parsers)
    }
}

class Repeat(var parser: Parser, var onlyOnce: Boolean) : Element {

    override fun parse(lexer: Lexer, res: MutableList<ASTree>) {
        while (parser.match(lexer)) {
            val t: ASTree = parser.parse(lexer)
            // 如果是个空的 ASTList 不添加
            if (t.javaClass != ASTList::class.java || t.numChildren() > 0) {
                res.add(t)
            }
            if (onlyOnce) break
        }
    }

    override fun match(lexer: Lexer): Boolean {
        return parser.match(lexer)
    }
}

abstract class AToken(var type: Class<out ASTLeaf>?) : Element {
    private var factory: Factory

    init {
        if (type == null) type = ASTLeaf::class.java
        factory = createFactory(type, Token::class.java)!!
    }

    override fun parse(lexer: Lexer, res: MutableList<ASTree>) {
        val t = lexer.read()
        if (test(t)) {
            val leaf = factory.make(t)
            res.add(leaf)
        } else
            throw ParseException(t)
    }

    override fun match(lexer: Lexer): Boolean {
        return test(lexer.peek(0))
    }

    protected abstract fun test(t: Token): Boolean
}

class IdToken(type: Class<out ASTLeaf>?, r: HashSet<String>?) : AToken(type) {
    var reserved: HashSet<String> = r ?: HashSet()

    override fun test(t: Token): Boolean {
        return t.isIdentifier() && !reserved.contains(t.getText())
    }
}

class NumToken(type: Class<out ASTLeaf>?) : AToken(type) {
    override fun test(t: Token): Boolean {
        return t.isNumber()
    }
}

class StrToken(type: Class<out ASTLeaf>?) : AToken(type) {
    override fun test(t: Token): Boolean {
        return t.isString()
    }
}

open class Leaf(var tokens: Array<String>) : Element {

    override fun parse(lexer: Lexer, res: MutableList<ASTree>) {
        val t = lexer.read()
        if (t.isIdentifier()) {
            for (token in tokens) {
                if (token == t.getText()) {
                    find(res, t)
                    return
                }
            }
        }

        if (tokens.isNotEmpty())
            throw ParseException(tokens[0] + " expected.", t)
        else
            throw ParseException(t)
    }

    open fun find(res: MutableList<ASTree>, t: Token) {
        res.add(ASTLeaf(t))
    }

    override fun match(lexer: Lexer): Boolean {
        val t = lexer.peek(0)
        if (t.isIdentifier()) {
            for (token in tokens) {
                if (token == t.getText()) return true
            }
        }

        return false
    }
}

class Skip(t: Array<String>) : Leaf(t) {
    override fun find(res: MutableList<ASTree>, t: Token) {}
}

// 优先级
class Precedence(
    var value: Int, var leftAssoc: Boolean // left associative
)

class Operators : HashMap<String, Precedence>() {

    fun add(name: String, prec: Int, leftAssoc: Boolean) {
        put(name, Precedence(prec, leftAssoc))
    }

    companion object {
        var LEFT = true
        var RIGHT = false
    }
}

class Expr(clazz: Class<out ASTree>?, var factor: Parser, var ops: Operators) : Element {
    var factory: Factory = createFactoryForASTList(clazz)

    override fun parse(lexer: Lexer, res: MutableList<ASTree>) {
        var right = factor.parse(lexer)
        var precedence: Precedence? = nextOperator(lexer)
        while (precedence != null) {
            right = doShift(lexer, right, precedence.value)
            precedence = nextOperator(lexer)
        }

        res.add(right)
    }

    private fun doShift(lexer: Lexer, left: ASTree, precedence: Int): ASTree {
        val list = ArrayList<ASTree>()
        list.add(left)
        list.add(ASTLeaf(lexer.read()))
        var right = factor.parse(lexer)
        var next: Precedence? = nextOperator(lexer)
        while (next != null && rightIsExpr(precedence, next)) {
            right = doShift(lexer, right, next.value)
            next = nextOperator(lexer)
        }

        list.add(right)
        return factory.make(list)
    }

    private fun nextOperator(lexer: Lexer): Precedence? {
        val t = lexer.peek(0)
        return if (t.isIdentifier())
            ops[t.getText()]
        else
            null
    }

    private fun rightIsExpr(prec: Int, nextPrec: Precedence): Boolean {
        return if (nextPrec.leftAssoc)
            prec < nextPrec.value
        else
            prec <= nextPrec.value
    }

    override fun match(lexer: Lexer): Boolean {
        return factor.match(lexer)
    }
}
