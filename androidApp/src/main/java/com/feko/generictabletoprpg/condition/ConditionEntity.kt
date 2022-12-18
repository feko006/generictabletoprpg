package com.feko.generictabletoprpg.condition

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.CoreConvertible
import com.feko.generictabletoprpg.common.FromSource
import com.feko.generictabletoprpg.common.MutableIdentifiable
import com.feko.generictabletoprpg.common.Named

@Entity(tableName = "conditions")
data class ConditionEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    val description: String,
    override val source: String
) : MutableIdentifiable,
    Named,
    FromSource,
    CoreConvertible<Condition> {
    override fun toCoreModel(): Condition =
        Condition(id, name, description, source)

    companion object {
        fun fromCoreModel(item: Condition): ConditionEntity =
            ConditionEntity(item.id, item.name, item.description, item.source)
    }
}