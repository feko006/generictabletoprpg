package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavKey
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination
import com.feko.generictabletoprpg.shared.common.ui.theme.LocalDimens
import com.feko.generictabletoprpg.shared.features.tracker.model.EquipmentContainer
import com.feko.generictabletoprpg.shared.features.tracker.model.IEquipmentItem

@Composable
fun EquipmentListScreen(
    trackerViewModel: TrackerViewModel?,
    onNavigateToEquipmentItemDetailsScreen: (IEquipmentItem) -> Unit,
    onPopEquipmentListScreen: () -> Unit,
    onPopAll: ((NavKey) -> Boolean) -> Unit
) {
    if (trackerViewModel == null) return
    val alertDialog by trackerViewModel.equipmentListDialog.collectAsState(ITrackerDialog.None)
    val dereferencedDialog = alertDialog
    if (dereferencedDialog !is ITrackerDialog.EquipmentListDialog) return
    val equipmentListEntries =
        (dereferencedDialog.equipment.serializedItem as EquipmentContainer).entries
    var previousEquipmentList by remember { mutableStateOf(equipmentListEntries) }
    val removedEquipment = previousEquipmentList.minus(equipmentListEntries.toSet()).firstOrNull()
    LaunchedEffect(removedEquipment) {
        if (removedEquipment != null) {
            onPopAll {
                it is INavigationDestination.EquipmentItemDetailsDestination
                        && it.equipmentItem.name == removedEquipment.item.name
            }
        }
    }
    @Suppress("AssignedValueIsNeverRead")
    previousEquipmentList = equipmentListEntries
    DisposableEffect(trackerViewModel) {
        onDispose {
            trackerViewModel.dismissDialog()
        }
    }
    Scaffold(
        topBar = {
            GttrpgTopAppBar(
                dereferencedDialog.title,
                onNavigationIconClick = {}
            )
        }) { paddingValues ->
        val screenAdditionalPadding = LocalDimens.current.paddingMedium
        Column(
            Modifier.padding(paddingValues)
                .padding(horizontal = screenAdditionalPadding),
            verticalArrangement = Arrangement.spacedBy(LocalDimens.current.gapSmall)
        ) {
            EquipmentListContent(
                dereferencedDialog,
                onEquipmentClick = onNavigateToEquipmentItemDetailsScreen,
                onSetQuantityRequested = trackerViewModel::setItemQuantityRequested,
                onRemoveRequested = trackerViewModel::removeItemFromEquipmentListRequested
            )
        }
    }
    EquipmentListSecondaryDialog(dereferencedDialog, trackerViewModel, onPopEquipmentListScreen)
}