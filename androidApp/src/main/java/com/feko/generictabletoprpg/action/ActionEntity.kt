package com.feko.generictabletoprpg.action

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.CoreConvertible
import com.feko.generictabletoprpg.common.FromSource
import com.feko.generictabletoprpg.common.MutableIdentifiable
import com.feko.generictabletoprpg.common.Named

@Entity(tableName = "actions")
data class ActionEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    val description: String,
    override val source: String
) : MutableIdentifiable,
    Named,
    FromSource,
    CoreConvertible<Action> {
    override fun toCoreModel(): Action =
        Action(id, name, description, source)

    companion object {
        fun fromCoreModel(item: Action): ActionEntity =
            ActionEntity(item.id, item.name, item.description, item.source)
    }
}