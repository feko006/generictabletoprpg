package com.feko.generictabletoprpg.shared.features.magicitem.ui

import com.feko.generictabletoprpg.shared.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.shared.features.magicitem.MagicItem
import com.feko.generictabletoprpg.shared.features.magicitem.MagicItemDao

class MagicItemViewModel(private val dao: MagicItemDao) : DetailsViewModel<MagicItem>(dao) {
}