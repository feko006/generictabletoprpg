package com.feko.generictabletoprpg.features.ammunition

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.data.local.entities.CostEmbeddedEntity
import com.feko.generictabletoprpg.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.common.domain.model.ICoreConvertible
import com.feko.generictabletoprpg.common.domain.model.IFromSource
import com.feko.generictabletoprpg.common.domain.model.IMutableIdentifiable
import com.feko.generictabletoprpg.common.domain.model.INamed

@DoNotObfuscate
@Entity(tableName = "ammunitions")
data class AmmunitionEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    val sellQuantity: Long,
    @Embedded
    val cost: CostEmbeddedEntity,
    val weight: String,
    override val source: String
) : IMutableIdentifiable,
    INamed,
    IFromSource,
    ICoreConvertible<Ammunition> {
    override fun toCoreModel(): Ammunition =
        Ammunition(id, name, sellQuantity, cost.toCoreModel(), weight, source)

    companion object {
        fun fromCoreModel(item: Ammunition): AmmunitionEntity =
            AmmunitionEntity(
                item.id,
                item.name,
                item.sellQuantity,
                CostEmbeddedEntity.fromCoreModel(item.cost),
                item.weight,
                item.source
            )
    }
}

