package com.feko.generictabletoprpg.features.spell.ui

import com.feko.generictabletoprpg.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.features.spell.Spell

class SpellDetailsViewModel(getById: IGetByIdDao<Spell>) : DetailsViewModel<Spell>(getById)