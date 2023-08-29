package com.feko.generictabletoprpg.app

import com.feko.generictabletoprpg.action.Action
import com.feko.generictabletoprpg.condition.Condition
import com.feko.generictabletoprpg.disease.Disease

data class Source(
    val name: String,
    val actions: List<Action> = listOf(),
    val conditions: List<Condition> = listOf(),
    val diseases: List<Disease> = listOf()
)