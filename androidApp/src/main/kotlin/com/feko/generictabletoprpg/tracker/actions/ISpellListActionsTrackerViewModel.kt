package com.feko.generictabletoprpg.tracker.actions

import com.feko.generictabletoprpg.tracker.SpellList

interface ISpellListActionsTrackerViewModel : IBasicActionsTrackerViewModel {
    fun showPreviewSpellListDialog(spellList: SpellList)
    fun addingSpellToList(spellList: SpellList)
}