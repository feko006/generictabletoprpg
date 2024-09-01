package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.spell.Spell
import com.feko.generictabletoprpg.tracker.SpellList
import kotlinx.coroutines.flow.StateFlow

interface ISpellListDialogTrackerViewModel : IDialogTrackerViewModel {
    val spellListBeingPreviewed: StateFlow<SpellList?>
    fun removeSpellFromSpellListRequested(spell: Spell)
    fun castSpellRequested(level: Int)
    fun canCastSpell(level: Int): Boolean
}