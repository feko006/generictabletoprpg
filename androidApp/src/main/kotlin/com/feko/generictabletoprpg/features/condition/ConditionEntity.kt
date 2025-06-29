package com.feko.generictabletoprpg.features.condition

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.domain.model.ICoreConvertible
import com.feko.generictabletoprpg.common.domain.model.IFromSource
import com.feko.generictabletoprpg.common.domain.model.IMutableIdentifiable
import com.feko.generictabletoprpg.common.domain.model.INamed

@Keep
@Entity(tableName = "conditions")
data class ConditionEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    val description: String,
    override val source: String
) : IMutableIdentifiable,
    INamed,
    IFromSource,
    ICoreConvertible<Condition> {
    override fun toCoreModel(): Condition =
        Condition(id, name, description, source)

    companion object {
        fun fromCoreModel(item: Condition): ConditionEntity =
            ConditionEntity(item.id, item.name, item.description, item.source)
    }
}