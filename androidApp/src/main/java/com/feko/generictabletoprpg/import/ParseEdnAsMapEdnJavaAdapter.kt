package com.feko.generictabletoprpg.import

import us.bpsm.edn.parser.Parsers

class ParseEdnAsMapEdnJavaAdapter : ParseEdnAsMapPort {
    override fun invoke(text: String): Map<Any, Any> {
        val parseable = Parsers.newParseable(text)
        val parser = Parsers.newParser(Parsers.defaultConfiguration())
        @Suppress("UNCHECKED_CAST")
        return parser.nextValue(parseable) as Map<Any, Any>
    }
}