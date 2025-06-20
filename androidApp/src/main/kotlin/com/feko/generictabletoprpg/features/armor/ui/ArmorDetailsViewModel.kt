package com.feko.generictabletoprpg.features.armor.ui

import com.feko.generictabletoprpg.features.armor.Armor
import com.feko.generictabletoprpg.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.common.data.local.IGetByIdDao

class ArmorDetailsViewModel(getById: IGetByIdDao<Armor>) : DetailsViewModel<Armor>(getById)