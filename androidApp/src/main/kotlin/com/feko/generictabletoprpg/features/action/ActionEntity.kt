package com.feko.generictabletoprpg.features.action

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.shared.common.domain.model.ICoreConvertible
import com.feko.generictabletoprpg.shared.common.domain.model.IFromSource
import com.feko.generictabletoprpg.shared.common.domain.model.IMutableIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed

@Keep
@Entity(tableName = "actions")
data class ActionEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    val description: String,
    override val source: String
) : IMutableIdentifiable,
    INamed,
    IFromSource,
    ICoreConvertible<Action> {
    override fun toCoreModel(): Action =
        Action(id, name, description, source)

    companion object {
        fun fromCoreModel(item: Action): ActionEntity =
            ActionEntity(item.id, item.name, item.description, item.source)
    }
}