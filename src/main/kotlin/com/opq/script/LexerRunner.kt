package com.opq.script

import java.io.*
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.JScrollPane
import javax.swing.JTextArea

fun main() {
    val l = Lexer(CodeDialog())
    var t: Token = l.read()
    while (t != Token.EOF) {
        println("=> ${t.getText()}")
        t = l.read()
    }
}

class CodeDialog : Reader() {
    private var buffer: String? = null
    private var pos = 0

    override fun close() = throw IOException()

    override fun read(cbuf: CharArray, off: Int, len: Int): Int {
        if (buffer == null) {
            val stdIn = showDialog()
            if (stdIn == "") {
                return -1
            } else {
                println(stdIn)
                buffer = stdIn + "\n"
                pos = 0
            }
        }

        var size = 0
        val length = buffer?.length ?: 0

        while (pos < length && size < len) {
            cbuf[off + size++] = buffer!![pos++]
        }
        if (pos == length) {
            buffer = null
        }

        return size
    }

    private fun showDialog(): String {
        val area = JTextArea(20, 40)
        val pane = JScrollPane(area)
        val result = JOptionPane.showOptionDialog(
            null,
            pane,
            "Input",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE,
            null,
            null,
            null
        )
        return if (result == JOptionPane.OK_OPTION) area.text else ""
    }
}

fun file(): Reader {
    val chooser = JFileChooser()
    if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        return BufferedReader(FileReader(chooser.selectedFile))
    } else {
        throw FileNotFoundException()
    }
}