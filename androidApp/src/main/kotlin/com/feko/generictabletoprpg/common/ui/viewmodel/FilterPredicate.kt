package com.feko.generictabletoprpg.common.ui.viewmodel

import com.feko.generictabletoprpg.features.filters.Filter
import com.feko.generictabletoprpg.features.filters.GenericFilter
import com.feko.generictabletoprpg.features.filters.SpellFilter
import com.feko.generictabletoprpg.features.spell.Spell

class FilterPredicate(val filter: Filter?) : (Any) -> Boolean {
    override fun invoke(item: Any): Boolean {
        if (filter == null) {
            return true
        }

        return when (filter) {
            is SpellFilter -> filterSpell(filter, item)
            is GenericFilter -> filterGeneric(filter, item)
        }
    }

    private fun filterGeneric(genericFilter: GenericFilter, item: Any): Boolean {
        return item::class.java == genericFilter.type
    }

    private fun filterSpell(spellFilter: SpellFilter, item: Any): Boolean {
        if (item !is Spell) {
            return false
        }

        spellFilter.school?.let { schoolFilter ->
            if (item.school.lowercase() != schoolFilter.lowercase()) {
                return false
            }
        }

        spellFilter.concentration?.let {
            if (item.concentration != it) {
                return false
            }
        }

        spellFilter.level?.let {
            if (item.level != it) {
                return false
            }
        }

        spellFilter.`class`?.let { classFilter ->
            val classCanCast =
                item.classesThatCanCast
                    .isEmpty()
                    .or(
                        item.classesThatCanCast
                            .any {
                                it.lowercase() == classFilter.lowercase()
                            }
                    )
            if (!classCanCast) {
                return false
            }
        }

        spellFilter.isRitual?.let {
            if (item.isRitual != it) {
                return false
            }
        }

        spellFilter.spellComponents?.let {
            if (!item.hasComponents
                || item.components.verbal != it.verbal
                || item.components.somatic != it.somatic
                || item.components.material != it.material
            ) {
                return false
            }
        }

        return true
    }
}
