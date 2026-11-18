package com.feko.generictabletoprpg.shared.features.encounter

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.shared.common.domain.model.IMutableIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed

@Immutable
@DoNotObfuscate
@Entity(tableName = "encounters")
data class EncounterEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    val round: Int = 1
) : IMutableIdentifiable, INamed