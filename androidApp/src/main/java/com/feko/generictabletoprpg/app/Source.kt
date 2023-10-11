package com.feko.generictabletoprpg.app

import com.feko.generictabletoprpg.action.Action
import com.feko.generictabletoprpg.ammunition.Ammunition
import com.feko.generictabletoprpg.armor.Armor
import com.feko.generictabletoprpg.condition.Condition
import com.feko.generictabletoprpg.disease.Disease
import com.feko.generictabletoprpg.feat.Feat
import com.feko.generictabletoprpg.spell.Spell
import com.feko.generictabletoprpg.weapon.Weapon

data class Source(
    val name: String,
    val actions: List<Action> = mutableListOf(),
    val ammunition: List<Ammunition> = mutableListOf(),
    val armor: List<Armor> = mutableListOf(),
    val conditions: List<Condition> = mutableListOf(),
    val diseases: List<Disease> = mutableListOf(),
    val feats: List<Feat> = mutableListOf(),
    val spells: List<Spell> = mutableListOf(),
    val weapons: List<Weapon> = mutableListOf()
)