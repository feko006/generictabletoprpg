package com.feko.generictabletoprpg.initiative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.initiative.InitiativeEntryEntity
import com.feko.generictabletoprpg.common.alertdialog.AlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.EditAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.IAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.IEditAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.toast.IToastSubViewModel
import com.feko.generictabletoprpg.common.toast.ToastSubViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class InitiativeViewModel(private val dao: InitiativeEntryDao) : ViewModel() {

    val entries: Flow<List<InitiativeEntryEntity>> = dao.getAllSortedByInitiative()

    private val _editAlertDialog =
        EditAlertDialogSubViewModel(InitiativeEntryEntity.Empty, viewModelScope)
    val editAlertDialog: IEditAlertDialogSubViewModel<InitiativeEntryEntity> = _editAlertDialog

    private val _confirmDeletionDialog =
        EditAlertDialogSubViewModel(InitiativeEntryEntity.Empty, viewModelScope)
    val confirmDeletionDialog: IEditAlertDialogSubViewModel<InitiativeEntryEntity> =
        _confirmDeletionDialog

    private val _confirmResetDialog = AlertDialogSubViewModel(viewModelScope)
    val confirmResetDialog: IAlertDialogSubViewModel = _confirmResetDialog

    private val _currentRound = MutableStateFlow(0)
    val currentRound: Flow<Int>
        get() = _currentRound

    private val _toastMessage = ToastSubViewModel(viewModelScope)
    val toastMessage: IToastSubViewModel
        get() = _toastMessage

    private val _pickLegendaryActionDialog =
        EditAlertDialogSubViewModel(emptyList<InitiativeEntryEntity>(), viewModelScope)
    val pickLegendaryActionDialog: IEditAlertDialogSubViewModel<List<InitiativeEntryEntity>>
        get() = _pickLegendaryActionDialog

    fun createOrUpdateInitiativeEntry(entity: InitiativeEntryEntity) {
        viewModelScope.launch {
            if (entity.isSavedInDatabase) {
                dao.update(entity)
            } else {
                dao.insert(entity)
            }
        }
    }

    fun showCreateNewDialog() {
        viewModelScope.launch {
            _editAlertDialog.show(InitiativeEntryEntity.Empty)
        }
    }

    fun showEditDialog(initiativeEntry: InitiativeEntryEntity) {
        viewModelScope.launch {
            _editAlertDialog.show(initiativeEntry)
        }
    }

    fun showDeleteDialog(initiativeEntry: InitiativeEntryEntity) {
        viewModelScope.launch {
            _confirmDeletionDialog.show(initiativeEntry)
        }
    }

    fun showResetDialog() {
        viewModelScope.launch {
            _confirmResetDialog.show()
        }
    }

    fun addLairActions() {
        viewModelScope.launch {
            dao.insert(InitiativeEntryEntity.createLairActionEntry())
        }
    }

    fun updateKeepOnReset(initiativeEntry: InitiativeEntryEntity, keepOnReset: Boolean) {
        viewModelScope.launch {
            dao.update(initiativeEntry.copy(keepOnRefresh = keepOnReset))
        }
    }

    fun deleteEntry(initiativeEntry: InitiativeEntryEntity) {
        viewModelScope.launch {
            dao.delete(initiativeEntry)
        }
    }

    fun resetInitiative() {
        viewModelScope.launch {
            _currentRound.emit(0)
            dao.resetInitiative()
            dao.resetAvailableLegendaryActions()
            dao.setCurrentTurn(0L)
            dao.setTurnCompleted(0L)
        }
    }

    fun startInitiative() {
        viewModelScope.launch {
            val entries = entries.first()
            _currentRound.emit(1)
            dao.setCurrentTurn(entries.first().id)
        }
    }

    fun concludeTurnOfCurrentEntry() {
        viewModelScope.launch {
            val entries = entries.first()
            val currentEntry = entries.first { it.hasTurn }
            dao.setTurnCompleted(currentEntry.id)
        }
    }

    fun progressInitiative() {
        viewModelScope.launch {
            progressInitiativeInternal()
        }
    }

    fun progressInitiativeWithLegendaryAction() {
        viewModelScope.launch {
            val entries = entries.first()
            val entriesWithLegendaryActions =
                entries.filter { it.canUseLegendaryAction }
            val numberOfEntriesWithLegendaryActions = entriesWithLegendaryActions.size
            if (numberOfEntriesWithLegendaryActions > 1) {
                _pickLegendaryActionDialog.show(entriesWithLegendaryActions)
            } else if (numberOfEntriesWithLegendaryActions == 1) {
                useLegendaryActionAndProgressInitiative(entriesWithLegendaryActions.first())
            }
        }
    }

    fun useLegendaryActionAndProgressInitiative(
        initiativeEntryEntity: InitiativeEntryEntity
    ) {
        viewModelScope.launch {
            useLegendaryAction(initiativeEntryEntity)
            progressInitiativeInternal()
        }
    }

    private suspend fun useLegendaryAction(entry: InitiativeEntryEntity) {
        _toastMessage.showMessage(R.string.legendary_action_used, entry.name)
        dao.update(entry.copy(availableLegendaryActions = entry.availableLegendaryActions - 1))
    }

    private suspend fun progressInitiativeInternal() {
        val entries = entries.first()
        val indexOfCurrentEntry = entries.indexOfFirst { it.hasTurn }
        val indexOfNextEntry = (indexOfCurrentEntry + 1) % entries.size
        val isNewRound = indexOfNextEntry == 0
        if (isNewRound) {
            _currentRound.emit(_currentRound.value + 1)
            dao.resetAvailableLegendaryActions()
        }
        dao.setCurrentTurn(entries[indexOfNextEntry].id)
        dao.setTurnCompleted(0L)
    }
}