package com.feko.generictabletoprpg.ammunition

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.CoreConvertible
import com.feko.generictabletoprpg.common.CostEmbeddedEntity
import com.feko.generictabletoprpg.common.FromSource
import com.feko.generictabletoprpg.common.MutableIdentifiable
import com.feko.generictabletoprpg.common.Named

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
) : MutableIdentifiable,
    Named,
    FromSource,
    CoreConvertible<Ammunition> {
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

