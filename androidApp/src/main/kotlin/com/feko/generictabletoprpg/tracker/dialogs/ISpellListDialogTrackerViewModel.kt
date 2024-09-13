package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.tracker.SpellList
import com.feko.generictabletoprpg.tracker.SpellListEntry
import kotlinx.coroutines.flow.StateFlow

interface ISpellListDialogTrackerViewModel : IDialogTrackerViewModel {
    val spellListBeingPreviewed: StateFlow<SpellList?>
    fun removeSpellFromSpellListRequested(spell: SpellListEntry)
    fun castSpellRequested(level: Int)
    fun canCastSpell(level: Int): Boolean
    fun changeSpellListEntryPreparedState(spellListEntry: SpellListEntry, isPrepared: Boolean)
}