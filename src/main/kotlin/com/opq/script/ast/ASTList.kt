package com.opq.script.ast

open class ASTList(protected var children: List<ASTree>) : ASTree() {
    override fun child(i: Int): ASTree {
        return children[i]
    }

    override fun numChildren(): Int {
        return children.size
    }

    override fun children(): Iterator<ASTree> {
        return children.iterator()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        builder.append('(')
        var sep = ""
        for (t in children) {
            builder.append(sep)
            sep = " "
            builder.append(t.toString())
        }
        return builder.append(')').toString()
    }

    override fun location(): String? {
        for (t in children) {
            val s = t.location()
            if (s != null) return s
        }
        return null
    }
}
