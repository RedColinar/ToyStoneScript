package com.opq.script

import com.opq.script.ast.ASTree

class StoneException : RuntimeException {
    constructor(m: String) : super(m) {}
    constructor(m: String, t: ASTree) : super(m + " " + t.location()) {}
}