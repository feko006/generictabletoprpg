package com.feko.generictabletoprpg.armor

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.feko.generictabletoprpg.common.ICoreConvertible
import com.feko.generictabletoprpg.common.IFromSource
import com.feko.generictabletoprpg.common.IMutableIdentifiable
import com.feko.generictabletoprpg.common.INamed

@DoNotObfuscate
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
) : INamed,
    IMutableIdentifiable,
    IFromSource,
    ICoreConvertible<Armor> {

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
