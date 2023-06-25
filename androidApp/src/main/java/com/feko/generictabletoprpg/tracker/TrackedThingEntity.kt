package com.feko.generictabletoprpg.com.feko.generictabletoprpg.tracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.CoreConvertible
import com.feko.generictabletoprpg.common.MutableIdentifiable
import com.feko.generictabletoprpg.common.Named
import com.feko.generictabletoprpg.tracker.TrackedThing

@Entity(tableName = "tracked_things")
data class TrackedThingEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    val level: Int,
    @ColumnInfo(defaultValue = "0")
    val temporaryHp: Int,
    val value: String,
    @ColumnInfo(defaultValue = "")
    var defaultValue: String,
    val type: Int
) : MutableIdentifiable,
    Named,
    CoreConvertible<TrackedThing> {

    override fun toCoreModel(): TrackedThing {
        val trackedThing = when (type) {
            TrackedThing.Type.Percentage.ordinal ->
                TrackedThing.Percentage(id, name, value.toFloat())

            TrackedThing.Type.Health.ordinal ->
                TrackedThing.Health(temporaryHp, id, name, value.toInt())

            TrackedThing.Type.Ability.ordinal ->
                TrackedThing.Ability(id, name, value.toInt())

            TrackedThing.Type.SpellSlot.ordinal ->
                TrackedThing.SpellSlot(level, id, name, value.toInt())

            else -> throw Exception("Tracked thing not supported.")
        }
        trackedThing.defaultValue = defaultValue
        return trackedThing
    }
}