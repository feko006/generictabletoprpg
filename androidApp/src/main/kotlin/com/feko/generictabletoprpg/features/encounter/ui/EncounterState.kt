package com.feko.generictabletoprpg.features.encounter.ui

import androidx.compose.runtime.Immutable

@Immutable
enum class EncounterState(
    val isAddButtonVisible: Boolean,
    val isResetButtonVisible: Boolean,
    val isStartEncounterButtonVisible: Boolean,
    val isCompleteTurnButtonVisible: Boolean,
    val isLegendaryActionButtonVisible: Boolean,
    val isNextTurnButtonVisible: Boolean
) {
    Empty(true, false, false, false, false, false),
    ReadyToStart(true, true, true, false, false, false),
    TurnInProgress(true, true, false, true, false, false),
    TurnCompletedChoiceRequired(true, true, false, false, true, true)
}