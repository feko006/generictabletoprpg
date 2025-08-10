package com.feko.generictabletoprpg.features.tracker.domain.model

import android.content.Context
import androidx.annotation.Keep
import com.feko.generictabletoprpg.R
import kotlinx.serialization.Serializable

@Keep
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

        fun createDefault5EStatEntries(context: Context): List<StatEntry> {
            val resources = context.resources
            return listOf(
                StatEntry(
                    resources.getString(R.string.strength),
                    resources.getString(R.string.strength_short),
                    score = 0,
                    bonus = 0,
                    isProficientInSavingThrow = false,
                    isSpellcastingModifier = false,
                    savingThrowBonus = 0,
                    savingThrowAdditionalBonus = 0,
                    skills = listOf(
                        StatSkillEntry(
                            resources.getString(R.string.athletics),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        )
                    ),
                ),
                StatEntry(
                    resources.getString(R.string.dexterity),
                    resources.getString(R.string.dexterity_short),
                    score = 0,
                    bonus = 0,
                    isProficientInSavingThrow = false,
                    isSpellcastingModifier = false,
                    savingThrowBonus = 0,
                    savingThrowAdditionalBonus = 0,
                    skills = listOf(
                        StatSkillEntry(
                            resources.getString(R.string.acrobatics),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            resources.getString(R.string.sleight_of_hand),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            resources.getString(R.string.stealth),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        )
                    ),
                ),
                StatEntry(
                    resources.getString(R.string.constitution),
                    resources.getString(R.string.constitution_short),
                    score = 0,
                    bonus = 0,
                    isProficientInSavingThrow = false,
                    isSpellcastingModifier = false,
                    savingThrowBonus = 0,
                    savingThrowAdditionalBonus = 0,
                    skills = listOf(),
                ),
                StatEntry(
                    resources.getString(R.string.intelligence),
                    resources.getString(R.string.intelligence_short),
                    score = 0,
                    bonus = 0,
                    isProficientInSavingThrow = false,
                    isSpellcastingModifier = false,
                    savingThrowBonus = 0,
                    savingThrowAdditionalBonus = 0,
                    skills = listOf(
                        StatSkillEntry(
                            resources.getString(R.string.arcana),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            resources.getString(R.string.history),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            resources.getString(R.string.investigation),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            resources.getString(R.string.nature),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            resources.getString(R.string.religion),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        )
                    ),
                ),
                StatEntry(
                    resources.getString(R.string.wisdom),
                    resources.getString(R.string.wisdom_short),
                    score = 0,
                    bonus = 0,
                    isProficientInSavingThrow = false,
                    isSpellcastingModifier = false,
                    savingThrowBonus = 0,
                    savingThrowAdditionalBonus = 0,
                    skills = listOf(
                        StatSkillEntry(
                            resources.getString(R.string.animal_handling),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            resources.getString(R.string.insight),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            resources.getString(R.string.medicine),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            resources.getString(R.string.perception),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false,
                            showPassive = true
                        ),
                        StatSkillEntry(
                            resources.getString(R.string.survival),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        )
                    ),
                ),
                StatEntry(
                    resources.getString(R.string.charisma),
                    resources.getString(R.string.charisma_short),
                    score = 0,
                    bonus = 0,
                    isProficientInSavingThrow = false,
                    isSpellcastingModifier = false,
                    savingThrowBonus = 0,
                    savingThrowAdditionalBonus = 0,
                    skills = listOf(
                        StatSkillEntry(
                            resources.getString(R.string.deception),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            resources.getString(R.string.intimidation),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            resources.getString(R.string.performance),
                            bonus = 0,
                            passiveScore = 0,
                            additionalBonus = 0,
                            isProficient = false
                        ),
                        StatSkillEntry(
                            resources.getString(R.string.persuasion),
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