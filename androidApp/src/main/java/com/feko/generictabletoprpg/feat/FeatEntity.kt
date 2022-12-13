package com.feko.generictabletoprpg.feat

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.CoreConvertible
import com.feko.generictabletoprpg.common.MutableIdentifiable
import com.feko.generictabletoprpg.common.Named
import com.feko.generictabletoprpg.common.Stat

@Entity(tableName = "feats")
data class FeatEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    val description: String,
    val source: String,
    val abilityIncreases: List<Stat>,
    val proficiencyRequirements: List<String>,
    val statRequirements: List<Stat>,
    val raceRequirements: List<String>,
    val savingThrow: Boolean
) : Named,
    MutableIdentifiable,
    CoreConvertible<Feat> {
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