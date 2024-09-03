package com.feko.generictabletoprpg.tracker.dialogs

interface ISpellSlotSelectDialogTrackerViewModel : IDialogTrackerViewModel {
    var availableSpellSlotsForSpellBeingCast: List<Int>?
    fun castSpell(withSlotLevel: Int)
}