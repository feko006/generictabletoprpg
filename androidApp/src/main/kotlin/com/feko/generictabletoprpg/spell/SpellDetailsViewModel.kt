package com.feko.generictabletoprpg.spell

import com.feko.generictabletoprpg.common.DetailsViewModel
import com.feko.generictabletoprpg.common.IGetById

class SpellDetailsViewModel(getById: IGetById<Spell>) : DetailsViewModel<Spell>(getById)