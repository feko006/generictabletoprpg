package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.edit
import com.feko.generictabletoprpg.equipment_item_successfully_saved
import com.feko.generictabletoprpg.failed_to_create_file_shortcut
import com.feko.generictabletoprpg.file_shortcut_successfully_saved
import com.feko.generictabletoprpg.five_e_stats
import com.feko.generictabletoprpg.item_successfully_added_to_equipment
import com.feko.generictabletoprpg.shared.common.domain.createNewComparator
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.ToastMessage
import com.feko.generictabletoprpg.shared.common.ui.theme.ScreenSize
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.OverviewViewModel
import com.feko.generictabletoprpg.shared.features.searchall.usecase.ISearchAllUseCase
import com.feko.generictabletoprpg.shared.features.spell.Spell
import com.feko.generictabletoprpg.shared.features.tracker.TrackedThingDao
import com.feko.generictabletoprpg.shared.features.tracker.model.EquipmentContainer
import com.feko.generictabletoprpg.shared.features.tracker.model.EquipmentEntry
import com.feko.generictabletoprpg.shared.features.tracker.model.EquipmentItem
import com.feko.generictabletoprpg.shared.features.tracker.model.FileShortcutEntry
import com.feko.generictabletoprpg.shared.features.tracker.model.FileShortcutsContainer
import com.feko.generictabletoprpg.shared.features.tracker.model.IEquipmentItem
import com.feko.generictabletoprpg.shared.features.tracker.model.SpellListEntry
import com.feko.generictabletoprpg.shared.features.tracker.model.StatEntry
import com.feko.generictabletoprpg.shared.features.tracker.model.StatSkillEntry
import com.feko.generictabletoprpg.shared.features.tracker.model.StatsContainer
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing
import com.feko.generictabletoprpg.shared.features.tracker.model.add
import com.feko.generictabletoprpg.shared.features.tracker.model.addTemporaryHp
import com.feko.generictabletoprpg.shared.features.tracker.model.amount
import com.feko.generictabletoprpg.shared.features.tracker.model.getItem
import com.feko.generictabletoprpg.shared.features.tracker.model.resetValueToDefault
import com.feko.generictabletoprpg.shared.features.tracker.model.setItem
import com.feko.generictabletoprpg.shared.features.tracker.model.subtract
import com.feko.generictabletoprpg.shared.features.tracker.ui.ITrackerDialog.EditFileShortcutNameDialog
import com.feko.generictabletoprpg.shared.logger
import com.feko.generictabletoprpg.shared.preprocessFileShortcut
import com.feko.generictabletoprpg.spell_cast_with_slot_level
import com.feko.generictabletoprpg.spells_already_in_list
import com.feko.generictabletoprpg.spells_successfully_added_to_list
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.absolutePath
import io.github.vinceglb.filekit.dialogs.openFileWithDefaultApplication
import io.github.vinceglb.filekit.nameWithoutExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class TrackerViewModel(
    private val groupId: Long,
    val groupName: String,
    private val trackedThingDao: TrackedThingDao,
    searchAllUseCase: ISearchAllUseCase,
) : OverviewViewModel<Any>(trackedThingDao) {

    private val _fabDropdownExpanded = MutableStateFlow(false)
    val fabDropdownExpanded: StateFlow<Boolean> = _fabDropdownExpanded

    private val _toast = MutableStateFlow<ToastMessage?>(null)
    val toast: Flow<ToastMessage?> = _toast

    private val _dialog: MutableStateFlow<ITrackerDialog> = MutableStateFlow(ITrackerDialog.None)
    val dialog: Flow<ITrackerDialog> = _dialog

    private val _spellListDialog = MutableStateFlow<ITrackerDialog.SpellListDialog?>(null)
    val spellListDialog: Flow<ITrackerDialog.SpellListDialog?> = _spellListDialog

    private val _equipmentListDialog = MutableStateFlow<ITrackerDialog.EquipmentListDialog?>(null)
    val equipmentListDialog: Flow<ITrackerDialog.EquipmentListDialog?> = _equipmentListDialog

    private lateinit var allItems: List<Any>

    private var spellListBeingAddedTo: TrackedThing? = null
    private var equipmentBeingAddedTo: TrackedThing? = null
    private var fiveEDefaultStats: List<StatEntry>? = null

    lateinit var spellListState: LazyListState
    private val isShowingPreparedSpells = MutableStateFlow(false)

    val availableSpellSlotLevels =
        _items.map { list ->
            list.filterIsInstance<TrackedThing>()
                .filter { it.type == TrackedThing.Type.SpellSlot && it.amount.toInt() > 0 }
        }

    override val combinedItemFlow: Flow<List<Any>> =
        _items.combine(_searchString) { items, searchString ->
            if (searchString.isBlank()) {
                items
            } else {
                _isLoadingShown.emit(true)
                val sortedItems =
                    withContext(Dispatchers.Default) {
                        val equipmentItems =
                            items.filterIsInstance<TrackedThing>()
                                .filter { it.type == TrackedThing.Type.Equipment }
                                .flatMap {
                                    (it.serializedItem as EquipmentContainer).entries
                                        .map { entry -> entry.item }
                                        .filterIsInstance<EquipmentItem>()
                                }
                        (items + equipmentItems + allItems)
                            .sortedWith(createNewComparator(searchString))
                    }
                _isLoadingShown.emit(false)
                sortedItems
            }
        }

    init {
        viewModelScope.launch {
            allItems = searchAllUseCase.getAllItems().first()
            isShowingPreparedSpells.collect {
                updateFlexDialogState(_spellListDialog) {
                    it.copy(isFilteringByPreparedSpells = isShowingPreparedSpells.value)
                }
            }
        }
    }

    override fun getAllItems(): Flow<List<Any>> =
        trackedThingDao.getAllSortedByIndex(groupId)
            .onEach { list ->
                list.onEach {
                    if (it.serializedItem == 0) {
                        it.serializedItem = it.getItem()
                    }
                }
            }

    fun showCreateDialog(type: TrackedThing.Type) {
        viewModelScope.launch {
            if (type == TrackedThing.Type.FiveEStats) {
                fiveEDefaultStats = fiveEDefaultStats
                    ?: StatsContainer.createDefault5EStatEntries()
                val defaultStats = requireNotNull(fiveEDefaultStats)
                val newStats = TrackedThing.emptyOfType(type, _items.value.size, groupId)
                newStats.setItem(StatsContainer.Empty.copy(stats = defaultStats))
                _dialog.emit(
                    ITrackerDialog.StatsEditDialog(newStats, Res.string.five_e_stats.asText())
                )
            } else {
                val newTrackedThing =
                    TrackedThing.emptyOfType(type, _items.value.size, groupId)
                _dialog.emit(
                    ITrackerDialog.EditDialog(newTrackedThing, type.nameResource!!.asText())
                )
            }
            dismissFabDropdown()
        }
    }

    fun showEditDialog(item: TrackedThing) {
        viewModelScope.launch {
            val copy = item.copy()
            if (item.type == TrackedThing.Type.FiveEStats) {
                _dialog.emit(
                    ITrackerDialog.StatsEditDialog(copy, IText.StringResourceText(Res.string.edit))
                )
            } else {
                _dialog.emit(
                    ITrackerDialog.EditDialog(copy, IText.StringResourceText(Res.string.edit))
                )
            }
            dismissFabDropdown()
        }
    }

    fun createOrEditTrackedThing(trackedThing: TrackedThing) {
        viewModelScope.launch {
            ensureNonEmptyName(trackedThing)
            if (trackedThing.id == 0L) {
                val id = trackedThingDao.insertOrUpdate(trackedThing)
                trackedThing.id = id
                scrollToEnd()
            } else {
                if (trackedThing.type == TrackedThing.Type.Health) {
                    trackedThing.temporaryHp = 0
                }
                trackedThingDao.insertOrUpdate(trackedThing)
            }
        }
    }

    private fun ensureNonEmptyName(trackedThing: TrackedThing) {
        if (trackedThing.name.isBlank()) {
            trackedThing.name = groupName
        }
    }

    fun deleteTrackedThing(trackedThing: TrackedThing) {
        viewModelScope.launch {
            trackedThingDao.delete(trackedThing.id)
            updateItemOrder()
            val spellListId = _spellListDialog.value?.spellList?.id
            if (spellListId == trackedThing.id) {
                _spellListDialog.emit(null)
            }
            val equipmentId = _equipmentListDialog.value?.equipment?.id
            if (equipmentId == trackedThing.id) {
                _equipmentListDialog.emit(null)
            }
        }
    }

    fun addToTrackedThing(trackedThing: TrackedThing, amount: String) {
        viewModelScope.launch {
            val copy = trackedThing.copy()
            copy.add(amount)
            trackedThingDao.insertOrUpdate(copy)
        }
    }

    fun subtractFromTrackedThing(trackedThing: TrackedThing, amount: String) {
        viewModelScope.launch {
            val copy = trackedThing.copy()
            copy.subtract(amount)
            trackedThingDao.insertOrUpdate(copy)
        }
    }

    fun addTemporaryHp(health: TrackedThing, amount: String) {
        viewModelScope.launch {
            val copy = health.copy()
            copy.addTemporaryHp(amount)
            trackedThingDao.insertOrUpdate(copy)
        }
    }

    fun refreshAll() {
        viewModelScope.launch {
            _items.value
                .filterIsInstance<TrackedThing>()
                .forEach {
                    resetValueToDefault(it)
                }
        }
    }

    fun insertOrUpdateStats(stats: TrackedThing) {
        viewModelScope.launch {
            val updatedStatsContainer =
                (stats.serializedItem as StatsContainer).finalizeStats()
            stats.setItem(updatedStatsContainer)
            val isNewItem = stats.id <= 0
            ensureNonEmptyName(stats)
            val id = trackedThingDao.insertOrUpdate(stats)
            stats.id = id
            if (isNewItem) {
                scrollToEnd()
            }
        }
    }

    fun editStatsDialogValueUpdated(stats: TrackedThing) {
        viewModelScope.launch {
            _dialog.update {
                if (it !is ITrackerDialog.StatsEditDialog) return@launch
                stats.setItem(stats.serializedItem)
                it.copy(stats = stats)
            }
        }
    }

    private fun StatsContainer.finalizeStats(): StatsContainer {
        val stats = stats
            .map { stat ->
                val skills = stat.skills.map { skill ->
                    val bonus =
                        getBonus(proficiencyBonus, stat, skill)
                    val passiveScore =
                        getPassiveScore(bonus, stat, skill)
                    skill.copy(
                        bonus = bonus,
                        passiveScore = passiveScore
                    )
                }
                val savingThrowBonus =
                    getSavingThrowBonus(proficiencyBonus, stat)
                stat.copy(
                    savingThrowBonus = savingThrowBonus,
                    skills = skills
                )
            }
        val initiative = getInitiative(stats)
        val spellSaveDc = getSpellSaveDc(stats)
        val spellAttackBonus = getSpellAttackBonus(stats)
        return copy(
            spellSaveDc = spellSaveDc,
            spellAttackBonus = spellAttackBonus,
            initiative = initiative,
            stats = stats
        )
    }

    private fun StatsContainer.getInitiative(stats: List<StatEntry>): Int {
        var initiative = initiative
        if (use5eCalculations) {
            val dexterityStat = stats.first { it.shortName.lowercase() == "dex" }
            initiative = dexterityStat.bonus + initiativeAdditionalBonus
        }
        return initiative
    }

    private fun getSavingThrowBonus(proficiencyBonus: Int, stat: StatEntry): Int {
        var savingThrowBonus = stat.savingThrowBonus
        if (stat.use5ESkillBonusCalculation) {
            savingThrowBonus = stat.bonus +
                    stat.savingThrowAdditionalBonus +
                    if (stat.isProficientInSavingThrow) proficiencyBonus else 0
        }
        return savingThrowBonus
    }

    private fun getBonus(
        proficiencyBonus: Int,
        stat: StatEntry,
        skill: StatSkillEntry
    ): Int {
        var bonus = skill.bonus
        if (stat.use5ESkillBonusCalculation) {
            bonus = stat.bonus + skill.additionalBonus
            if (skill.hasExpertise) {
                bonus += proficiencyBonus * 2
            } else if (skill.isProficient) {
                bonus += proficiencyBonus
            }
        }
        return bonus
    }

    private fun getPassiveScore(
        bonus: Int,
        stat: StatEntry,
        skill: StatSkillEntry
    ): Int {
        var passiveScore = skill.passiveScore
        if (stat.use5ESkillBonusCalculation) {
            passiveScore = 10 + bonus
        }
        return passiveScore
    }

    private fun StatsContainer.getSpellSaveDc(stats: List<StatEntry>): Int {
        var spellSaveDc = spellSaveDc
        if (use5eCalculations) {
            val spellcastingModifierStat =
                stats.firstOrNull { stat -> stat.isSpellcastingModifier }
            spellSaveDc =
                8 + (spellcastingModifierStat?.bonus
                    ?: 0) + spellSaveDcAdditionalBonus + proficiencyBonus
        }
        return spellSaveDc
    }

    private fun StatsContainer.getSpellAttackBonus(stats: List<StatEntry>): Int {
        var spellAttackBonus = spellAttackBonus
        if (use5eCalculations) {
            val spellcastingModifierStat =
                stats.firstOrNull { stat -> stat.isSpellcastingModifier }
            spellAttackBonus = (spellcastingModifierStat?.bonus ?: 0) +
                    spellAttackAdditionalBonus + proficiencyBonus
        }
        return spellAttackBonus
    }

    fun resetValueToDefault(item: TrackedThing) {
        viewModelScope.launch {
            val itemCopy = item.copy()
            itemCopy.resetValueToDefault()
            if (itemCopy.type == TrackedThing.Type.Health) {
                itemCopy.temporaryHp = 0
            }
            trackedThingDao.insertOrUpdate(itemCopy)
        }
    }

    fun reduceByOne(item: TrackedThing) {
        viewModelScope.launch {
            reduceByOneSuspending(item)
        }
    }

    private suspend fun reduceByOneSuspending(item: TrackedThing) {
        val itemCopy = item.copy()
        itemCopy.subtract("1")
        trackedThingDao.insertOrUpdate(itemCopy)
    }

    fun addOne(item: TrackedThing) {
        viewModelScope.launch {
            val itemCopy = item.copy()
            itemCopy.add("1")
            trackedThingDao.insertOrUpdate(itemCopy)
        }
    }

    fun addToPercentageRequested(percentage: TrackedThing) =
        _dialog.update { ITrackerDialog.AddToPercentageDialog(percentage) }

    fun subtractFromPercentageRequested(percentage: TrackedThing) =
        _dialog.update { ITrackerDialog.SubtractFromPercentageDialog(percentage) }

    fun addToNumberRequested(number: TrackedThing) =
        _dialog.update { ITrackerDialog.AddToNumberDialog(number) }

    fun subtractFromNumberRequested(number: TrackedThing) =
        _dialog.update { ITrackerDialog.SubtractFromNumberDialog(number) }

    fun takeDamageRequested(health: TrackedThing) =
        _dialog.update { ITrackerDialog.DamageHealthDialog(health) }

    fun healRequested(health: TrackedThing) =
        _dialog.update { ITrackerDialog.HealHealthDialog(health) }

    fun addTemporaryHpRequested(health: TrackedThing) =
        _dialog.update { ITrackerDialog.AddTemporaryHpDialog(health) }

    fun deleteItemRequested(item: TrackedThing) =
        _dialog.update { ITrackerDialog.ConfirmDeletionDialog(item) }

    fun refreshAllRequested() =
        _dialog.update { ITrackerDialog.RefreshAllDialog() }

    fun itemReordered(from: Int, to: Int) {
        val itemCount = _items.value.size
        if (from == itemCount || to == itemCount) return

        updateItemOrder { add(to, removeAt(from)) }
    }

    private fun updateItemOrder(transformList: (MutableList<TrackedThing>.() -> Unit)? = null) {
        viewModelScope.launch {
            val newList =
                _items
                    .value
                    .filterIsInstance<TrackedThing>()
                    .toMutableList()
                    .apply {
                        transformList?.invoke(this)
                        forEachIndexed { index, item ->
                            item.index = index
                        }
                    }
            trackedThingDao.updateAll(newList)
        }
    }

    fun showPreviewSpellListDialog(
        spellList: TrackedThing,
        resetListState: Boolean,
        screenSize: ScreenSize,
        onNavigateToSpellListScreen: () -> Unit
    ) {
        @Suppress("UNCHECKED_CAST")
        if ((spellList.serializedItem as List<SpellListEntry>).isEmpty()) {
            return
        }
        if (resetListState) {
            spellListState = LazyListState()
        }
        val spellListDialog =
            ITrackerDialog.SpellListDialog(spellList, isShowingPreparedSpells.value)
        viewModelScope.launch {
            _dialog.emit(spellListDialog)
            _spellListDialog.emit(spellListDialog)
            if (screenSize != ScreenSize.Compact) {
                onNavigateToSpellListScreen()
            }
        }
    }

    fun addSpellsToList(spells: List<Spell>) {
        viewModelScope.launch {
            val spellList = requireNotNull(spellListBeingAddedTo).copy()

            @Suppress("UNCHECKED_CAST")
            val serializedItem = spellList.serializedItem as List<SpellListEntry>
            val spellsNotInList = spells.filter { newSpell ->
                serializedItem.all {
                    it.id != newSpell.id && it.name != newSpell.name
                }
            }
            val spellsAlreadyInList = spellsNotInList.isEmpty()
            if (spellsAlreadyInList) {
                _toast.emit(ToastMessage(Res.string.spells_already_in_list.asText(), _toast))
            } else {
                val newSpells = spellsNotInList.map { SpellListEntry.fromSpell(it) }
                val sortedSpells =
                    serializedItem
                        .plus(newSpells)
                        .sortedWith { spell1, spell2 ->
                            val comparisonByLevel = spell1.level.compareTo(spell2.level)
                            when {
                                comparisonByLevel != 0 -> comparisonByLevel
                                else -> spell1.name.compareTo(spell2.name)
                            }
                        }
                spellList.setItem(sortedSpells)
                trackedThingDao.insertOrUpdate(spellList)
                updateFlexDialogState(_spellListDialog) { it.copy(spellList = spellList) }
                _toast.emit(
                    ToastMessage(Res.string.spells_successfully_added_to_list.asText(), _toast)
                )
            }
            spellListBeingAddedTo = null
        }
    }

    fun addingSpellToList(spellList: TrackedThing) {
        spellListBeingAddedTo = spellList
    }

    fun removeSpellFromSpellListRequested(spell: SpellListEntry) =
        updateFlexDialogState(_spellListDialog) {
            it.copy(secondaryDialog = ISpellListDialogDialogs.ConfirmSpellRemovalDialog(spell))
        }

    fun removeSpellFromSpellList(
        spellList: TrackedThing,
        spellListEntry: SpellListEntry,
        onPopSpellListScreen: () -> Unit
    ) {
        viewModelScope.launch {
            @Suppress("UNCHECKED_CAST")
            val serializedItem =
                (spellList.serializedItem as List<SpellListEntry>).minus(spellListEntry)
            val spellListCopy = spellList.copy()
            spellListCopy.setItem(serializedItem)
            trackedThingDao.insertOrUpdate(spellListCopy)
            if (serializedItem.isEmpty()) {
                onPopSpellListScreen()
                dismissDialog()
            } else {
                updateFlexDialogState(_spellListDialog) { it.copy(spellList = spellListCopy) }
            }
        }
    }

    fun castSpellRequested(level: Int) {
        viewModelScope.launch {
            val availableSpellSlots =
                availableSpellSlotLevels
                    .first()
                    .filter { it.level >= level }
                    .map { it.level }
                    .distinct()
            require(availableSpellSlots.isNotEmpty())
            if (availableSpellSlots.size == 1) {
                castSpellImmediate(availableSpellSlots.first())
                return@launch
            }
            updateFlexDialogState(_spellListDialog) {
                it.copy(
                    secondaryDialog =
                        ISpellListDialogDialogs.SelectSpellSlotDialog(availableSpellSlots)
                )
            }
        }
    }

    private fun castSpellImmediate(withSlotLevel: Int) {
        viewModelScope.launch {
            val spellSlot =
                _items.value
                    .filterIsInstance<TrackedThing>()
                    .first {
                        it.type == TrackedThing.Type.SpellSlot
                                && it.level == withSlotLevel
                                && it.amount.toInt() > 0
                    }
            reduceByOneSuspending(spellSlot)
            _toast.emit(
                ToastMessage(
                    Res.string.spell_cast_with_slot_level.asText(arrayOf(withSlotLevel.toString())),
                    _toast
                )
            )
        }
    }

    fun castSpell(withSlotLevel: Int) {
        viewModelScope.launch {
            val spellSlot =
                _items.value
                    .filterIsInstance<TrackedThing>()
                    .first {
                        it.type == TrackedThing.Type.SpellSlot
                                && it.level == withSlotLevel
                                && it.amount.toInt() > 0
                    }
            reduceByOneSuspending(spellSlot)
            _toast.emit(
                ToastMessage(
                    Res.string.spell_cast_with_slot_level.asText(arrayOf(withSlotLevel.toString())),
                    _toast
                )
            )
        }
    }

    fun showStatsDialog(stats: TrackedThing) =
        _dialog.update {
            ITrackerDialog.PreviewStatSkillsDialog(stats.serializedItem as StatsContainer)
        }

    fun changeSpellListEntryPreparedState(
        spellList: TrackedThing,
        spellListEntry: SpellListEntry,
        isPrepared: Boolean
    ) {
        viewModelScope.launch {
            val spellListCopy = spellList.copy()
            spellListEntry.isPrepared = isPrepared
            spellListCopy.setItem(spellListCopy.serializedItem)
            trackedThingDao.insertOrUpdate(spellListCopy)
            updateFlexDialogState(_spellListDialog) { it.copy(spellList = spellListCopy) }
        }
    }

    fun setShowingPreparedSpells(value: Boolean) = isShowingPreparedSpells.update { value }

    fun dismissDialog() = _dialog.update { ITrackerDialog.None }

    fun dismissSpellListSecondaryDialog() =
        updateFlexDialogState(_spellListDialog) { it.copy(secondaryDialog = ISpellListDialogDialogs.None) }

    fun editDialogValueUpdated(trackedThing: TrackedThing) =
        _dialog.update {
            if (it is ITrackerDialog.EditDialog)
                it.copy(editedItem = trackedThing)
            else it
        }

    fun dismissFabDropdown() {
        _fabDropdownExpanded.update { false }
    }

    fun toggleFabDropdown() {
        _fabDropdownExpanded.update { !it }
    }

    fun addingItemToEquipment(equipment: TrackedThing) {
        equipmentBeingAddedTo = equipment
    }

    fun addItemsToEquipment(items: List<IEquipmentItem>) {
        viewModelScope.launch {
            val equipment = requireNotNull(equipmentBeingAddedTo).copy()
            if (items.isEmpty()) {
                equipmentBeingAddedTo = null
                return@launch
            }

            val serializedItem = equipment.serializedItem as EquipmentContainer
            var newEntries = serializedItem.entries

            items.forEach { item ->
                val itemAlreadyInEquipment = serializedItem.entries.firstOrNull { entry ->
                    entry.item::class == item::class && entry.item.name == item.name
                }
                if (itemAlreadyInEquipment != null) {
                    val newItem =
                        itemAlreadyInEquipment.copy(count = itemAlreadyInEquipment.count + 1)
                    newEntries = newEntries.minus(itemAlreadyInEquipment).plus(newItem)
                } else {
                    val newItem = EquipmentEntry(item)
                    newEntries = newEntries.plus(newItem)
                }
            }
            newEntries = newEntries.sortedBy { it.item.name }
            val newSerializedItem = serializedItem.copy(entries = newEntries)
            equipment.setItem(newSerializedItem)
            trackedThingDao.insertOrUpdate(equipment)
            updateFlexDialogState(_equipmentListDialog) { it.copy(equipment = equipment) }
            _toast.emit(
                ToastMessage(Res.string.item_successfully_added_to_equipment.asText(), _toast)
            )
            equipmentBeingAddedTo = null
        }
    }

    fun showEquipmentListDialog(
        equipment: TrackedThing,
        screenSize: ScreenSize,
        onNavigateToEquipmentListScreen: () -> Unit
    ) {
        @Suppress("UNCHECKED_CAST")
        if ((equipment.serializedItem as EquipmentContainer).entries.isEmpty()) {
            return
        }
        val equipmentListDialog =
            ITrackerDialog.EquipmentListDialog(equipment)
        _dialog.update { equipmentListDialog }
        _equipmentListDialog.update { equipmentListDialog }
        if (screenSize != ScreenSize.Compact) {
            onNavigateToEquipmentListScreen()
        }
    }

    fun removeItemFromEquipmentListRequested(equipment: EquipmentEntry) =
        updateFlexDialogState(_equipmentListDialog) {
            it.copy(secondaryDialog = IEquipmentListDialogDialogs.ConfirmItemRemovalDialog(equipment))
        }

    fun removeEquipmentItemFromList(
        equipment: TrackedThing,
        equipmentItem: EquipmentEntry,
        onPopEquipmentListScreen: () -> Unit = {}
    ) {
        viewModelScope.launch {
            @Suppress("UNCHECKED_CAST")
            val serializedItem = (equipment.serializedItem as EquipmentContainer)
                .run { copy(entries = entries.minus(equipmentItem)) }
            val equipmentListCopy = equipment.copy()
            equipmentListCopy.setItem(serializedItem)
            trackedThingDao.insertOrUpdate(equipmentListCopy)
            if (serializedItem.entries.isEmpty()) {
                onPopEquipmentListScreen()
                dismissDialog()
            } else {
                updateFlexDialogState(_equipmentListDialog) {
                    it.copy(equipment = equipmentListCopy)
                }
            }
        }
    }

    fun setItemQuantityRequested(equipmentEntry: EquipmentEntry) =
        updateFlexDialogState(_equipmentListDialog) {
            it.copy(
                secondaryDialog =
                    IEquipmentListDialogDialogs.SetItemQuantityDialog(equipmentEntry)
            )
        }

    fun setItemQuantity(equipment: TrackedThing, equipmentEntry: EquipmentEntry, value: String) {
        viewModelScope.launch {
            @Suppress("UNCHECKED_CAST")
            val serializedItem = (equipment.serializedItem as EquipmentContainer)
                .run {
                    copy(entries = entries.map {
                        if (it.item.name == equipmentEntry.item.name) {
                            val quantity = (value.toIntOrNull() ?: 1).coerceAtLeast(1)
                            it.copy(count = quantity)
                        } else it
                    })
                }
            val equipmentListCopy = equipment.copy()
            equipmentListCopy.setItem(serializedItem)
            trackedThingDao.insertOrUpdate(equipmentListCopy)
            updateFlexDialogState(_equipmentListDialog) {
                it.copy(
                    equipment = equipmentListCopy,
                    secondaryDialog = IEquipmentListDialogDialogs.None
                )
            }
        }
    }

    fun dismissEquipmentListSecondaryDialog() =
        updateFlexDialogState(_equipmentListDialog) {
            it.copy(secondaryDialog = IEquipmentListDialogDialogs.None)
        }

    fun showEditEquipmentItemDialog(
        equipment: TrackedThing,
        equipmentItem: IEquipmentItem? = null
    ) {
        addingItemToEquipment(equipment)
        val item = (equipmentItem as? EquipmentItem) ?: EquipmentItem.empty()
        updateFlexDialogState(_equipmentListDialog) {
            val editEquipmentItemDialog = ITrackerDialog.EditEquipmentItemDialog(item)
            it.copy(secondaryDialog = editEquipmentItemDialog)
        }
    }

    fun createOrEditEquipmentItem(equipmentItem: EquipmentItem) {
        viewModelScope.launch {
            val equipmentList = requireNotNull(equipmentBeingAddedTo).copy()

            @Suppress("UNCHECKED_CAST")
            var serializedItem = equipmentList.serializedItem as EquipmentContainer
            val equipmentAlreadyInList =
                serializedItem.entries.any {
                    it.item is EquipmentItem && it.item.id == equipmentItem.id
                }
            if (equipmentAlreadyInList) {
                serializedItem = serializedItem.run {
                    copy(
                        entries =
                            entries.map {
                                if (it.item !is EquipmentItem || it.item.id != equipmentItem.id) it
                                else {
                                    it.copy(item = equipmentItem)
                                }
                            })
                }
            } else {
                serializedItem = serializedItem.run {
                    copy(
                        entries =
                            entries.plus(EquipmentEntry(equipmentItem))
                                .sortedBy { it.item.name }
                    )
                }
            }
            equipmentList.setItem(serializedItem)
            trackedThingDao.insertOrUpdate(equipmentList)
            updateFlexDialogState(_equipmentListDialog) {
                it.copy(
                    equipment = equipmentList,
                    secondaryDialog = IEquipmentListDialogDialogs.None
                )
            }
            _toast.emit(
                ToastMessage(Res.string.equipment_item_successfully_saved.asText(), _toast)
            )
            equipmentBeingAddedTo = null
        }
    }

    private inline fun <reified T : ITrackerDialog> updateFlexDialogState(
        flexDialogProperty: MutableStateFlow<T?>,
        transform: (T) -> T
    ) {
        flexDialogProperty.update { dialog -> dialog?.let { transform(it) } }
        val currentDialog = _dialog.value
        if (currentDialog is T) {
            _dialog.update { transform(currentDialog) }
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun addFileToShortcuts(
        fileShortcuts: TrackedThing,
        file: PlatformFile
    ) {
        try {
            val newFileShortcut =
                FileShortcutEntry(
                    Uuid.random().toHexDashString(),
                    file.nameWithoutExtension,
                    preprocessFileShortcut(file.absolutePath())
                )
            _dialog.update {
                EditFileShortcutNameDialog(fileShortcuts, newFileShortcut)
            }
        } catch (throwable: Throwable) {
            logger.error(throwable) { "Failed to add file to shortcuts." }
            _toast.update {
                ToastMessage(Res.string.failed_to_create_file_shortcut.asText(), _toast)
            }
        }
    }

    fun showChangeFileShortcutNameSecondaryDialog(
        fileShortcut: FileShortcutEntry
    ) {
        _dialog.update {
            if (it is ITrackerDialog.FileShortcutsDialog) {
                it.copy(
                    secondaryDialog =
                        EditFileShortcutNameDialog(it.fileShortcuts, fileShortcut)
                )
            } else it
        }
    }

    fun createOrEditFileShortcut(fileShortcuts: TrackedThing, fileShortcut: FileShortcutEntry) {
        viewModelScope.launch {
            val serializedItem = fileShortcuts.serializedItem as FileShortcutsContainer
            val existingEntry = serializedItem.entries.firstOrNull { it.id == fileShortcut.id }
            var newEntries = serializedItem.entries
            if (existingEntry != null) {
                newEntries = newEntries.minus(existingEntry)
            }
            newEntries = newEntries.plus(fileShortcut).sortedBy { it.name }
            fileShortcuts.setItem(serializedItem.copy(entries = newEntries))
            trackedThingDao.insertOrUpdate(fileShortcuts)
            _toast.emit(ToastMessage(Res.string.file_shortcut_successfully_saved.asText(), _toast))
        }
    }

    fun showFileShortcutsDialog(fileShortcuts: TrackedThing) {
        @Suppress("UNCHECKED_CAST")
        if ((fileShortcuts.serializedItem as FileShortcutsContainer).entries.isEmpty()) {
            return
        }
        _dialog.update { ITrackerDialog.FileShortcutsDialog(fileShortcuts) }
    }

    fun removeFileShortcutRequested(fileShortcutEntry: FileShortcutEntry) {
        _dialog.update {
            if (it is ITrackerDialog.FileShortcutsDialog) {
                it.copy(
                    secondaryDialog =
                        IFileShortcutDialogs.ConfirmItemRemovalDialog(fileShortcutEntry)
                )
            } else it
        }
    }

    fun removeFileShortcut(fileShortcuts: TrackedThing, fileShortcut: FileShortcutEntry) {
        viewModelScope.launch {
            @Suppress("UNCHECKED_CAST")
            val serializedItem = (fileShortcuts.serializedItem as FileShortcutsContainer)
                .run { copy(entries = entries.minus(fileShortcut)) }
            val fileShortcutsCopy = fileShortcuts.copy()
            fileShortcutsCopy.setItem(serializedItem)
            trackedThingDao.insertOrUpdate(fileShortcutsCopy)
            if (serializedItem.entries.isEmpty()) {
                dismissDialog()
            } else {
                _dialog.update {
                    if (it is ITrackerDialog.FileShortcutsDialog) {
                        it.copy(
                            fileShortcuts = fileShortcutsCopy,
                            secondaryDialog = IFileShortcutDialogs.None
                        )
                    } else it
                }
            }
        }
    }

    fun dismissFileShortcutSecondaryDialog() {
        _dialog.update {
            if (it is ITrackerDialog.FileShortcutsDialog) {
                it.copy(secondaryDialog = IFileShortcutDialogs.None)
            } else it
        }
    }

    fun openFileShortcut(fileShortcut: FileShortcutEntry) {
        try {
            val file = PlatformFile(fileShortcut.path)
            FileKit.openFileWithDefaultApplication(file)
        } catch (throwable: Throwable) {
            logger.error(throwable) { "Unable to open file shortcut." }
            _dialog.update {
                if (it is ITrackerDialog.FileShortcutsDialog) {
                    it.copy(
                        secondaryDialog =
                            IFileShortcutDialogs.BrokenFileShortcutDialog(fileShortcut)
                    )
                } else it
            }
        }
    }

    fun resolveBrokenFileShortcutDialog(file: PlatformFile) {
        viewModelScope.launch {
            val fileShortcutDialog = _dialog.value as ITrackerDialog.FileShortcutsDialog
            val brokenFileShortcutDialog =
                fileShortcutDialog.secondaryDialog as IFileShortcutDialogs.BrokenFileShortcutDialog
            createOrEditFileShortcut(
                fileShortcutDialog.fileShortcuts,
                brokenFileShortcutDialog.fileShortcut.copy(path = preprocessFileShortcut(file.absolutePath()))
            )
            dismissFileShortcutSecondaryDialog()
        }
    }
}