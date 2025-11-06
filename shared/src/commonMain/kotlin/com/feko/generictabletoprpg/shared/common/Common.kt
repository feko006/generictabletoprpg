package com.feko.generictabletoprpg.shared.common

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.action
import com.feko.generictabletoprpg.ammunition
import com.feko.generictabletoprpg.armor
import com.feko.generictabletoprpg.condition
import com.feko.generictabletoprpg.disease
import com.feko.generictabletoprpg.feat
import com.feko.generictabletoprpg.shared.features.action.Action
import com.feko.generictabletoprpg.shared.features.ammunition.Ammunition
import com.feko.generictabletoprpg.shared.features.armor.Armor
import com.feko.generictabletoprpg.shared.features.condition.Condition
import com.feko.generictabletoprpg.shared.features.disease.Disease
import com.feko.generictabletoprpg.shared.features.feat.Feat
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.weapon.Weapon
import com.feko.generictabletoprpg.spell
import com.feko.generictabletoprpg.weapon

val appNamesByType = mapOf(
    Action::class to Res.string.action,
    Ammunition::class to Res.string.ammunition,
    Armor::class to Res.string.armor,
    Condition::class to Res.string.condition,
    Disease::class to Res.string.disease,
    Feat::class to Res.string.feat,
    Spell::class to Res.string.spell,
    Weapon::class to Res.string.weapon
)

val appTypes = appNamesByType.keys
