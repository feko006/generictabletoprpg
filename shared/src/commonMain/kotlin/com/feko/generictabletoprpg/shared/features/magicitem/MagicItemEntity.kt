package com.feko.generictabletoprpg.shared.features.magicitem

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.feko.generictabletoprpg.shared.common.domain.model.DoNotObfuscate
import com.feko.generictabletoprpg.shared.common.domain.model.ICoreConvertible
import com.feko.generictabletoprpg.shared.common.domain.model.IMutableIdentifiable

@DoNotObfuscate
@Entity(tableName = "magic_items")
data class MagicItemEntity(
    @PrimaryKey(autoGenerate = true)
    override var id: Long,
    override val name: String,
    override val source: String,
    override val type: String,
    override val rarity: String,
    override val description: String,
) : IMutableIdentifiable,
    ICoreConvertible<MagicItem>,
    MagicItem {

    override fun toCoreModel(): MagicItem = this

    companion object {
        fun fromCoreModel(item: MagicItem): MagicItemEntity =
            item as? MagicItemEntity
                ?: item.run { MagicItemEntity(id, name, source, type, rarity, description) }
    }
}