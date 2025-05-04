package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.IText

sealed interface ITrackerGroupDialog {

    data object None : ITrackerGroupDialog

    data class EditDialog(
        val trackedThingGroup: TrackedThingGroup,
        val dialogTitle: IText
    ) : ITrackerGroupDialog

    data class DeleteDialog(
        val trackedThingGroup: TrackedThingGroup,
        val dialogTitle: IText
    ) : ITrackerGroupDialog
}