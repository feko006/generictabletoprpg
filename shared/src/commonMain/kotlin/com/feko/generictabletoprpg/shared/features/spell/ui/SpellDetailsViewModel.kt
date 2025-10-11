package com.feko.generictabletoprpg.shared.features.spell.ui

import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.shared.features.spell.Spell

class SpellDetailsViewModel(getById: IGetByIdDao<Spell>) : DetailsViewModel<Spell>(getById)