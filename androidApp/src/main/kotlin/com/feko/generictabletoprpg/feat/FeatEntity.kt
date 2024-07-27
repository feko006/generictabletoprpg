package com.feko.generictabletoprpg.feat

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.feko.generictabletoprpg.common.ICoreConvertible
import com.feko.generictabletoprpg.common.IFromSource
import com.feko.generictabletoprpg.common.IMutableIdentifiable
import com.feko.generictabletoprpg.common.INamed
import com.feko.generictabletoprpg.common.Stat

@DoNotObfuscate
@Entity(tableName = "feats")
data class FeatEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    val description: String,
    override val source: String,
    val abilityIncreases: List<Stat>,
    val proficiencyRequirements: List<String>,
    val statRequirements: List<Stat>,
    val raceRequirements: List<String>,
    val savingThrow: Boolean
) : INamed,
    IMutableIdentifiable,
    IFromSource,
    ICoreConvertible<Feat> {
    override fun toCoreModel(): Feat =
        Feat(
            id,
            name,
            description,
            source,
            abilityIncreases,
            proficiencyRequirements,
            statRequirements,
            raceRequirements,
            savingThrow
        )

    companion object {
        fun fromCoreModel(feat: Feat): FeatEntity =
            FeatEntity(
                feat.id,
                feat.name,
                feat.description,
                feat.source,
                feat.abilityIncreases,
                feat.proficiencyRequirements,
                feat.statRequirements,
                feat.raceRequirements,
                feat.savingThrow
            )
    }
}