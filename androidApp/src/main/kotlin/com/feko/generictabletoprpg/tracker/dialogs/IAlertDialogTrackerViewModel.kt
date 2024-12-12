package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.dialogs.IStatsPreviewDialogTrackerViewModel

interface IAlertDialogTrackerViewModel
    : IEditDialogTrackerViewModel,
    IValueInputDialogTrackerViewModel,
    ICreateDialogTrackerViewModel,
    ISpellListDialogTrackerViewModel,
    ISpellSlotSelectDialogTrackerViewModel,
    IStatsEditDialogTrackerViewModel,
    IStatsPreviewDialogTrackerViewModel {

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
        EditText,
        EditStats,
        PreviewStatSkills
    }

}