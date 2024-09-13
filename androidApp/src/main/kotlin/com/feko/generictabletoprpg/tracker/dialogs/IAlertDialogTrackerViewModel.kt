package com.feko.generictabletoprpg.tracker.dialogs

interface IAlertDialogTrackerViewModel
    : IEditDialogTrackerViewModel,
    IValueInputDialogTrackerViewModel,
    ICreateDialogTrackerViewModel,
    ISpellListDialogTrackerViewModel,
    ISpellSlotSelectDialogTrackerViewModel {

    var dialogType: DialogType

    enum class DialogType {
        None,
        Create,
        Edit,
        ConfirmDeletion,
        ConfirmSpellRemovalFromList,
        AddPercentage,
        ReducePercentage,
        DamageHealth,
        HealHealth,
        AddTemporaryHp,
        RefreshAll,
        AddNumber,
        ReduceNumber,
        ShowSpellList,
        SelectSlotLevelToCastSpell,
        EditText
    }

}