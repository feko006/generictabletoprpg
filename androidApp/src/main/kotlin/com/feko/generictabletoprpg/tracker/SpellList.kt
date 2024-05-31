package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.feko.generictabletoprpg.import.IJson
import com.feko.generictabletoprpg.spell.Spell
import com.squareup.moshi.Types
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@DoNotObfuscate
class SpellList(
    id: Long = 0L,
    name: String,
    value: String,
    index: Int = 0,
    groupId: Long = 0L
) : TrackedThing(id, name, value, Type.SpellList, index, groupId) {

    companion object {
        val spellListType = Types.newParameterizedType(List::class.java, Spell::class.java)
    }

    var spells: MutableList<Spell> = mutableListOf()

    override fun setNewValue(value: String) = Unit

    override fun getPrintableValue(): String = spells.size.toString()

    override fun add(delta: String) = throw IllegalStateException()

    override fun subtract(delta: String) = throw IllegalStateException()

    override fun copy(): TrackedThing = this

    override fun canAdd(): Boolean = throw IllegalStateException()

    override fun canSubtract(): Boolean = throw IllegalStateException()
    suspend fun setSpells(spells: List<Spell>, json: IJson) {
        this.spells = spells.toMutableList()
        withContext(Dispatchers.Default) {
            value = json.to(spells, spellListType)
        }
    }

}
