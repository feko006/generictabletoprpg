@file:Suppress("DeprecatedCallableAddReplaceWith")

package com.feko.generictabletoprpg.features.tracker.domain.model

import androidx.annotation.Keep
import com.feko.generictabletoprpg.common.domain.IJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Keep
abstract class JsonTrackedThing<T>(
    id: Long, name: String,
    value: String,
    type: Type,
    index: Int = 0,
    groupId: Long = 0L,
    private val serializedType: java.lang.reflect.Type
) : TrackedThing(id, name, value, type, index, groupId) {
    var serializedItem: T = this.createDefaultValue()

    abstract fun createDefaultValue(): T

    @Deprecated("", level = DeprecationLevel.ERROR)
    final override fun setNewValue(value: String) = Unit

    @Deprecated("", level = DeprecationLevel.ERROR)
    final override fun add(delta: String) = throw IllegalStateException()

    @Deprecated("", level = DeprecationLevel.ERROR)
    final override fun subtract(delta: String) = throw IllegalStateException()

    @Deprecated("", level = DeprecationLevel.ERROR)
    final override fun canAdd(): Boolean = throw IllegalStateException()

    @Deprecated("", level = DeprecationLevel.ERROR)
    final override fun canSubtract(): Boolean = throw IllegalStateException()

    override fun copy(): TrackedThing = createCopy().also { it.serializedItem = serializedItem }

    protected abstract fun createCopy(): JsonTrackedThing<T>

    suspend fun setItem(newItem: T, json: IJson) {
        this.serializedItem = newItem
        withContext(Dispatchers.Default) {
            value = json.to(serializedItem, serializedType)
        }
    }

    fun setItemFromValue(json: IJson) {
        serializedItem = json.from(value, serializedType)
    }
}
