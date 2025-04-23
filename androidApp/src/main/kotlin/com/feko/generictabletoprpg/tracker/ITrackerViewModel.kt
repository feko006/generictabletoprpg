package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.fabdropdown.IFabDropdownSubViewModel
import com.feko.generictabletoprpg.common.toast.IToastSubViewModel
import com.feko.generictabletoprpg.tracker.actions.IAbilityActionsTrackerViewModel
import com.feko.generictabletoprpg.tracker.actions.IHitDiceActionsTrackerViewModel
import com.feko.generictabletoprpg.tracker.actions.ISpellSlotActionsTrackerViewModel
import com.feko.generictabletoprpg.tracker.dialogs.IAlertDialogTrackerViewModel
import kotlinx.coroutines.flow.Flow

interface ITrackerViewModel
    : IAbilityActionsTrackerViewModel,
    ISpellSlotActionsTrackerViewModel,
    IHitDiceActionsTrackerViewModel,
    IAlertDialogTrackerViewModel {

    val fabDropdown: IFabDropdownSubViewModel
    val toast: IToastSubViewModel
    val combinedItemFlow: Flow<List<Any>>
    fun refreshAllRequested()
    fun itemReordered(from: Int, to: Int)
    fun addSpellToList(spellId: Long)

}