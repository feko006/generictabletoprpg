package com.feko.generictabletoprpg.import

interface ParseEdnAsMapPort {
    fun parse(ednContent: String): Map<Any, Any>
}
