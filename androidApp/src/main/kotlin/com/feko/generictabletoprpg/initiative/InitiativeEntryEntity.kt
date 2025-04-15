package com.feko.generictabletoprpg.com.feko.generictabletoprpg.initiative

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.feko.generictabletoprpg.common.IMutableIdentifiable
import com.feko.generictabletoprpg.common.INamed

@DoNotObfuscate
@Entity(tableName = "initiative_entries")
data class InitiativeEntryEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    val initiative: Int,
    val health: Int,
    val armorClass: Int,
    val legendaryActions: Int,
    val spellSaveDc: Int,
    val spellAttackModifier: Int,
    val keepOnRefresh: Boolean,
    val hasTurn: Boolean
) : IMutableIdentifiable, INamed {
    val hasLegendaryActions
        get() = legendaryActions > 0
    val hasSpellSaveDc
        get() = spellSaveDc > 0
    val hasSpellAttackModifier
        get() = spellAttackModifier > 0
}