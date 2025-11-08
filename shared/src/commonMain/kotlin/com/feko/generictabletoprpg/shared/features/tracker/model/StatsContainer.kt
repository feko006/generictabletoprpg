package com.feko.generictabletoprpg.shared.features.tracker.model

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.acrobatics
import com.feko.generictabletoprpg.animal_handling
import com.feko.generictabletoprpg.arcana
import com.feko.generictabletoprpg.athletics
import com.feko.generictabletoprpg.charisma
import com.feko.generictabletoprpg.charisma_short
import com.feko.generictabletoprpg.constitution
import com.feko.generictabletoprpg.constitution_short
import com.feko.generictabletoprpg.deception
import com.feko.generictabletoprpg.dexterity
import com.feko.generictabletoprpg.dexterity_short
import com.feko.generictabletoprpg.history
import com.feko.generictabletoprpg.insight
import com.feko.generictabletoprpg.intelligence
import com.feko.generictabletoprpg.intelligence_short
import com.feko.generictabletoprpg.intimidation
import com.feko.generictabletoprpg.investigation
import com.feko.generictabletoprpg.medicine
import com.feko.generictabletoprpg.nature
import com.feko.generictabletoprpg.perception
import com.feko.generictabletoprpg.performance
import com.feko.generictabletoprpg.persuasion
import com.feko.generictabletoprpg.religion
import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.sleight_of_hand
import com.feko.generictabletoprpg.stealth
import com.feko.generictabletoprpg.strength
import com.feko.generictabletoprpg.strength_short
import com.feko.generictabletoprpg.survival
import com.feko.generictabletoprpg.wisdom
import com.feko.generictabletoprpg.wisdom_short
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.getString

@DoNotObfuscate
@Serializable
data class StatsContainer(
    val proficiencyBonus: Int,
    val spellSaveDc: Int,
    val spellSaveDcAdditionalBonus: Int,
    val spellAttackBonus: Int,
    val spellAttackAdditionalBonus: Int,
    val initiative: Int,
    val initiativeAdditionalBonus: Int,
    val stats: List<StatEntry>,
    val use5eCalculations: Boolean = true
) {
    companion object {
        val Empty = StatsContainer(0, 0, 0, 0, 0, 0, 0, emptyList())

        suspend fun createDefault5EStatEntries(): List<StatEntry> {
            return listOf(
                StatEntry(
                    getString(Res.string.strength),
                    getString(Res.string.strength_short),
                    score = 0,
                    bonus = 0,
                    isProficientInSavingThrow = false,
                    isSpellcastingModifier = false,
                    savingThrowBonus = 0,
                    savingThrowAdditionalBonus = 0,
                    skills = listOf(
                        StatSkillEntry(
                            getString(Res.string.athletics),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        )
                    ),
                ),
                StatEntry(
                    getString(Res.string.dexterity),
                    getString(Res.string.dexterity_short),
                    score = 0,
                    bonus = 0,
                    isProficientInSavingThrow = false,
                    isSpellcastingModifier = false,
                    savingThrowBonus = 0,
                    savingThrowAdditionalBonus = 0,
                    skills = listOf(
                        StatSkillEntry(
                            getString(Res.string.acrobatics),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            getString(Res.string.sleight_of_hand),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            getString(Res.string.stealth),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        )
                    ),
                ),
                StatEntry(
                    getString(Res.string.constitution),
                    getString(Res.string.constitution_short),
                    score = 0,
                    bonus = 0,
                    isProficientInSavingThrow = false,
                    isSpellcastingModifier = false,
                    savingThrowBonus = 0,
                    savingThrowAdditionalBonus = 0,
                    skills = listOf(),
                ),
                StatEntry(
                    getString(Res.string.intelligence),
                    getString(Res.string.intelligence_short),
                    score = 0,
                    bonus = 0,
                    isProficientInSavingThrow = false,
                    isSpellcastingModifier = false,
                    savingThrowBonus = 0,
                    savingThrowAdditionalBonus = 0,
                    skills = listOf(
                        StatSkillEntry(
                            getString(Res.string.arcana),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            getString(Res.string.history),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            getString(Res.string.investigation),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            getString(Res.string.nature),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            getString(Res.string.religion),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        )
                    ),
                ),
                StatEntry(
                    getString(Res.string.wisdom),
                    getString(Res.string.wisdom_short),
                    score = 0,
                    bonus = 0,
                    isProficientInSavingThrow = false,
                    isSpellcastingModifier = false,
                    savingThrowBonus = 0,
                    savingThrowAdditionalBonus = 0,
                    skills = listOf(
                        StatSkillEntry(
                            getString(Res.string.animal_handling),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            getString(Res.string.insight),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            getString(Res.string.medicine),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            getString(Res.string.perception),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false,
                            showPassive = true
                        ),
                        StatSkillEntry(
                            getString(Res.string.survival),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        )
                    ),
                ),
                StatEntry(
                    getString(Res.string.charisma),
                    getString(Res.string.charisma_short),
                    score = 0,
                    bonus = 0,
                    isProficientInSavingThrow = false,
                    isSpellcastingModifier = false,
                    savingThrowBonus = 0,
                    savingThrowAdditionalBonus = 0,
                    skills = listOf(
                        StatSkillEntry(
                            getString(Res.string.deception),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            getString(Res.string.intimidation),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            getString(Res.string.performance),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            getString(Res.string.persuasion),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        )
                    ),
                )
            )
        }
    }
}