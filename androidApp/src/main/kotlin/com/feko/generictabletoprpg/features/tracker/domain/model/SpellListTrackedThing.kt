package com.feko.generictabletoprpg.features.tracker.domain.model

import androidx.annotation.Keep
import com.squareup.moshi.Types

@Keep
class SpellListTrackedThing(
    id: Long = 0L,
    name: String,
    value: String,
    index: Int = 0,
    groupId: Long = 0L
) : JsonTrackedThing<MutableList<SpellListEntry>>(
    id,
    name,
    value,
    Type.SpellList,
    index,
    groupId,
    spellListType
) {
    companion object {
        val spellListType =
            Types.newParameterizedType(List::class.java, SpellListEntry::class.java)!!

        val Empty = SpellListTrackedThing(0L, "", "", 0, 0L)
    }

    override fun getPrintableValue(): String = serializedItem.size.toString()
    override fun createDefaultValue(): MutableList<SpellListEntry> = mutableListOf()
    override fun createCopy(): JsonTrackedThing<MutableList<SpellListEntry>> =
        SpellListTrackedThing(id, name, value, index, groupId)

}
