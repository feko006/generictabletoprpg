package com.feko.generictabletoprpg.shared.features.tracker.model

import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import kotlinx.serialization.Serializable

@DoNotObfuscate
@Serializable
data class StatSkillEntry(
    val name: String,
    val bonus: Int,
    val passiveScore: Int,
    val additionalBonus: Int,
    val isProficient: Boolean,
    val hasExpertise: Boolean = false,
    val showPassive: Boolean = false
)