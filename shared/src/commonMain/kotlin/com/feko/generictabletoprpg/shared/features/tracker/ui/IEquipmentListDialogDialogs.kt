package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.runtime.Immutable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.confirm_equipment_item_removal_from_list_dialog_title
import com.feko.generictabletoprpg.set_quantity
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.features.tracker.model.EquipmentEntry

sealed interface IEquipmentListDialogDialogs {
    @Immutable
    data object None : IEquipmentListDialogDialogs

    @Immutable
    data class ConfirmItemRemovalDialog(
        val equipmentEntry: EquipmentEntry,
        val title: IText = IText.StringResourceText(Res.string.confirm_equipment_item_removal_from_list_dialog_title)
    ) : IEquipmentListDialogDialogs

    @Immutable
    data class SetItemQuantityDialog(
        val equipmentEntry: EquipmentEntry,
        val title: IText = IText.StringResourceText(Res.string.set_quantity)
    ) : IEquipmentListDialogDialogs
}