package com.feko.generictabletoprpg.com.feko.generictabletoprpg.initiative

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.common.DoNotObfuscate
import com.feko.generictabletoprpg.common.IMutableIdentifiable
import com.feko.generictabletoprpg.common.INamed

@Immutable
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

    val hasLegendaryActions by lazy { legendaryActions > 0 }
    val hasSpellSaveDc by lazy { spellSaveDc > 0 }
    val hasSpellAttackModifier by lazy { spellAttackModifier > 0 }
    val isSavedInDatabase by lazy { id > 0L }
    val isLairAction by lazy {
        (initiative == 20
                && armorClass == 0
                && health == 0)
    }
    val isNameValid by lazy { name.isNotEmpty() }
    val isArmorClassValid by lazy { armorClass > 0 }
    val isHealthValid by lazy { health > 0 }
    val isEntryValid by lazy { isNameValid && isArmorClassValid && isHealthValid }

    companion object {
        val Empty = InitiativeEntryEntity(0L, "", 0, 0, 0, 0, 0, 0, false, false)
        fun createLairActionEntry(): InitiativeEntryEntity = Empty.copy(initiative = 20)
    }
}