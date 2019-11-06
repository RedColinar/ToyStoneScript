package com.opq.script

import com.opq.script.ast.NativeFunction
import com.opq.script.interpreter.Environment
import java.lang.reflect.Method
import javax.swing.JOptionPane

class Natives {
    fun environment(env: Environment): Environment {
        appendNatives(env)
        return env
    }

    private fun appendNatives(env: Environment) {
        append(env, "print", Natives::class.java, "print", Any::class.java)
        append(env, "read", Natives::class.java, "read")
        append(env, "length", Natives::class.java, "length", String::class.java)
        append(env, "toInt", Natives::class.java, "toInt", Any::class.java)
        append(env, "currentTime", Natives::class.java, "currentTime")
    }

    private fun append(
        env: Environment, name: String, clazz: Class<*>, methodName: String, vararg params: Class<*>
    ) {
        val m: Method
        try {
            m = clazz.getMethod(methodName, *params)
        } catch (e: Exception) {
            throw StoneException("cannot find a native function: $methodName")
        }

        env.put(name, NativeFunction(methodName, m))
    }

    companion object {

        // native methods
        @JvmStatic
        fun print(obj: Any) {
            println(obj.toString())
            return
        }

        @JvmStatic
        fun read(): String {
            return JOptionPane.showInputDialog(null)
        }

        @JvmStatic
        fun length(s: String): Int {
            return s.length
        }

        @JvmStatic
        fun toInt(value: Any): Int {
            return if (value is String)
                Integer.parseInt(value)
            else (value as? Int)?.toInt() ?: throw NumberFormatException(value.toString())
        }

        @JvmStatic
        private val startTime = System.currentTimeMillis()

        @JvmStatic
        fun currentTime(): Int {
            return (System.currentTimeMillis() - startTime).toInt()
        }
    }
}
