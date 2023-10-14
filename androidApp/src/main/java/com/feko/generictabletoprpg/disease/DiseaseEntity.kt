package com.feko.generictabletoprpg.disease

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.ICoreConvertible
import com.feko.generictabletoprpg.common.IFromSource
import com.feko.generictabletoprpg.common.IMutableIdentifiable
import com.feko.generictabletoprpg.common.INamed

@Entity(tableName = "diseases")
data class DiseaseEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    val description: String,
    override val source: String
) : IMutableIdentifiable,
    INamed,
    IFromSource,
    ICoreConvertible<Disease> {
    override fun toCoreModel(): Disease =
        Disease(id, name, description, source)

    companion object {
        fun fromCoreModel(item: Disease): DiseaseEntity =
            DiseaseEntity(item.id, item.name, item.description, item.source)
    }
}