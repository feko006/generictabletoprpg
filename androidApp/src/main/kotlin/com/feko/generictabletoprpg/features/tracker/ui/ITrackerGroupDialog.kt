package com.feko.generictabletoprpg.features.tracker.ui

import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThingGroup

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