package com.feko.generictabletoprpg.app

import com.feko.generictabletoprpg.action.Action
import com.feko.generictabletoprpg.condition.Condition

data class Source(
    val name: String,
    val actions: List<Action> = listOf(),
    val conditions: List<Condition> = listOf()
)