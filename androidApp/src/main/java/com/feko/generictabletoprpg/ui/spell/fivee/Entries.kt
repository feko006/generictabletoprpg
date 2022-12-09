package com.feko.generictabletoprpg.ui.spell.fivee

sealed class Entries {
    data class FullEntries(
        var type: String? = null,
        var name: String? = null,
        var entries: List<Entries> = arrayListOf()
    ) : Entries() {
        override fun toString(): String {
            return "$name${entries.toReadableString()}"
        }
    }

    data class StringEntry(var entry: String) : Entries() {
        override fun toString(): String {
            return "\n${entry}"
        }
    }
}

fun List<Entries>.toReadableString(): String {
    val builder = StringBuilder()
    forEach {
        builder.append("\n")
        builder.append(it.toString())
    }
    return builder.toString()
}