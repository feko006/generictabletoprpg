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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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

    private val _healDialog =
        EditAlertDialogSubViewModel(InitiativeEntryEntity.Empty, viewModelScope)
    val healDialog: IEditAlertDialogSubViewModel<InitiativeEntryEntity>
        get() = _healDialog

    private val _damageDialog =
        EditAlertDialogSubViewModel(InitiativeEntryEntity.Empty, viewModelScope)
    val damageDialog: IEditAlertDialogSubViewModel<InitiativeEntryEntity>
        get() = _damageDialog

    private val _removeAfterTakingDamageDialog =
        EditAlertDialogSubViewModel(InitiativeEntryEntity.Empty, viewModelScope)
    val removeAfterTakingDamageDialog: IEditAlertDialogSubViewModel<InitiativeEntryEntity>
        get() = _removeAfterTakingDamageDialog

    val encounterState: Flow<EncounterState> =
        entries.map {
            if (it.isEmpty()) return@map EncounterState.Empty
            val indexOfEntryWithCurrentTurn = it.indexOfFirst { it.hasTurn }
            if (indexOfEntryWithCurrentTurn == -1) return@map EncounterState.ReadyToStart
            return@map if (it[indexOfEntryWithCurrentTurn].isTurnCompleted)
                EncounterState.TurnCompletedChoiceRequired
            else
                EncounterState.TurnInProgress
        }

    private val _scrollToItemWithIndex = MutableSharedFlow<Int>()
    val scrollToItemWithIndex: Flow<Int>
        get() = _scrollToItemWithIndex

    fun createOrUpdateInitiativeEntry(entity: InitiativeEntryEntity) {
        viewModelScope.launch {
            if (entity.isSavedInDatabase) {
                dao.update(entity)
            } else {
                dao.insert(entity)
            }
        }
    }

    fun heal(initiativeEntry: InitiativeEntryEntity, amount: Int) {
        viewModelScope.launch {
            dao.update(initiativeEntry.copy(health = initiativeEntry.health + amount))
        }
    }

    fun damage(initiativeEntry: InitiativeEntryEntity, amount: Int) {
        viewModelScope.launch {
            val newHealthValue = (initiativeEntry.health - amount).coerceAtLeast(0)
            dao.update(initiativeEntry.copy(health = newHealthValue))
            if (newHealthValue == 0 && !initiativeEntry.keepOnRefresh) {
                _removeAfterTakingDamageDialog.show(initiativeEntry)
            }
        }
    }

    fun showCreateNewDialog() {
        viewModelScope.launch {
            _editAlertDialog.show(InitiativeEntryEntity.Empty)
        }
    }

    fun showHealDialog(initiativeEntry: InitiativeEntryEntity) {
        viewModelScope.launch {
            _healDialog.show(initiativeEntry)
        }
    }

    fun showDamageDialog(initiativeEntry: InitiativeEntryEntity) {
        viewModelScope.launch {
            _damageDialog.show(initiativeEntry)
        }
    }

    fun duplicateEntry(initiativeEntry: InitiativeEntryEntity) {
        viewModelScope.launch {
            _editAlertDialog.show(
                initiativeEntry.copy(id = 0L, hasTurn = false, isTurnCompleted = false)
            )
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
            val indexOfCurrentEntry = entries.indexOfFirst { it.hasTurn }
            val currentEntry = entries[indexOfCurrentEntry]
            if (currentEntry.isLairAction || !entries.any { it.canUseLegendaryAction }) {
                progressInitiativeInternal()
            } else {
                dao.setTurnCompleted(currentEntry.id)
                _scrollToItemWithIndex.emit(indexOfCurrentEntry)
            }
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
        _scrollToItemWithIndex.emit(indexOfNextEntry)
    }
}