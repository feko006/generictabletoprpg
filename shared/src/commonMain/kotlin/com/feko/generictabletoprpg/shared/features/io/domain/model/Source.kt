package com.feko.generictabletoprpg.shared.features.io.domain.model

import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.shared.features.action.Action
import com.feko.generictabletoprpg.shared.features.ammunition.Ammunition
import com.feko.generictabletoprpg.shared.features.armor.Armor
import com.feko.generictabletoprpg.shared.features.condition.Condition
import com.feko.generictabletoprpg.shared.features.disease.Disease
import com.feko.generictabletoprpg.shared.features.feat.Feat
import com.feko.generictabletoprpg.shared.features.magicitem.MagicItem
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.weapon.Weapon
import kotlinx.serialization.Serializable

@Serializable
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
    val weapons: List<Weapon> = mutableListOf(),
    val magicItems: List<MagicItemImpl> = mutableListOf()
)

@Serializable
@DoNotObfuscate
data class MagicItemImpl(
    override val id: Long = 0L,
    override val name: String,
    override var source: String = "",
    override val type: String,
    override val rarity: String,
    override val attunement: Boolean = false,
    override val description: String
) : MagicItem