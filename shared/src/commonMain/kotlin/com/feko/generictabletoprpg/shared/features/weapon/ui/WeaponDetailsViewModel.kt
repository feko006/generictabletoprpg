package com.feko.generictabletoprpg.shared.features.weapon.ui

import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.shared.features.weapon.Weapon

class WeaponDetailsViewModel(getById: IGetByIdDao<Weapon>) : DetailsViewModel<Weapon>(getById)