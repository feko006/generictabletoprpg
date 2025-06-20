package com.feko.generictabletoprpg.common.domain

interface IParseEdnAsMap {
    fun parse(ednContent: String): Map<Any, Any>
}
