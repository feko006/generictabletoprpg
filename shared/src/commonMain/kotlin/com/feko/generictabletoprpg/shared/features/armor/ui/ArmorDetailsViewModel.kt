package com.feko.generictabletoprpg.shared.features.armor.ui

import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.shared.features.armor.Armor

class ArmorDetailsViewModel(getById: IGetByIdDao<Armor>) : DetailsViewModel<Armor>(getById)