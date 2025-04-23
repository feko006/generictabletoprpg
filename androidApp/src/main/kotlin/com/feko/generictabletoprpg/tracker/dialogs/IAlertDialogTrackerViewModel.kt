package com.feko.generictabletoprpg.tracker.dialogs

interface IAlertDialogTrackerViewModel
    : IEditDialogTrackerViewModel,
    IValueInputDialogTrackerViewModel {

    @Deprecated("")
    var dialogType: DialogType

    val statsEditDialog: IStatsEditDialogSubViewModel

    @Deprecated("")
    enum class DialogType {
        None,
        EditStats
    }

}