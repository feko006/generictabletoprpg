package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.runtime.Immutable
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThingGroup

sealed interface ITrackerGroupDialog {

    @Immutable
    data object None : ITrackerGroupDialog

    @Immutable
    data class EditDialog(
        val trackedThingGroup: TrackedThingGroup,
        val dialogTitle: IText
    ) : ITrackerGroupDialog

    @Immutable
    data class DeleteDialog(
        val trackedThingGroup: TrackedThingGroup,
        val dialogTitle: IText
    ) : ITrackerGroupDialog
}