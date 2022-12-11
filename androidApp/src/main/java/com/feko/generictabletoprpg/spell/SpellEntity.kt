package com.feko.generictabletoprpg.spell

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.com.feko.generictabletoprpg.common.RangeEmbeddedEntity
import com.feko.generictabletoprpg.spells.Spell

@Entity(
    tableName = "spells"
)
data class SpellEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    val name: String,
    val description: String,
    val school: String,
    val duration: String,
    val concentration: Boolean,
    val level: Int,
    val source: String,
    @Embedded
    val components: SpellComponentsEmbeddedEntity,
    val castingTime: String,
    val classesThatCanCast: List<String>,
    @Embedded
    val range: RangeEmbeddedEntity
) {
    data class SpellComponentsEmbeddedEntity(
        val verbal: Boolean,
        val somatic: Boolean,
        val material: Boolean,
        val materialComponent: String?
    ) {
        companion object {
            fun fromCoreModel(spellComponents: Spell.SpellComponents) =
                SpellComponentsEmbeddedEntity(
                    spellComponents.verbal,
                    spellComponents.somatic,
                    spellComponents.material,
                    spellComponents.materialComponent
                )
        }
    }

    companion object {
        fun fromCoreModel(spell: Spell) =
            SpellEntity(
                spell.id,
                spell.name,
                spell.description,
                spell.school,
                spell.duration,
                spell.concentration,
                spell.level,
                spell.source,
                SpellComponentsEmbeddedEntity.fromCoreModel(spell.components),
                spell.castingTime,
                spell.classesThatCanCast,
                RangeEmbeddedEntity.fromCoreModel(spell.range)
            )
    }
}