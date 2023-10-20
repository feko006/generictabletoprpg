package com.feko.generictabletoprpg.condition

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.feko.generictabletoprpg.common.ICoreConvertible
import com.feko.generictabletoprpg.common.IFromSource
import com.feko.generictabletoprpg.common.IMutableIdentifiable
import com.feko.generictabletoprpg.common.INamed

@DoNotObfuscate
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