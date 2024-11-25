package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.squareup.moshi.Types

@DoNotObfuscate
class SpellList(
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
    }

    override fun getPrintableValue(): String = serializedItem.size.toString()
    override fun createDefaultValue(): MutableList<SpellListEntry> = mutableListOf()
    override fun createCopy(): JsonTrackedThing<MutableList<SpellListEntry>> =
        SpellList(id, name, value, index, groupId)

}
