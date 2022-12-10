package com.feko.generictabletoprpg.spells

import com.feko.generictabletoprpg.common.Range

data class Spell(
    val id: Long,
    val name: String,
    val description: String,
    val school: String,
    val duration: String,
    val concentration: Boolean,
    val level: Int,
    val source: String,
    val components: String,
    val castingTime: String,
    val classesThatCanCast: List<String>,
    val range: Range
)