package com.feko.generictabletoprpg.common.data

import com.feko.generictabletoprpg.shared.common.domain.IParseEdnAsMap
import us.bpsm.edn.parser.Parsers

class ParseEdnAsMapEdnJava : IParseEdnAsMap {
    override fun parse(ednContent: String): Map<Any, Any> {
        val parseable = Parsers.newParseable(ednContent)
        val parser = Parsers.newParser(Parsers.defaultConfiguration())
        @Suppress("UNCHECKED_CAST")
        return parser.nextValue(parseable) as Map<Any, Any>
    }
}