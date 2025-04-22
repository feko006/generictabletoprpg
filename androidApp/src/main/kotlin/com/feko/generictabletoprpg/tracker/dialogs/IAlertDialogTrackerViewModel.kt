package com.feko.generictabletoprpg.tracker.dialogs

interface IAlertDialogTrackerViewModel
    : IEditDialogTrackerViewModel,
    IValueInputDialogTrackerViewModel,
    ICreateDialogTrackerViewModel,
    ISpellListDialogTrackerViewModel {

    @Deprecated("")
    var dialogType: DialogType

    @Deprecated("")
    val statsEditDialog: IStatsEditDialogSubViewModel

    @Deprecated("")
    enum class DialogType {
        None,
        Create,
        Edit,
        ShowSpellList,
        EditStats
    }

}