package com.feko.generictabletoprpg.shared.features.io.domain.model

import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.shared.features.action.Action
import com.feko.generictabletoprpg.shared.features.ammunition.Ammunition
import com.feko.generictabletoprpg.shared.features.armor.Armor
import com.feko.generictabletoprpg.shared.features.condition.Condition
import com.feko.generictabletoprpg.shared.features.disease.Disease
import com.feko.generictabletoprpg.shared.features.feat.Feat
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.weapon.Weapon
import kotlinx.serialization.Serializable

@DoNotObfuscate
@Serializable
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