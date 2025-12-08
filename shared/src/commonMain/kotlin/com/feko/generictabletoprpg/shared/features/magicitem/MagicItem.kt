package com.feko.generictabletoprpg.shared.features.magicitem

import com.feko.generictabletoprpg.shared.common.domain.model.IFromSource
import com.feko.generictabletoprpg.shared.common.domain.model.IIdentifiable
import com.feko.generictabletoprpg.shared.common.domain.model.INamed

interface MagicItem : IIdentifiable, INamed, IFromSource {
    val type: String
    val rarity: String
    val description: String
}