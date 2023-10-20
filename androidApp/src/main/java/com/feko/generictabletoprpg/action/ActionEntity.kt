package com.feko.generictabletoprpg.action

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.feko.generictabletoprpg.common.ICoreConvertible
import com.feko.generictabletoprpg.common.IFromSource
import com.feko.generictabletoprpg.common.IMutableIdentifiable
import com.feko.generictabletoprpg.common.INamed

@DoNotObfuscate
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