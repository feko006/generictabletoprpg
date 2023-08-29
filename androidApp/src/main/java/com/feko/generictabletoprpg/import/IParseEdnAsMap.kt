package com.feko.generictabletoprpg.import

interface IParseEdnAsMap {
    fun parse(ednContent: String): Map<Any, Any>
}
