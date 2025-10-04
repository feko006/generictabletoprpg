package com.feko.generictabletoprpg.features.tracker

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.shared.common.domain.model.ICoreConvertible
import com.feko.generictabletoprpg.shared.common.domain.model.IMutableIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.features.tracker.domain.model.TrackedThing

@Keep
@Entity(
    tableName = "tracked_things",
    foreignKeys = [
        ForeignKey(
            entity = TrackedThingGroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
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
    val type: Int,
    @ColumnInfo(defaultValue = "0")
    val idx: Int,
    @ColumnInfo(
        index = true,
        defaultValue = "0"
    )
    val groupId: Long
) : IMutableIdentifiable,
    INamed,
    ICoreConvertible<TrackedThing> {

    override fun toCoreModel(): TrackedThing {
        val type = TrackedThing.Type.entries[type]
        if (type == TrackedThing.Type.None) {
            throw Exception("Tracked thing not supported.")
        }
        val trackedThing =
            TrackedThing(id, name, value, type, idx, groupId = groupId, defaultValue = defaultValue)
        if (type == TrackedThing.Type.Health) {
            trackedThing.temporaryHp = temporaryHp
        } else if (type == TrackedThing.Type.SpellSlot) {
            trackedThing.level = level
        }
        return trackedThing
    }
}