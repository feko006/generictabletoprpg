package com.feko.generictabletoprpg.features.tracker.ui

import com.feko.generictabletoprpg.common.domain.model.IText
import com.feko.generictabletoprpg.features.tracker.domain.model.TrackedThingGroup

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