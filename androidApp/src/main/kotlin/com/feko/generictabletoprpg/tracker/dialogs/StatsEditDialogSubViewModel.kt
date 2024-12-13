package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.common.alertdialog.IAlertDialogSubViewModel
import com.feko.generictabletoprpg.import.IJson
import com.feko.generictabletoprpg.tracker.Stats
import com.feko.generictabletoprpg.tracker.StatsContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class StatsEditDialogSubViewModel(
    private val viewModelScope: CoroutineScope,
    private val json: IJson,
    override val confirmButtonEnabled: MutableStateFlow<Boolean>,
    override val alertDialog: IAlertDialogSubViewModel,
    private val onDialogConfirmed: () -> Unit
) : IStatsEditDialogSubViewModel {

    override val editedStats: MutableStateFlow<Stats?> = MutableStateFlow(null)

    override fun updateStatsName(name: String) {
        viewModelScope.launch {
            val copy = requireNotNull(editedStats.value).copy() as Stats
            copy.name = name
            editedStats.emit(copy)
        }
    }

    override fun updateStatsProficiencyBonus(proficiencyBonus: String) =
        updateStatIntValue(proficiencyBonus) { statsContainer, bonus ->
            statsContainer.copy(proficiencyBonus = bonus)
        }

    override fun updateStatsInitiativeAdditionalBonus(initiativeAdditionalBonus: String) =
        updateStatIntValue(initiativeAdditionalBonus) { statsContainer, bonus ->
            statsContainer.copy(initiativeAdditionalBonus = bonus)
        }

    override fun updateSpellSaveDcAdditionalBonus(spellSaveDcAdditionalBonus: String) =
        updateStatIntValue(spellSaveDcAdditionalBonus) { statsContainer, bonus ->
            statsContainer.copy(spellSaveDcAdditionalBonus = bonus)
        }

    override fun updateSpellAttackAdditionalBonus(spellAttackAdditionalBonus: String) =
        updateStatIntValue(spellAttackAdditionalBonus) { statsContainer, bonus ->
            statsContainer.copy(spellAttackAdditionalBonus = bonus)
        }

    override fun updateStatScore(statIndex: Int, statScore: String) =
        updateStatIntValue(statScore) { statsContainer, score ->
            val newStatEntry =
                statsContainer.stats[statIndex].copy(score = score)
            statsContainer.copy(
                stats = statsContainer.stats.mapIndexed { index, statEntry ->
                    if (index == statIndex) newStatEntry else statEntry
                })
        }

    override fun updateStatSavingThrowProficiency(statIndex: Int, isProficient: Boolean) =
        updateStatValue { statsContainer ->
            val newStatEntry =
                statsContainer.stats[statIndex].copy(isProficientInSavingThrow = isProficient)
            statsContainer.copy(
                stats = statsContainer.stats.mapIndexed { index, statEntry ->
                    if (index == statIndex) newStatEntry else statEntry
                })
        }

    override fun updateStatSpellcastingModifier(statIndex: Int, isSpellcastingModifier: Boolean) {
        updateStatValue { statsContainer ->
            val newStatEntry =
                statsContainer.stats[statIndex].copy(isSpellcastingModifier = isSpellcastingModifier)
            statsContainer.copy(
                stats = statsContainer.stats.mapIndexed { index, statEntry ->
                    if (index == statIndex) newStatEntry else statEntry
                })
        }
    }

    override fun updateStatSkillAdditionalBonus(
        statIndex: Int,
        skillIndex: Int,
        skillAdditionalBonus: String
    ) = updateStatIntValue(
        skillAdditionalBonus,
    ) { statsContainer, additionalBonus ->
        val newStatEntry =
            statsContainer.stats[statIndex].let {
                val newSkill =
                    it.skills[skillIndex].copy(additionalBonus = additionalBonus)
                it.copy(
                    skills = it.skills.mapIndexed { index, skill ->
                        if (index == skillIndex) newSkill else skill
                    }
                )
            }
        statsContainer.copy(
            stats = statsContainer.stats.mapIndexed { index, statEntry ->
                if (index == statIndex) newStatEntry else statEntry
            })
    }

    override fun updateStatSkillProficiency(
        statIndex: Int,
        skillIndex: Int,
        isProficient: Boolean
    ) = updateStatValue { statsContainer ->
        val newStatEntry =
            statsContainer.stats[statIndex].let {
                val newSkill =
                    it.skills[skillIndex].copy(isProficient = isProficient)
                it.copy(
                    skills = it.skills.mapIndexed { index, skill ->
                        if (index == skillIndex) newSkill else skill
                    }
                )
            }
        statsContainer.copy(
            stats = statsContainer.stats.mapIndexed { index, statEntry ->
                if (index == statIndex) newStatEntry else statEntry
            })
    }

    override fun updateStatSavingThrowAdditionalBonus(
        statIndex: Int,
        savingThrowAdditionalBonus: String
    ) = updateStatIntValue(savingThrowAdditionalBonus) { statsContainer, bonus ->
        val newStatEntry =
            statsContainer.stats[statIndex].copy(savingThrowAdditionalBonus = bonus)
        statsContainer.copy(
            stats = statsContainer.stats.mapIndexed { index, statEntry ->
                if (index == statIndex) newStatEntry else statEntry
            })
    }

    override fun confirmDialogAction() = onDialogConfirmed()

    private fun updateStatIntValue(
        value: String,
        copyWithNewValue: (StatsContainer, Int) -> (StatsContainer)
    ) = updateStatValue { copyWithNewValue(it, value.toIntOrNull() ?: 0) }

    private fun updateStatValue(copyWithNewValue: (StatsContainer) -> (StatsContainer)) {
        viewModelScope.launch {
            val copy = requireNotNull(editedStats.value).copy() as Stats
            val newItemValue = copyWithNewValue(copy.serializedItem)
            copy.setItem(newItemValue, json)
            editedStats.emit(copy)
        }
    }

}