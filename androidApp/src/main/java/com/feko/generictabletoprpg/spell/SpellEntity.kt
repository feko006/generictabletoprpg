package com.feko.generictabletoprpg.spell

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.*

@Entity(
    tableName = "spells"
)
data class SpellEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    val description: String,
    val school: String,
    val duration: String,
    val concentration: Boolean,
    val level: Int,
    override val source: String,
    @Embedded
    val components: SpellComponentsEmbeddedEntity,
    val castingTime: String,
    val classesThatCanCast: List<String>,
    @Embedded
    val range: SpellRangeEmbeddedEntity,
    @ColumnInfo(defaultValue = "0")
    val isRitual: Boolean
) : Named,
    MutableIdentifiable,
    FromSource,
    CoreConvertible<Spell> {

    override fun toCoreModel(): Spell =
        Spell(
            id,
            name,
            description,
            school,
            duration,
            concentration,
            level,
            source,
            components.toCoreModel(),
            castingTime,
            classesThatCanCast,
            range.toCoreModel(),
            isRitual
        )

    data class SpellComponentsEmbeddedEntity(
        val verbal: Boolean,
        val somatic: Boolean,
        val material: Boolean,
        val materialComponent: String?
    ) : CoreConvertible<Spell.SpellComponents> {
        override fun toCoreModel(): Spell.SpellComponents =
            Spell.SpellComponents(
                verbal,
                somatic,
                material,
                materialComponent
            )

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
                SpellRangeEmbeddedEntity.fromCoreModel(spell.range),
                spell.isRitual
            )
    }
}