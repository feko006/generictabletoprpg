package com.feko.generictabletoprpg.common

@DoNotObfuscate
enum class Stat {
    Str,
    Dex,
    Con,
    Int,
    Wis,
    Cha;

    companion object {
        fun fromOrcbrewString(string: String): Stat {
            return when (string.replace(":orcpub.dnd.e5.character/", "")) {
                "str" -> Str
                "dex" -> Dex
                "con" -> Con
                "int" -> Int
                "wis" -> Wis
                "cha" -> Cha
                else -> throw IllegalStateException("Unable to convert string '$string' to stat")
            }
        }
    }
}
