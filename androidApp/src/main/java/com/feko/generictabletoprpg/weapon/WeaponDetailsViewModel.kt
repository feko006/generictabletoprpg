package com.feko.generictabletoprpg.weapon

import com.feko.generictabletoprpg.common.DetailsViewModel
import com.feko.generictabletoprpg.common.IGetById

class WeaponDetailsViewModel(getById: IGetById<Weapon>) : DetailsViewModel<Weapon>(getById)