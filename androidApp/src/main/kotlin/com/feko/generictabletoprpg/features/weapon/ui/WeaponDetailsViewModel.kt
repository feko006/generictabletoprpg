package com.feko.generictabletoprpg.features.weapon.ui

import com.feko.generictabletoprpg.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.features.weapon.Weapon

class WeaponDetailsViewModel(getById: IGetByIdDao<Weapon>) : DetailsViewModel<Weapon>(getById)