package com.feko.generictabletoprpg.initiative

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.initiative.InitiativeEntryEntity
import com.feko.generictabletoprpg.common.alertdialog.EditAlertDialogSubViewModel
import com.feko.generictabletoprpg.common.alertdialog.IEditAlertDialogSubViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class InitiativeViewModel(private val dao: InitiativeEntryDao) : ViewModel() {

    val entries: Flow<List<InitiativeEntryEntity>>
        get() = dao.getAllSortedByInitiative()

    private val _editAlertDialog =
        EditAlertDialogSubViewModel(InitiativeEntryEntity.Empty, viewModelScope)
    val editAlertDialog: IEditAlertDialogSubViewModel<InitiativeEntryEntity> = _editAlertDialog

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
}

