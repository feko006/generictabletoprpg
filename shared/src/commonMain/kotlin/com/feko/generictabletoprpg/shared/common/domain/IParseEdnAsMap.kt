package com.feko.generictabletoprpg.shared.common.domain

interface IParseEdnAsMap {
    fun parse(ednContent: String): Map<Any, Any>
}
