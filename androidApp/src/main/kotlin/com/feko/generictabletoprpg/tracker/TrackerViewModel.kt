package com.feko.generictabletoprpg.tracker

import android.content.Context
import androidx.compose.foundation.lazy.LazyListState
import androidx.lifecycle.viewModelScope
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.IText
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker.dialogs.ISpellListDialogDialogs
import com.feko.generictabletoprpg.common.INamed
import com.feko.generictabletoprpg.common.OverviewViewModel
import com.feko.generictabletoprpg.common.SmartNamedSearchComparator
import com.feko.generictabletoprpg.common.fabdropdown.FabDropdownSubViewModel
import com.feko.generictabletoprpg.common.fabdropdown.IFabDropdownSubViewModel
import com.feko.generictabletoprpg.common.toast.IToastSubViewModel
import com.feko.generictabletoprpg.common.toast.ToastSubViewModel
import com.feko.generictabletoprpg.import.IJson
import com.feko.generictabletoprpg.searchall.ISearchAllUseCase
import com.feko.generictabletoprpg.spell.SpellDao
import com.feko.generictabletoprpg.tracker.dialogs.ITrackerDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TrackerViewModel(
    private val groupId: Long,
    val groupName: String,
    private val trackedThingDao: TrackedThingDao,
    private val spellDao: SpellDao,
    private val json: IJson,
    searchAllUseCase: ISearchAllUseCase
) : OverviewViewModel<Any>(trackedThingDao) {

    private val _fabDropdown = FabDropdownSubViewModel(viewModelScope)
    val fabDropdown: IFabDropdownSubViewModel = _fabDropdown

    private val _toast = ToastSubViewModel(viewModelScope)
    val toast: IToastSubViewModel = _toast

    private val _dialog: MutableStateFlow<ITrackerDialog> = MutableStateFlow(ITrackerDialog.None)
    val dialog: Flow<ITrackerDialog> = _dialog

    private lateinit var allItems: List<Any>

    private var spellListBeingAddedTo: SpellList? = null
    private var fiveEDefaultStats: List<StatEntry>? = null

    lateinit var spellListState: LazyListState
    private val isShowingPreparedSpells = MutableStateFlow(false)

    override val combinedItemFlow: Flow<List<Any>> =
        _items.combine(_searchString) { items, searchString ->
            if (searchString.isBlank()) {
                items
            } else {
                (items + allItems)
                    .filter { item ->
                        item is INamed
                                && item.name.lowercase().contains(searchString.lowercase())
                    }
                    .sortedWith(SmartNamedSearchComparator(searchString))
            }
        }

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                allItems = searchAllUseCase.getAllItems()
                isShowingPreparedSpells.collect {
                    _dialog.update {
                        if (it !is ITrackerDialog.SpellListDialog) return@collect
                        it.copy(isFilteringByPreparedSpells = isShowingPreparedSpells.value)
                    }
                }
            }
        }
    }

    override fun getAllItems(): List<TrackedThing> {
        return trackedThingDao.getAllSortedByIndex(groupId)
            .onEach {
                if (it is JsonTrackedThing<*>) {
                    it.setItemFromValue(json)
                }
            }
    }

    fun showCreateDialog(type: TrackedThing.Type, context: Context) {
        viewModelScope.launch {
            if (type == TrackedThing.Type.FiveEStats) {
                fiveEDefaultStats = fiveEDefaultStats ?: createDefault5EStatEntries(context)
                val defaultStats = requireNotNull(fiveEDefaultStats)
                val newStats =
                    TrackedThing.emptyOfType(type, _items.value.size, groupId) as Stats
                val defaultStatsContainer =
                    newStats.serializedItem.copy(stats = defaultStats)
                newStats.setItem(defaultStatsContainer, json)
                _dialog.emit(
                    ITrackerDialog.StatsEditDialog(
                        newStats,
                        IText.StringResourceText(R.string.five_e_stats)
                    )
                )
            } else {
                val newTrackedThing =
                    TrackedThing.emptyOfType(type, _items.value.size, groupId)
                _dialog.emit(
                    ITrackerDialog.EditDialog(
                        newTrackedThing,
                        IText.StringResourceText(type.nameResource)
                    )
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
                    ITrackerDialog.StatsEditDialog(
                        copy as Stats,
                        IText.StringResourceText(R.string.edit)
                    )
                )
            } else {
                _dialog.emit(
                    ITrackerDialog.EditDialog(copy, IText.StringResourceText(R.string.edit))
                )
            }
            _fabDropdown.collapse()
        }
    }

    fun createOrEditTrackedThing(trackedThing: TrackedThing) {
        viewModelScope.launch {
            ensureNonEmptyName(trackedThing)
            if (trackedThing.id == 0L) {
                withContext(Dispatchers.IO) {
                    val id = trackedThingDao.insertOrUpdate(trackedThing)
                    trackedThing.id = id
                }
                addItem(trackedThing) {
                    if (it is TrackedThing)
                        it.index
                    else Int.MAX_VALUE
                }
            } else {
                if (trackedThing is Health) {
                    trackedThing.temporaryHp = 0
                }
                withContext(Dispatchers.IO) {
                    trackedThingDao.insertOrUpdate(trackedThing)
                }
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
            withContext(Dispatchers.IO) {
                trackedThingDao.delete(trackedThing.id)
            }
            removeItem(trackedThing)
        }
    }

    fun addToPercentage(percentage: Percentage, amount: String) {
        addToTrackedThing(percentage, amount)
    }

    fun subtractFromPercentage(percentage: Percentage, amount: String) {
        subtractFromTrackedThing(percentage, amount)
    }

    fun addToNumber(number: Number, amount: String) {
        addToTrackedThing(number, amount)
    }

    fun subtractFromNumber(number: Number, amount: String) {
        subtractFromTrackedThing(number, amount)
    }

    fun healHealth(health: Health, amount: String) {
        addToTrackedThing(health, amount)
    }

    fun damageHealth(health: Health, amount: String) {
        subtractFromTrackedThing(health, amount)
    }

    private fun addToTrackedThing(trackedThing: TrackedThing, amount: String) {
        viewModelScope.launch {
            trackedThing.add(amount)
            withContext(Dispatchers.IO) {
                trackedThingDao.insertOrUpdate(trackedThing)
            }
            replaceItem(trackedThing.copy())
        }
    }

    private fun subtractFromTrackedThing(trackedThing: TrackedThing, amount: String) {
        viewModelScope.launch {
            trackedThing.subtract(amount)
            withContext(Dispatchers.IO) {
                trackedThingDao.insertOrUpdate(trackedThing)
            }
            replaceItem(trackedThing.copy())
        }
    }

    fun addTemporaryHp(health: Health, amount: String) {
        viewModelScope.launch {
            health.addTemporaryHp(amount)
            withContext(Dispatchers.IO) {
                trackedThingDao.insertOrUpdate(health)
            }
            replaceItem(health.copy())
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

    fun insertOrUpdateStats(stats: Stats) {
        viewModelScope.launch {
            val updatedStatsContainer = stats.serializedItem.let(::finalizeStats)
            stats.setItem(updatedStatsContainer, json)
            val isNewItem = stats.id <= 0
            withContext(Dispatchers.IO) {
                ensureNonEmptyName(stats)
                val id = trackedThingDao.insertOrUpdate(stats)
                stats.id = id
            }
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

    fun editStatsDialogValueUpdated(stats: Stats) {
        viewModelScope.launch {
            _dialog.update {
                if (it !is ITrackerDialog.StatsEditDialog) return@launch
                stats.setItem(stats.serializedItem, json)
                it.copy(stats = stats)
            }
        }
    }

    private fun finalizeStats(statsContainer: StatsContainer): StatsContainer {
        val stats =
            statsContainer.stats
                .map { stat ->
                    val skills = stat.skills.map { skill ->
                        val bonus =
                            getBonus(statsContainer.proficiencyBonus, stat, skill)
                        val passiveScore =
                            getPassiveScore(bonus, stat, skill)
                        skill.copy(
                            bonus = bonus,
                            passiveScore = passiveScore
                        )
                    }
                    val savingThrowBonus =
                        getSavingThrowBonus(statsContainer.proficiencyBonus, stat)
                    stat.copy(
                        savingThrowBonus = savingThrowBonus,
                        skills = skills
                    )
                }
        val initiative = getInitiative(statsContainer, stats)
        val spellSaveDc = getSpellSaveDc(statsContainer, stats)
        val spellAttackBonus = getSpellAttackBonus(statsContainer, stats)
        return statsContainer.copy(
            spellSaveDc = spellSaveDc,
            spellAttackBonus = spellAttackBonus,
            initiative = initiative,
            stats = stats
        )
    }

    private fun getInitiative(statsContainer: StatsContainer, stats: List<StatEntry>): Int {
        var initiative = statsContainer.initiative
        if (statsContainer.use5eCalculations) {
            val dexterityStat = stats.first { it.shortName.lowercase() == "dex" }
            initiative = dexterityStat.bonus +
                    statsContainer.initiativeAdditionalBonus
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
            bonus = stat.bonus +
                    skill.additionalBonus +
                    if (skill.isProficient) proficiencyBonus else 0
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

    private fun getSpellSaveDc(
        statsContainer: StatsContainer,
        stats: List<StatEntry>
    ): Int {
        var spellSaveDc = statsContainer.spellSaveDc
        if (statsContainer.use5eCalculations) {
            val spellcastingModifierStat =
                stats.firstOrNull { stat -> stat.isSpellcastingModifier }
            spellSaveDc =
                8 + (spellcastingModifierStat?.bonus ?: 0) +
                        statsContainer.spellSaveDcAdditionalBonus +
                        statsContainer.proficiencyBonus
        }
        return spellSaveDc
    }

    private fun getSpellAttackBonus(
        statsContainer: StatsContainer,
        stats: List<StatEntry>
    ): Int {
        var spellAttackBonus = statsContainer.spellAttackBonus
        if (statsContainer.use5eCalculations) {
            val spellcastingModifierStat =
                stats.firstOrNull { stat -> stat.isSpellcastingModifier }
            spellAttackBonus =
                (spellcastingModifierStat?.bonus ?: 0) +
                        statsContainer.spellAttackAdditionalBonus +
                        statsContainer.proficiencyBonus
        }
        return spellAttackBonus
    }

    fun resetValueToDefault(item: TrackedThing) {
        viewModelScope.launch {
            val itemCopy = item.copy()
            itemCopy.resetValueToDefault()
            if (itemCopy is Health) {
                itemCopy.temporaryHp = 0
            }
            withContext(Dispatchers.IO) {
                trackedThingDao.insertOrUpdate(itemCopy)
            }
            replaceItem(itemCopy)
        }
    }

    fun useAbility(item: Ability) {
        reduceByOne(item)
    }

    fun useSpell(item: SpellSlot) {
        reduceByOne(item)
    }

    private suspend fun useSpellSuspending(item: TrackedThing) {
        reduceByOneSuspending(item)
    }

    private fun reduceByOne(item: TrackedThing) {
        viewModelScope.launch {
            reduceByOneSuspending(item)
        }
    }

    private suspend fun reduceByOneSuspending(item: TrackedThing) {
        val itemCopy = item.copy()
        itemCopy.subtract("1")
        withContext(Dispatchers.IO) {
            trackedThingDao.insertOrUpdate(itemCopy)
        }
        replaceItem(itemCopy)
    }

    private fun addOne(item: TrackedThing) {
        viewModelScope.launch {
            val itemCopy = item.copy()
            itemCopy.add("1")
            withContext(Dispatchers.IO) {
                trackedThingDao.insertOrUpdate(itemCopy)
            }
            replaceItem(itemCopy)
        }
    }

    fun addToPercentageRequested(percentage: Percentage) =
        _dialog.update { ITrackerDialog.AddToPercentageDialog(percentage) }

    fun subtractFromPercentageRequested(percentage: Percentage) =
        _dialog.update { ITrackerDialog.SubtractFromPercentageDialog(percentage) }

    fun addToNumberRequested(number: Number) =
        _dialog.update { ITrackerDialog.AddToNumberDialog(number) }

    fun subtractFromNumberRequested(number: Number) =
        _dialog.update { ITrackerDialog.SubtractFromNumberDialog(number) }

    fun takeDamageRequested(health: Health) =
        _dialog.update { ITrackerDialog.DamageHealthDialog(health) }

    fun healRequested(health: Health) =
        _dialog.update { ITrackerDialog.HealHealthDialog(health) }

    fun addTemporaryHpRequested(health: Health) =
        _dialog.update { ITrackerDialog.AddTemporaryHpDialog(health) }

    fun deleteItemRequested(item: TrackedThing) =
        _dialog.update { ITrackerDialog.ConfirmDeletionDialog(item) }

    fun refreshAllRequested() =
        _dialog.update { ITrackerDialog.RefreshAllDialog() }

    fun itemReordered(from: Int, to: Int) {
        val itemCount = _items.value.size
        if (from == itemCount || to == itemCount) return

        viewModelScope.launch {
            val newList =
                _items
                    .value
                    .filterIsInstance<TrackedThing>()
                    .toMutableList()
                    .apply {
                        add(to, removeAt(from))
                        forEachIndexed { index, item ->
                            item.index = index
                        }
                    }
            _items.emit(newList)
            withContext(Dispatchers.IO) {
                newList.forEach {
                    trackedThingDao.insertOrUpdate(it)
                }
            }
        }
    }

    fun showPreviewSpellListDialog(spellList: SpellList, resetListState: Boolean) {
        if (spellList.serializedItem.isEmpty()) {
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
            val spellToAdd = withContext(Dispatchers.IO) {
                spellDao.getById(spellId)
            }
            val spellList = requireNotNull(spellListBeingAddedTo)
            val spellAlreadyInList =
                spellList.serializedItem.any {
                    it.id == spellId && it.name == spellToAdd.name
                }
            if (spellAlreadyInList) {
                _toast.showMessage(R.string.spell_already_in_list)
            } else {
                spellList.serializedItem.add(SpellListEntry.fromSpell(spellToAdd))
                val sortedSpells =
                    spellList.serializedItem
                        .sortedWith { spell1, spell2 ->
                            requireNotNull(spell1)
                            requireNotNull(spell2)
                            val comparisonByLevel = spell1.level.compareTo(spell2.level)
                            when {
                                comparisonByLevel != 0 -> comparisonByLevel
                                else -> spell1.name.compareTo(spell2.name)
                            }
                        }.toMutableList()
                spellList.setItem(sortedSpells, json)
                withContext(Dispatchers.IO) {
                    trackedThingDao.insertOrUpdate(spellList)
                }
                replaceItem(spellList.copy())
                _toast.showMessage(R.string.spell_successfully_added_to_list)
            }
            spellListBeingAddedTo = null
        }
    }

    fun addingSpellToList(spellList: SpellList) {
        spellListBeingAddedTo = spellList
    }

    fun removeSpellFromSpellListRequested(spell: SpellListEntry) {
        _dialog.update {
            if (it !is ITrackerDialog.SpellListDialog) return
            it.copy(secondaryDialog = ISpellListDialogDialogs.ConfirmSpellRemovalDialog(spell))
        }
    }

    fun removeSpellFromSpellList(spellList: SpellList, spellListEntry: SpellListEntry) {
        viewModelScope.launch {
            spellList.serializedItem.remove(spellListEntry)
            spellList.setItem(spellList.serializedItem, json)
            withContext(Dispatchers.IO) {
                trackedThingDao.insertOrUpdate(spellList)
            }
            val spellListCopy = spellList.copy() as SpellList
            replaceItem(spellListCopy)
            _dialog.update {
                if (it !is ITrackerDialog.SpellListDialog) return@launch
                it.copy(spellList = spellListCopy)
            }
        }
    }

    fun castSpellRequested(level: Int) {
        viewModelScope.launch {
            val availableSpellSlots =
                _items.value
                    .filterIsInstance<SpellSlot>()
                    .filter { it.level >= level && it.amount > 0 }
                    .map { it.level }
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
                    .filterIsInstance<SpellSlot>()
                    .first { it.level == withSlotLevel && it.amount > 0 }
            useSpellSuspending(spellSlot)
            _toast.showMessage(
                R.string.spell_cast_with_slot_level,
                withSlotLevel.toString()
            )
        }
    }

    fun castSpell(withSlotLevel: Int) {
        viewModelScope.launch {
            val spellSlot =
                _items.value
                    .filterIsInstance<SpellSlot>()
                    .first { it.level == withSlotLevel && it.amount > 0 }
            useSpellSuspending(spellSlot)
            _toast.showMessage(
                R.string.spell_cast_with_slot_level,
                withSlotLevel.toString()
            )
        }
    }

    fun showStatsDialog(stats: Stats) =
        _dialog.update { ITrackerDialog.PreviewStatSkillsDialog(stats.serializedItem) }

    fun canCastSpell(level: Int): Boolean =
        _items.value
            .filterIsInstance<SpellSlot>()
            .any { it.level >= level && it.amount > 0 }

    fun changeSpellListEntryPreparedState(
        spellList: SpellList,
        spellListEntry: SpellListEntry,
        isPrepared: Boolean
    ) {
        viewModelScope.launch {
            spellListEntry.isPrepared = isPrepared
            spellList.setItem(spellList.serializedItem, json)
            withContext(Dispatchers.IO) {
                trackedThingDao.insertOrUpdate(spellList)
            }
            val spellListCopy = spellList.copy() as SpellList
            replaceItem(spellListCopy)
            _dialog.update {
                if (it !is ITrackerDialog.SpellListDialog) return@launch
                it.copy(spellList = spellListCopy)
            }
        }
    }

    fun setShowingPreparedSpells(value: Boolean) {
        viewModelScope.launch { isShowingPreparedSpells.value = value }
    }

    fun useHitDie(item: HitDice) {
        reduceByOne(item)
    }

    fun restoreHitDie(item: HitDice) {
        addOne(item)
    }

    fun dismissDialog() {
        _dialog.update { ITrackerDialog.None }
    }

    fun dismissSpellListSecondaryDialog() {
        _dialog.update {
            if (it !is ITrackerDialog.SpellListDialog) return
            it.copy(secondaryDialog = ISpellListDialogDialogs.None)
        }
    }

    fun editDialogValueUpdated(trackedThing: TrackedThing) {
        _dialog.update {
            if (it !is ITrackerDialog.EditDialog) return
            it.copy(editedItem = trackedThing)
        }
    }
}