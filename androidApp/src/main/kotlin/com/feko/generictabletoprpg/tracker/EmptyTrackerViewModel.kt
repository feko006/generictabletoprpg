package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.alertdialog.EmptyAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.IAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.composable.InputFieldData
import com.feko.generictabletoprpg.common.fabdropdown.EmptyFabDropdownSubViewModel
import com.feko.generictabletoprpg.common.fabdropdown.IFabDropdownSubViewModel
import com.feko.generictabletoprpg.common.toast.EmptyToastSubViewModel
import com.feko.generictabletoprpg.common.toast.IToastSubViewModel
import com.feko.generictabletoprpg.spell.Spell
import com.feko.generictabletoprpg.tracker.dialogs.IAlertDialogTrackerViewModel.DialogType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOf

object EmptyTrackerViewModel : ITrackerViewModel {
    override val alertDialog: IAlertDialogSubViewModel
        get() = EmptyAlertDialogSubViewModel
    override val fabDropdown: IFabDropdownSubViewModel
        get() = EmptyFabDropdownSubViewModel
    override val toast: IToastSubViewModel
        get() = EmptyToastSubViewModel
    override val editedTrackedThingName: MutableStateFlow<InputFieldData>
        get() = MutableStateFlow(InputFieldData.EMPTY)
    override val editedTrackedThingSpellSlotLevel: MutableStateFlow<InputFieldData>
        get() = MutableStateFlow(InputFieldData.EMPTY)
    override val editedTrackedThingValue: MutableStateFlow<InputFieldData>
        get() = MutableStateFlow(InputFieldData.EMPTY)
    override val editedTrackedThingType: MutableStateFlow<TrackedThing.Type>
        get() = MutableStateFlow(TrackedThing.Type.None)
    override val confirmButtonEnabled: MutableStateFlow<Boolean>
        get() = MutableStateFlow(false)
    override var dialogType: DialogType = DialogType.None
    override val spellListBeingPreviewed: StateFlow<SpellList?>
        get() = MutableStateFlow(null)
    override var availableSpellSlotsForSpellBeingCast: List<Int>? = null
    override val combinedItemFlow: Flow<List<Any>>
        get() = flowOf()

    override fun showCreateDialog(type: TrackedThing.Type) = Unit

    override fun showEditDialog(item: TrackedThing) = Unit

    override fun confirmDialogAction() = Unit

    override fun resetValueToDefault(item: TrackedThing) = Unit

    override fun useAbility(item: TrackedThing) = Unit

    override fun useSpell(item: TrackedThing) = Unit

    override fun setName(name: String) = Unit

    override fun setLevel(level: String) = Unit

    override fun setValue(value: String) = Unit

    override fun addToPercentageRequested(item: TrackedThing) = Unit

    override fun subtractFromPercentageRequested(item: TrackedThing) = Unit

    override fun addToNumberRequested(item: TrackedThing) = Unit

    override fun subtractFromNumberRequested(item: TrackedThing) = Unit

    override fun takeDamageRequested(item: TrackedThing) = Unit

    override fun healRequested(item: TrackedThing) = Unit

    override fun addTemporaryHp(item: TrackedThing) = Unit

    override fun updateValueInputField(delta: String) = Unit

    override fun deleteItemRequested(item: TrackedThing) = Unit

    override fun refreshAllRequested() = Unit

    override fun itemReordered(from: Int, to: Int) = Unit

    override fun showPreviewSpellListDialog(spellList: SpellList) = Unit

    override fun addSpellToList(spellId: Long) = Unit

    override fun addingSpellToList(spellList: SpellList) = Unit

    override fun removeSpellFromSpellListRequested(spell: Spell) = Unit

    override fun castSpellRequested(level: Int) = Unit

    override fun castSpell(withSlotLevel: Int) = Unit

    override fun canCastSpell(level: Int): Boolean = false

    override fun useHitDie(item: TrackedThing) = Unit

    override fun restoreHitDie(item: TrackedThing) = Unit
}