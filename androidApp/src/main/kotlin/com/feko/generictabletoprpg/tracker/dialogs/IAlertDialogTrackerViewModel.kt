package com.feko.generictabletoprpg.tracker.dialogs

interface IAlertDialogTrackerViewModel
    : IEditDialogTrackerViewModel,
    IValueInputDialogTrackerViewModel,
    ISpellListDialogTrackerViewModel {

    @Deprecated("")
    var dialogType: DialogType

    val statsEditDialog: IStatsEditDialogSubViewModel

    @Deprecated("")
    enum class DialogType {
        None,
        ShowSpellList,
        EditStats
    }

}