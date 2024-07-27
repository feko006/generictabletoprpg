package com.feko.generictabletoprpg.tracker

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.feko.generictabletoprpg.common.ICoreConvertible
import com.feko.generictabletoprpg.common.IMutableIdentifiable
import com.feko.generictabletoprpg.common.INamed

@DoNotObfuscate
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