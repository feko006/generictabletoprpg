package com.feko.generictabletoprpg.disease

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.CoreConvertible
import com.feko.generictabletoprpg.common.FromSource
import com.feko.generictabletoprpg.common.MutableIdentifiable
import com.feko.generictabletoprpg.common.Named

@Entity(tableName = "diseases")
data class DiseaseEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    val description: String,
    override val source: String
) : MutableIdentifiable,
    Named,
    FromSource,
    CoreConvertible<Disease> {
    override fun toCoreModel(): Disease =
        Disease(id, name, description, source)

    companion object {
        fun fromCoreModel(item: Disease): DiseaseEntity =
            DiseaseEntity(item.id, item.name, item.description, item.source)
    }
}