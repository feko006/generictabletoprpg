package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.common.alertdialog.IStatefulAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.StatefulAlertDialogSubViewModel
import com.feko.generictabletoprpg.import.IJson
import com.feko.generictabletoprpg.tracker.Stats
import com.feko.generictabletoprpg.tracker.StatsContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class StatsEditDialogSubViewModel(
    private val viewModelScope: CoroutineScope,
    private val json: IJson
) : IStatsEditDialogSubViewModel {

    val _alertDialog = StatefulAlertDialogSubViewModel(Stats.Empty, viewModelScope)
    override val alertDialog: IStatefulAlertDialogSubViewModel<Stats>
        get() = _alertDialog

    override fun updateStatsName(name: String) {
        viewModelScope.launch {
            val copy = alertDialog.state.value.copy() as Stats
            copy.name = name
            _alertDialog.updateState(copy)
        }
    }

    override fun updateStatsProficiencyBonus(proficiencyBonus: Int) =
        updateStatValue { it.copy(proficiencyBonus = proficiencyBonus) }

    override fun updateStatsInitiativeAdditionalBonus(initiativeAdditionalBonus: Int) =
        updateStatValue { it.copy(initiativeAdditionalBonus = initiativeAdditionalBonus) }

    override fun updateSpellSaveDcAdditionalBonus(spellSaveDcAdditionalBonus: Int) =
        updateStatValue { it.copy(spellSaveDcAdditionalBonus = spellSaveDcAdditionalBonus) }

    override fun updateSpellAttackAdditionalBonus(spellAttackAdditionalBonus: Int) =
        updateStatValue { it.copy(spellAttackAdditionalBonus = spellAttackAdditionalBonus) }

    override fun updateStatScore(statIndex: Int, statScore: Int) =
        updateStatValue {
            val newStatEntry = it.stats[statIndex].copy(score = statScore)
            it.copy(
                stats = it.stats.mapIndexed { index, statEntry ->
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
        skillAdditionalBonus: Int
    ) = updateStatValue { statsContainer ->
        val newStatEntry =
            statsContainer.stats[statIndex].let {
                val newSkill =
                    it.skills[skillIndex].copy(additionalBonus = skillAdditionalBonus)
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
        savingThrowAdditionalBonus: Int
    ) = updateStatValue {
        val newStatEntry =
            it.stats[statIndex].copy(savingThrowAdditionalBonus = savingThrowAdditionalBonus)
        it.copy(
            stats = it.stats.mapIndexed { index, statEntry ->
                if (index == statIndex) newStatEntry else statEntry
            })
    }

    private fun updateStatIntValue(
        value: String,
        copyWithNewValue: (StatsContainer, Int) -> (StatsContainer)
    ) = updateStatValue { copyWithNewValue(it, value.toIntOrNull() ?: 0) }

    private fun updateStatValue(copyWithNewValue: (StatsContainer) -> (StatsContainer)) {
        viewModelScope.launch {
            val copy = _alertDialog.state.value.copy() as Stats
            val newItemValue = copyWithNewValue(copy.serializedItem)
            copy.setItem(newItemValue, json)
            _alertDialog.updateState(copy)
        }
    }
}