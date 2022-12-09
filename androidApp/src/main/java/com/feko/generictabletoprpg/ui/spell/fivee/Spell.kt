package com.feko.generictabletoprpg.ui.spell.fivee

data class Spell(
    var name: String? = null,
    var source: String? = null,
    var page: Int? = null,
    var level: Int? = null,
    var school: String? = null,
    var time: List<Time> = arrayListOf(),
    var range: Range? = Range(),
    var components: Components? = Components(),
    var duration: List<Duration> = arrayListOf(),
    var entries: List<Entries> = arrayListOf(),
    var entriesHigherLevel: List<Entries> = arrayListOf(),
    var miscTags: List<String> = arrayListOf(),
    var classes: Classes? = Classes(),
    var hasFluffImages: Boolean? = null
)