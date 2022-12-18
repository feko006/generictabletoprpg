package com.feko.generictabletoprpg.app

import com.feko.generictabletoprpg.action.Action

data class Source(
    val name: String,
    val actions: List<Action> = listOf()
)