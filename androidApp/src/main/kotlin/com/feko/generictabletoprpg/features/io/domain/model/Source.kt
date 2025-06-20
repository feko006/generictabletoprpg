package com.feko.generictabletoprpg.features.io.domain.model

import com.feko.generictabletoprpg.features.action.Action
import com.feko.generictabletoprpg.features.ammunition.Ammunition
import com.feko.generictabletoprpg.features.armor.Armor
import com.feko.generictabletoprpg.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.features.condition.Condition
import com.feko.generictabletoprpg.features.disease.Disease
import com.feko.generictabletoprpg.features.feat.Feat
import com.feko.generictabletoprpg.features.spell.Spell
import com.feko.generictabletoprpg.features.weapon.Weapon

@DoNotObfuscate
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