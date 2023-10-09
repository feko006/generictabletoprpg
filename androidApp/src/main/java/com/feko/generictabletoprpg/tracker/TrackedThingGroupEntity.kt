package com.feko.generictabletoprpg.tracker

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.CoreConvertible
import com.feko.generictabletoprpg.common.MutableIdentifiable
import com.feko.generictabletoprpg.common.Named

@Entity(tableName = "tracked_thing_groups")
data class TrackedThingGroupEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String
) : MutableIdentifiable,
    Named,
    CoreConvertible<TrackedThingGroup> {
    override fun toCoreModel(): TrackedThingGroup = TrackedThingGroup(id, name)
}