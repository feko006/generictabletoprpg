package com.feko.generictabletoprpg.features.ammunition.ui

import com.feko.generictabletoprpg.features.ammunition.Ammunition
import com.feko.generictabletoprpg.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao

class AmmunitionDetailsViewModel(
    getById: IGetByIdDao<Ammunition>
) : DetailsViewModel<Ammunition>(getById)