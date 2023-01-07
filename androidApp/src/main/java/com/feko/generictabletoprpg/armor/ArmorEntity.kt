package com.feko.generictabletoprpg.armor

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.CoreConvertible
import com.feko.generictabletoprpg.common.FromSource
import com.feko.generictabletoprpg.common.MutableIdentifiable
import com.feko.generictabletoprpg.common.Named

@Entity(
    tableName = "armors"
)
data class ArmorEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    override val source: String,
    val type: String,
    val baseAc: Int?,
    val maxDexModifier: Int?,
    val stealthDisadvantage: Boolean?,
    // In lb
    val weight: Int?,
    val minimumStrength: Int?
) : Named,
    MutableIdentifiable,
    FromSource,
    CoreConvertible<Armor> {

    override fun toCoreModel(): Armor =
        Armor(
            id,
            name,
            source,
            type,
            baseAc,
            maxDexModifier,
            stealthDisadvantage,
            weight,
            minimumStrength
        )

    companion object {
        fun fromCoreModel(armor: Armor): ArmorEntity {
            return ArmorEntity(
                armor.id,
                armor.name,
                armor.source,
                armor.type,
                armor.baseAc,
                armor.maxDexModifier,
                armor.stealthDisadvantage,
                armor.weight,
                armor.minimumStrength
            )
        }
    }
}
