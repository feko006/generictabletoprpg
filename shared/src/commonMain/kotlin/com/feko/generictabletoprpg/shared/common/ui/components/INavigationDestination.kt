package com.feko.generictabletoprpg.shared.common.ui.components

import androidx.navigation3.runtime.NavKey
import com.feko.generictabletoprpg.shared.features.spell.Spell
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

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

        val serializersModule = SerializersModule {
            polymorphic(baseClass = NavKey::class) {
                subclass(serializer = TrackerGroupsDestination.serializer())
                subclass(serializer = TrackerDestination.serializer())
                subclass(serializer = EncounterDestination.serializer())
                subclass(serializer = SearchAllDestination.serializer())
                subclass(serializer = ActionDetailsDestination.serializer())
                subclass(serializer = AmmunitionDetailsDestination.serializer())
                subclass(serializer = ArmorDetailsDestination.serializer())
                subclass(serializer = ConditionDetailsDestination.serializer())
                subclass(serializer = DiseaseDetailsDestination.serializer())
                subclass(serializer = FeatDetailsDestination.serializer())
                subclass(serializer = SpellDetailsDestination.serializer())
                subclass(serializer = WeaponDetailsDestination.serializer())
                subclass(serializer = SimpleSpellDetailsDestination.serializer())
                subclass(serializer = ImportDestination.serializer())
            }
        }
    }
}