package com.feko.generictabletoprpg.features.encounter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.add
import com.feko.generictabletoprpg.common.ui.viewmodel.IToastSubViewModel
import com.feko.generictabletoprpg.common.ui.viewmodel.ToastSubViewModel
import com.feko.generictabletoprpg.edit
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.features.encounter.InitiativeEntryDao
import com.feko.generictabletoprpg.shared.features.encounter.InitiativeEntryEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EncounterViewModel(private val dao: InitiativeEntryDao) : ViewModel() {

    val entries: Flow<List<InitiativeEntryEntity>> = dao.getAllSortedByInitiative()

    private val _dialog = MutableStateFlow<IEncounterDialog>(IEncounterDialog.None)
    val dialog: Flow<IEncounterDialog> = _dialog

    private var areLairActionsAdded = false

    private val _currentRound = MutableStateFlow(0)
    val currentRound: Flow<Int>
        get() = _currentRound

    private val _toastMessage = ToastSubViewModel(viewModelScope)
    val toastMessage: IToastSubViewModel
        get() = _toastMessage

    val encounterState: Flow<EncounterState> =
        entries.map { entry ->
            if (entry.isEmpty()) return@map EncounterState.Empty
            val indexOfEntryWithCurrentTurn = entry.indexOfFirst { it.hasTurn }
            if (indexOfEntryWithCurrentTurn == -1) return@map EncounterState.ReadyToStart
            return@map if (entry[indexOfEntryWithCurrentTurn].isTurnCompleted)
                EncounterState.TurnCompletedChoiceRequired
            else
                EncounterState.TurnInProgress
        }

    init {
        viewModelScope.launch {
            entries.collect { entries -> areLairActionsAdded = entries.any { it.isLairAction } }
        }
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
                _dialog.update { IEncounterDialog.RemoveAfterTakingDamageDialog(initiativeEntry) }
            }
        }
    }

    fun showCreateNewDialog() =
        _dialog.update {
            IEncounterDialog.EditDialog(
                InitiativeEntryEntity.Companion.Empty,
                isLairActionsButtonVisible = !areLairActionsAdded,
                IText.StringResourceText(Res.string.add)
            )
        }

    fun showInitiativeDialog(initiativeEntry: InitiativeEntryEntity) =
        _dialog.update { IEncounterDialog.InitiativeDialog(initiativeEntry) }

    fun showHealDialog(initiativeEntry: InitiativeEntryEntity) =
        _dialog.update { IEncounterDialog.HealDialog(initiativeEntry) }

    fun showDamageDialog(initiativeEntry: InitiativeEntryEntity) =
        _dialog.update { IEncounterDialog.DamageDialog(initiativeEntry) }

    fun duplicateEntry(initiativeEntry: InitiativeEntryEntity) =
        _dialog.update {
            IEncounterDialog.EditDialog(
                initiativeEntry.copy(id = 0L, hasTurn = false, isTurnCompleted = false),
                isLairActionsButtonVisible = !areLairActionsAdded,
                IText.StringResourceText(Res.string.add)
            )
        }

    fun showEditDialog(initiativeEntry: InitiativeEntryEntity) =
        _dialog.update {
            IEncounterDialog.EditDialog(
                initiativeEntry,
                isLairActionsButtonVisible = !areLairActionsAdded,
                IText.StringResourceText(Res.string.edit)
            )
        }

    fun showDeleteDialog(initiativeEntry: InitiativeEntryEntity) =
        _dialog.update { IEncounterDialog.ConfirmDeletionDialog(initiativeEntry) }

    fun showResetDialog() = _dialog.update { IEncounterDialog.ConfirmResetDialog }

    fun addLairActions() {
        viewModelScope.launch {
            dao.insert(InitiativeEntryEntity.Companion.createLairActionEntry())
        }
    }

    fun updateInitiative(initiativeEntry: InitiativeEntryEntity, initiative: Int) {
        viewModelScope.launch {
            dao.update(initiativeEntry.copy(initiative = initiative))
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
                _dialog.update {
                    IEncounterDialog.PickLegendaryActionDialog(entriesWithLegendaryActions)
                }
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

    fun dismissDialog() {
        _dialog.update { IEncounterDialog.None }
    }

    fun editDialogItemUpdated(entry: InitiativeEntryEntity) {
        _dialog.update {
            if (it !is IEncounterDialog.EditDialog) return
            it.copy(entry = entry)
        }
    }
}