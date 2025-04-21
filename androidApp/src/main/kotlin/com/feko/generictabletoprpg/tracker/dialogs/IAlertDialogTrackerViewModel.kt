package com.feko.generictabletoprpg.tracker.dialogs

import com.feko.generictabletoprpg.tracker.actions.IStatsActionsTrackerViewModel

interface IAlertDialogTrackerViewModel
    : IEditDialogTrackerViewModel,
    IValueInputDialogTrackerViewModel,
    ICreateDialogTrackerViewModel,
    ISpellListDialogTrackerViewModel,
    ISpellSlotSelectDialogTrackerViewModel,
    IStatsPreviewDialogTrackerViewModel,
    IStatsActionsTrackerViewModel {

    var dialogType: DialogType

    val statsEditDialog: IStatsEditDialogSubViewModel

    enum class DialogType {
        None,
        Create,
        Edit,
        ShowSpellList,
        SelectSlotLevelToCastSpell,
        EditStats,
        PreviewStatSkills
    }

}