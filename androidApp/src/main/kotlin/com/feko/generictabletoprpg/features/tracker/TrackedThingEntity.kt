package com.feko.generictabletoprpg.features.tracker

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.domain.model.ICoreConvertible
import com.feko.generictabletoprpg.common.domain.model.IMutableIdentifiable
import com.feko.generictabletoprpg.common.domain.model.INamed
import com.feko.generictabletoprpg.features.tracker.domain.model.AbilityTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.HealthTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.HitDiceTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.NumberTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.PercentageTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.SpellListTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.SpellSlotTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.StatsTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.TextTrackedThing
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
        val trackedThing =
            when (TrackedThing.Type.entries[type]) {
                TrackedThing.Type.Percentage ->
                    PercentageTrackedThing(id, name, value.toFloat(), idx, groupId)

                TrackedThing.Type.Health ->
                    HealthTrackedThing(temporaryHp, id, name, value.toInt(), idx, groupId)

                TrackedThing.Type.Ability ->
                    AbilityTrackedThing(id, name, value.toInt(), idx, groupId)

                TrackedThing.Type.SpellSlot ->
                    SpellSlotTrackedThing(level, id, name, value.toInt(), idx, groupId)

                TrackedThing.Type.Number ->
                    NumberTrackedThing(id, name, value.toInt(), idx, groupId)

                TrackedThing.Type.SpellList ->
                    SpellListTrackedThing(id, name, value, idx, groupId)

                TrackedThing.Type.Text ->
                    TextTrackedThing(id, name, value, idx, groupId)

                TrackedThing.Type.HitDice ->
                    HitDiceTrackedThing(id, name, value.toInt(), idx, groupId)

                TrackedThing.Type.FiveEStats ->
                    StatsTrackedThing(id, name, value, idx, groupId)

                TrackedThing.Type.None -> throw Exception("Tracked thing not supported.")
            }
        trackedThing.defaultValue = defaultValue
        return trackedThing
    }
}