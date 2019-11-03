package com.opq.script.parser

import com.opq.script.Parser.rule
import com.opq.script.ast.Arguments
import com.opq.script.ast.DefStmnt
import com.opq.script.ast.ParameterList

class FuncParser : BasicParser() {
    var param = rule().identifier(reserved)
    var params = rule(ParameterList::class.java)
        .ast(param).repeat(rule().sep(",").ast(param))
    var paramList = rule().sep("(").maybe(params).sep(")")
    var def = rule(DefStmnt::class.java)
        .sep("def").identifier(reserved).ast(paramList).ast(block)
    var args = rule(Arguments::class.java)
        .ast(expr).repeat(rule().sep(",").ast(expr))
    var postfix = rule().sep("(").maybe(args).sep(")")

    init {
        reserved.add(")")
        primary.repeat(postfix)
        simple.option(args)
        program.insertChoice(def)
    }
}