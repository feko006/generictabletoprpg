package com.feko.generictabletoprpg.common

data class Cost(
    val number: Long,
    val type: String
) {
    override fun toString(): String = "$number $type"
}