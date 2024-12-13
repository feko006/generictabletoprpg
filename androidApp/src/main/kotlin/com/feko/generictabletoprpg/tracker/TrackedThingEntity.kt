package com.feko.generictabletoprpg.tracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.feko.generictabletoprpg.common.ICoreConvertible
import com.feko.generictabletoprpg.common.IMutableIdentifiable
import com.feko.generictabletoprpg.common.INamed

@DoNotObfuscate
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
        val trackedThing =
            when (TrackedThing.Type.entries[type]) {
                TrackedThing.Type.Percentage ->
                    Percentage(id, name, value.toFloat(), idx, groupId)

                TrackedThing.Type.Health ->
                    Health(temporaryHp, id, name, value.toInt(), idx, groupId)

                TrackedThing.Type.Ability ->
                    Ability(id, name, value.toInt(), idx, groupId)

                TrackedThing.Type.SpellSlot ->
                    SpellSlot(level, id, name, value.toInt(), idx, groupId)

                TrackedThing.Type.Number ->
                    Number(id, name, value.toInt(), idx, groupId)

                TrackedThing.Type.SpellList ->
                    SpellList(id, name, value, idx, groupId)

                TrackedThing.Type.Text ->
                    Text(id, name, value, idx, groupId)

                TrackedThing.Type.HitDice ->
                    HitDice(id, name, value.toInt(), idx, groupId)

                TrackedThing.Type.FiveEStats ->
                    Stats(id, name, value, idx, groupId)

                TrackedThing.Type.None -> throw Exception("Tracked thing not supported.")
            }
        trackedThing.defaultValue = defaultValue
        return trackedThing
    }
}