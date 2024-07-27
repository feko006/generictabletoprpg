package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.DoNotObfuscate

@DoNotObfuscate
class Text(
    id: Long = 0L,
    name: String,
    value: String,
    index: Int = 0,
    groupId: Long = 0L
) : TrackedThing(id, name, value, Type.Text, index, groupId) {

    init {
        defaultValue = value
    }

    override fun setNewValue(value: String) {
        this.value = value
        defaultValue = value
    }

    override fun getPrintableValue(): String = value

    override fun add(delta: String) = throw UnsupportedOperationException()

    override fun subtract(delta: String) = throw UnsupportedOperationException()

    override fun copy(): TrackedThing = Text(id, name, value, index, groupId)

    override fun canAdd(): Boolean = false

    override fun canSubtract(): Boolean = false
}
