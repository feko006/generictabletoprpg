package com.feko.generictabletoprpg.shared.features.tracker.model

import com.feko.generictabletoprpg.shared.common.data.json
import com.feko.generictabletoprpg.shared.features.tracker.model.TrackedThing.Type
import kotlinx.serialization.builtins.ListSerializer
import java.util.Locale

val Type.initialDefaultValue
    get() = when (this) {
        Type.None,
        Type.Text,
        Type.SpellList,
        Type.FiveEStats -> ""

        Type.Ability,
        Type.Health,
        Type.HitDice,
        Type.Number,
        Type.SpellSlot,
        Type.Percentage -> "0"
    }

fun Type.normalize(value: String) = when (this) {
    Type.None,
    Type.Text,
    Type.SpellList,
    Type.FiveEStats -> value

    Type.Ability,
    Type.Health,
    Type.HitDice,
    Type.Number,
    Type.SpellSlot -> value.toIntOrNull()?.toString() ?: "0"

    Type.Percentage ->
        String.format(Locale.US, "%.2f", value.toFloat())
}

fun Type.toAmount(value: String): Number = when (this) {
    Type.None,
    Type.Text,
    Type.SpellList,
    Type.FiveEStats -> 0

    Type.Ability,
    Type.Health,
    Type.HitDice,
    Type.Number,
    Type.SpellSlot -> value.toIntOrNull() ?: 0

    Type.Percentage -> (value.toFloatOrNull() ?: 0f).coerceIn(0f, 100f)
}

val TrackedThing.isIntBased
    get() = when (this.type) {
        Type.Ability,
        Type.Health,
        Type.HitDice,
        Type.Number,
        Type.SpellSlot -> true

        Type.None,
        Type.Percentage,
        Type.SpellList,
        Type.Text,
        Type.FiveEStats -> false
    }

val TrackedThing.amount: Number
    get() = type.toAmount(value)

fun TrackedThing.setNewValue(value: String) {
    when (type) {
        Type.None -> Unit

        Type.Ability,
        Type.Health,
        Type.HitDice,
        Type.Number,
        Type.SpellSlot,
        Type.Percentage,
        Type.Text,
        Type.SpellList,
        Type.FiveEStats -> this.value = type.normalize(value)
    }
}

fun TrackedThing.isValueValid(): Boolean = when (type) {
    Type.None,
    Type.Number -> true

    Type.Ability,
    Type.Health,
    Type.HitDice,
    Type.SpellSlot -> value.isNotBlank()
            && amount.toInt() > 0
            && amount.toInt() <= type.toAmount(managedDefaultValue).toInt()

    Type.Percentage -> value.isNotBlank()
            && amount.toFloat().let { it >= 0f && it <= 100f }

    Type.Text,
    Type.SpellList,
    Type.FiveEStats -> value.isNotBlank()
}

fun TrackedThing.validate(): Boolean = when (type) {
    Type.None -> true

    Type.Ability,
    Type.Health,
    Type.HitDice,
    Type.Number,
    Type.Percentage,
    Type.Text,
    Type.SpellList,
    Type.FiveEStats -> isValueValid()

    Type.SpellSlot -> isValueValid() && isLevelValid
}

@Suppress("UNCHECKED_CAST")
val TrackedThing.printableValue: String
    get() = when (type) {
        Type.None,
        Type.FiveEStats -> ""

        Type.Text,
        Type.Number -> value

        Type.Ability,
        Type.Health,
        Type.HitDice,
        Type.SpellSlot -> "$value / $managedDefaultValue"

        Type.Percentage -> "$value%"

        Type.SpellList -> (serializedItem as? List<SpellListEntry>)?.size?.toString() ?: "0"
    }

fun TrackedThing.add(delta: String) {
    when (type) {
        Type.None,
        Type.Text,
        Type.SpellList,
        Type.FiveEStats -> Unit

        Type.Ability,
        Type.Health,
        Type.HitDice,
        Type.Number,
        Type.SpellSlot -> {
            val intDelta = type.toAmount(delta).toInt()
            var newAmount = amount.toInt() + intDelta
            if (type != Type.Number) {
                newAmount = newAmount.coerceAtMost(type.toAmount(managedDefaultValue).toInt())
            }
            value = newAmount.toString()
        }

        Type.Percentage -> {
            val floatDelta = type.toAmount(delta).toFloat()
            val newAmount = (amount.toFloat() + floatDelta).coerceAtMost(100f)
            setNewValue(newAmount.toString())
        }
    }
}

fun TrackedThing.subtract(delta: String) {
    when (type) {
        Type.None,
        Type.Text,
        Type.SpellList,
        Type.FiveEStats -> Unit

        Type.Ability,
        Type.HitDice,
        Type.Number,
        Type.SpellSlot -> {
            val intDelta = type.toAmount(delta).toInt()
            var newAmount = amount.toInt() - intDelta
            if (type != Type.Number) {
                newAmount = newAmount.coerceAtLeast(0)
            }
            value = newAmount.toString()
        }

        Type.Health -> {
            var intDelta = type.toAmount(delta).toInt()
            val newTemporaryHp = (temporaryHp - intDelta).coerceAtLeast(0)
            intDelta -= (temporaryHp - newTemporaryHp)
            temporaryHp = newTemporaryHp
            var newAmount = amount.toInt() - intDelta
            newAmount = newAmount.coerceAtLeast(0)
            value = newAmount.toString()
        }

        Type.Percentage -> {
            val floatDelta = type.toAmount(delta).toFloat()
            val newAmount = (amount.toFloat() - floatDelta).coerceAtLeast(0f)
            setNewValue(newAmount.toString())
        }
    }
}

val TrackedThing.canAdd: Boolean
    get() = when (type) {
        Type.None,
        Type.Text,
        Type.SpellList,
        Type.FiveEStats -> false

        Type.Number -> true

        Type.Ability,
        Type.Health,
        Type.HitDice,
        Type.SpellSlot -> amount.toInt() < type.toAmount(managedDefaultValue).toInt()

        Type.Percentage -> amount.toFloat() < 100f
    }

val TrackedThing.canSubtract: Boolean
    get() = when (type) {
        Type.None,
        Type.Text,
        Type.SpellList,
        Type.FiveEStats -> false

        Type.Number -> true

        Type.Ability,
        Type.Health,
        Type.HitDice,
        Type.SpellSlot -> amount.toInt() > 0

        Type.Percentage -> amount.toFloat() > 0f
    }

fun TrackedThing.resetValueToDefault() =
    when (type) {
        Type.None,
        Type.Number,
        Type.Text,
        Type.SpellList,
        Type.FiveEStats -> Unit

        Type.Ability,
        Type.Health,
        Type.SpellSlot,
        Type.Percentage -> setNewValue(managedDefaultValue)

        Type.HitDice -> {
            val maximumValue = managedDefaultValue.toInt()
            val toAdd = (maximumValue / 2f).toInt().coerceAtLeast(1)
            val newValue = (amount.toInt() + toAdd).coerceAtMost(maximumValue)
            setNewValue(newValue.toString())
        }
    }

fun TrackedThing.addTemporaryHp(value: String) {
    if (type != Type.Health) return
    val newTemporaryHp = type.toAmount(value).toInt()
    if (temporaryHp < newTemporaryHp) {
        temporaryHp = newTemporaryHp
    }
}

val TrackedThing.isLevelValid: Boolean
    get() = type == Type.SpellSlot && level > 0

fun TrackedThing.setItem(item: Any) {
    serializedItem = item
    value =
        if (type == Type.FiveEStats && item is StatsContainer) {
            json.encodeToString(StatsContainer.Companion.serializer(), item)
        } else if (type == Type.SpellList && item is List<*> && item.all { it is SpellListEntry }) {
            @Suppress("UNCHECKED_CAST")
            json.encodeToString(
                ListSerializer(SpellListEntry.Companion.serializer()),
                (item as List<SpellListEntry>)
            )
        } else ""
}

fun TrackedThing.getItem(): Any =
    when (type) {
        Type.FiveEStats -> json.decodeFromString(StatsContainer.Companion.serializer(), value)
        Type.SpellList -> json.decodeFromString(
            ListSerializer(SpellListEntry.Companion.serializer()),
            value
        )
        else -> ""
    }
