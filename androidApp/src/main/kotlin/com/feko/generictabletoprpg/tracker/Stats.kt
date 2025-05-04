package com.feko.generictabletoprpg.tracker

import android.content.Context
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.DoNotObfuscate

@DoNotObfuscate
class Stats(
    id: Long = 0L,
    name: String,
    value: String,
    index: Int = 0,
    groupId: Long = 0L
) : JsonTrackedThing<StatsContainer>(
    id,
    name,
    value,
    Type.FiveEStats,
    index,
    groupId,
    StatsContainer::class.java
) {

    override fun createDefaultValue(): StatsContainer =
        StatsContainer(
            proficiencyBonus = 0,
            spellSaveDc = 0,
            spellSaveDcAdditionalBonus = 0,
            spellAttackBonus = 0,
            spellAttackAdditionalBonus = 0,
            initiative = 0,
            initiativeAdditionalBonus = 0,
            stats = listOf()
        )

    override fun createCopy(): JsonTrackedThing<StatsContainer> =
        Stats(id, name, value, index, groupId)

    override fun getPrintableValue(): String = throw IllegalStateException()

    companion object {
        val Empty = Stats(0L, "", "", 0, 0L)
    }
}

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
