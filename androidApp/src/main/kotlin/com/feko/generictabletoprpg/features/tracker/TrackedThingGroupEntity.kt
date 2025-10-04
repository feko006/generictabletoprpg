package com.feko.generictabletoprpg.features.tracker

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.shared.common.domain.model.ICoreConvertible
import com.feko.generictabletoprpg.shared.common.domain.model.IMutableIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed
import com.feko.generictabletoprpg.features.tracker.domain.model.TrackedThingGroup

@Keep
@Entity(tableName = "tracked_thing_groups")
data class TrackedThingGroupEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String
) : IMutableIdentifiable,
    INamed,
    ICoreConvertible<TrackedThingGroup> {
    override fun toCoreModel(): TrackedThingGroup = TrackedThingGroup(id, name)
}