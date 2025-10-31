package com.feko.generictabletoprpg.shared.features.tracker.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.edit
import com.feko.generictabletoprpg.five_e_stats
import com.feko.generictabletoprpg.shared.common.domain.createNewComparator
import com.feko.generictabletoprpg.shared.common.domain.model.IText
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringResourceText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.FabDropdownSubViewModel
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.IFabDropdownSubViewModel
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.IToastSubViewModel
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.OverviewViewModel
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.ToastSubViewModel
import com.feko.generictabletoprpg.shared.features.searchall.usecase.ISearchAllUseCase
import com.feko.generictabletoprpg.shared.features.spell.SpellDao
import com.feko.generictabletoprpg.shared.features.tracker.TrackedThingDao
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
import com.feko.generictabletoprpg.spell_already_in_list
import com.feko.generictabletoprpg.spell_cast_with_slot_level
import com.feko.generictabletoprpg.spell_successfully_added_to_list
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackerViewModel(
    private val groupId: Long,
    val groupName: String,
    private val trackedThingDao: TrackedThingDao,
    private val spellDao: SpellDao,
    searchAllUseCase: ISearchAllUseCase,
) : OverviewViewModel<Any>(trackedThingDao) {

    private val _fabDropdown = FabDropdownSubViewModel(viewModelScope)
    val fabDropdown: IFabDropdownSubViewModel = _fabDropdown

    private val _toast = ToastSubViewModel(viewModelScope)
    val toast: IToastSubViewModel = _toast

    private val _dialog: MutableStateFlow<ITrackerDialog> = MutableStateFlow(ITrackerDialog.None)
    val dialog: Flow<ITrackerDialog> = _dialog

    val spellListDialog: Flow<ITrackerDialog.SpellListDialog> =
        _dialog.filterIsInstance<ITrackerDialog.SpellListDialog>()

    private lateinit var allItems: List<Any>

    private var spellListBeingAddedTo: TrackedThing? = null
    private var fiveEDefaultStats: List<StatEntry>? = null

    lateinit var spellListState: LazyListState
    private val isShowingPreparedSpells = MutableStateFlow(false)

    override val combinedItemFlow: Flow<List<Any>> =
        _items.combine(_searchString) { items, searchString ->
            if (searchString.isBlank()) {
                items
            } else {
                _isLoadingShown.emit(true)
                val sortedItems =
                    withContext(Dispatchers.Default) {
                        (items + allItems).sortedWith(createNewComparator(searchString))
                    }
                _isLoadingShown.emit(false)
                sortedItems
            }
        }

    init {
        viewModelScope.launch {
            allItems = searchAllUseCase.getAllItems()
            isShowingPreparedSpells.collect {
                _dialog.update {
                    if (it !is ITrackerDialog.SpellListDialog) return@collect
                    it.copy(isFilteringByPreparedSpells = isShowingPreparedSpells.value)
                }
            }
        }
    }

    override suspend fun getAllItems(): List<TrackedThing> =
        trackedThingDao.getAllSortedByIndex(groupId)
            .onEach { it.serializedItem = it.getItem() }

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
                    TrackedThing.Companion.emptyOfType(type, _items.value.size, groupId)
                _dialog.emit(
                    ITrackerDialog.EditDialog(newTrackedThing, type.nameResource!!.asText())
                )
            }
            _fabDropdown.collapse()
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
            _fabDropdown.collapse()
        }
    }

    fun createOrEditTrackedThing(trackedThing: TrackedThing) {
        viewModelScope.launch {
            ensureNonEmptyName(trackedThing)
            if (trackedThing.id == 0L) {
                val id = trackedThingDao.insertOrUpdate(trackedThing)
                trackedThing.id = id
                addItem(trackedThing) {
                    if (it is TrackedThing)
                        it.index
                    else Int.MAX_VALUE
                }
            } else {
                if (trackedThing.type == TrackedThing.Type.Health) {
                    trackedThing.temporaryHp = 0
                }
                trackedThingDao.insertOrUpdate(trackedThing)
                replaceItem(trackedThing)
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
            removeItem(trackedThing)
            updateItemOrder()
        }
    }

    fun addToTrackedThing(trackedThing: TrackedThing, amount: String) {
        viewModelScope.launch {
            val copy = trackedThing.copy()
            copy.add(amount)
            trackedThingDao.insertOrUpdate(copy)
            replaceItem(copy)
        }
    }

    fun subtractFromTrackedThing(trackedThing: TrackedThing, amount: String) {
        viewModelScope.launch {
            val copy = trackedThing.copy()
            copy.subtract(amount)
            trackedThingDao.insertOrUpdate(copy)
            replaceItem(copy)
        }
    }

    fun addTemporaryHp(health: TrackedThing, amount: String) {
        viewModelScope.launch {
            val copy = health.copy()
            copy.addTemporaryHp(amount)
            trackedThingDao.insertOrUpdate(copy)
            replaceItem(copy)
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
                addItem(stats) {
                    if (it is TrackedThing)
                        it.index
                    else Int.MAX_VALUE
                }
            } else {
                replaceItem(stats)
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
            replaceItem(itemCopy)
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
        replaceItem(itemCopy)
    }

    private fun addOne(item: TrackedThing) {
        viewModelScope.launch {
            val itemCopy = item.copy()
            itemCopy.add("1")
            trackedThingDao.insertOrUpdate(itemCopy)
            replaceItem(itemCopy)
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
            _items.emit(newList)
            newList.forEach {
                trackedThingDao.insertOrUpdate(it)
            }
        }
    }

    fun showPreviewSpellListDialog(spellList: TrackedThing, resetListState: Boolean) {
        @Suppress("UNCHECKED_CAST")
        if ((spellList.serializedItem as List<SpellListEntry>).isEmpty()) {
            return
        }
        if (resetListState) {
            spellListState = LazyListState()
        }
        _dialog.update {
            ITrackerDialog.SpellListDialog(spellList, isShowingPreparedSpells.value)
        }
    }

    fun addSpellToList(spellId: Long) {
        viewModelScope.launch {
            val spellToAdd = spellDao.getById(spellId)
            val spellList = requireNotNull(spellListBeingAddedTo).copy()

            @Suppress("UNCHECKED_CAST")
            val serializedItem = spellList.serializedItem as List<SpellListEntry>
            val spellAlreadyInList =
                serializedItem.any { it.id == spellId && it.name == spellToAdd.name }
            if (spellAlreadyInList) {
                _toast.showMessage(Res.string.spell_already_in_list)
            } else {
                val sortedSpells =
                    serializedItem
                        .plus(SpellListEntry.Companion.fromSpell(spellToAdd))
                        .sortedWith { spell1, spell2 ->
                            requireNotNull(spell1)
                            requireNotNull(spell2)
                            val comparisonByLevel = spell1.level.compareTo(spell2.level)
                            when {
                                comparisonByLevel != 0 -> comparisonByLevel
                                else -> spell1.name.compareTo(spell2.name)
                            }
                        }
                spellList.setItem(sortedSpells)
                trackedThingDao.insertOrUpdate(spellList)
                replaceItem(spellList)
                val currentDialog = _dialog.value
                if (currentDialog is ITrackerDialog.SpellListDialog) {
                    _dialog.update { currentDialog.copy(spellList = spellList) }
                }
                _toast.showMessage(Res.string.spell_successfully_added_to_list)
            }
            spellListBeingAddedTo = null
        }
    }

    fun addingSpellToList(spellList: TrackedThing) {
        spellListBeingAddedTo = spellList
    }

    fun removeSpellFromSpellListRequested(spell: SpellListEntry) =
        _dialog.update {
            if (it !is ITrackerDialog.SpellListDialog) return
            it.copy(secondaryDialog = ISpellListDialogDialogs.ConfirmSpellRemovalDialog(spell))
        }

    fun removeSpellFromSpellList(spellList: TrackedThing, spellListEntry: SpellListEntry) {
        viewModelScope.launch {
            @Suppress("UNCHECKED_CAST")
            val serializedItem =
                (spellList.serializedItem as List<SpellListEntry>).minus(spellListEntry)
            val spellListCopy = spellList.copy()
            spellListCopy.setItem(serializedItem)
            trackedThingDao.insertOrUpdate(spellListCopy)
            replaceItem(spellListCopy)
            if (serializedItem.isEmpty()) {
                dismissDialog()
            } else {
                _dialog.update {
                    if (it !is ITrackerDialog.SpellListDialog) return@launch
                    it.copy(spellList = spellListCopy)
                }
            }
        }
    }

    fun castSpellRequested(level: Int) {
        viewModelScope.launch {
            val availableSpellSlots =
                _items.value
                    .filterIsInstance<TrackedThing>()
                    .filter {
                        it.type == TrackedThing.Type.SpellSlot
                                && it.level >= level
                                && it.amount.toInt() > 0
                    }
                    .map { it.level }
                    .distinct()
            require(availableSpellSlots.isNotEmpty())
            if (availableSpellSlots.size == 1) {
                castSpellImmediate(availableSpellSlots.first())
                return@launch
            }
            _dialog.update {
                if (it !is ITrackerDialog.SpellListDialog) return@launch
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
            _toast.showMessage(
                Res.string.spell_cast_with_slot_level,
                withSlotLevel.toString()
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
            _toast.showMessage(
                Res.string.spell_cast_with_slot_level,
                withSlotLevel.toString()
            )
        }
    }

    fun showStatsDialog(stats: TrackedThing) =
        _dialog.update {
            ITrackerDialog.PreviewStatSkillsDialog(stats.serializedItem as StatsContainer)
        }

    fun canCastSpell(level: Int): Boolean =
        _items.value
            .filterIsInstance<TrackedThing>()
            .any {
                it.type == TrackedThing.Type.SpellSlot
                        && it.level >= level
                        && it.amount.toInt() > 0
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
            replaceItem(spellListCopy)
            _dialog.update {
                if (it !is ITrackerDialog.SpellListDialog) return@launch
                it.copy(spellList = spellListCopy)
            }
        }
    }

    fun setShowingPreparedSpells(value: Boolean) = isShowingPreparedSpells.update { value }

    fun restoreHitDie(item: TrackedThing) = addOne(item)

    fun dismissDialog() = _dialog.update { ITrackerDialog.None }

    fun dismissSpellListSecondaryDialog() =
        _dialog.update {
            if (it !is ITrackerDialog.SpellListDialog) return
            it.copy(secondaryDialog = ISpellListDialogDialogs.None)
        }

    fun editDialogValueUpdated(trackedThing: TrackedThing) =
        _dialog.update {
            if (it !is ITrackerDialog.EditDialog) return
            it.copy(editedItem = trackedThing)
        }
}