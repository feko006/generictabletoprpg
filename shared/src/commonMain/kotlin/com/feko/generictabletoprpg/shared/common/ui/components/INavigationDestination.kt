package com.feko.generictabletoprpg.shared.common.ui.components

import androidx.navigation3.runtime.NavKey
import com.feko.generictabletoprpg.shared.features.spell.Spell
import kotlinx.serialization.Serializable

@Serializable
sealed interface INavigationDestination : NavKey {

    @Serializable
    data object TrackerGroupsDestination : INavigationDestination

    @Serializable
    data class TrackerDestination(val id: Long, val name: String) : INavigationDestination

    @Serializable
    data object EncounterDestination : INavigationDestination

    @Serializable
    data class SearchAllDestination(
        val filterIndex: Int?,
        val isShownForResult: Boolean
    ) : INavigationDestination

    @Serializable
    class ActionDetailsDestination(val id: Long) : INavigationDestination

    @Serializable
    class AmmunitionDetailsDestination(val id: Long) : INavigationDestination

    @Serializable
    class ArmorDetailsDestination(val id: Long) : INavigationDestination

    @Serializable
    class ConditionDetailsDestination(val id: Long) : INavigationDestination

    @Serializable
    class DiseaseDetailsDestination(val id: Long) : INavigationDestination

    @Serializable
    class FeatDetailsDestination(val id: Long) : INavigationDestination

    @Serializable
    class SpellDetailsDestination(val id: Long) : INavigationDestination

    @Serializable
    class WeaponDetailsDestination(val id: Long) : INavigationDestination

    @Serializable
    data class SimpleSpellDetailsDestination(val spell: Spell) : INavigationDestination

    @Serializable
    data object ImportDestination : INavigationDestination

    companion object {
        val startDestination: INavigationDestination = TrackerGroupsDestination
    }
}