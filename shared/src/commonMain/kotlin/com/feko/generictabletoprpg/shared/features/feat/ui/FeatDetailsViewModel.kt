package com.feko.generictabletoprpg.shared.features.feat.ui

import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.shared.features.feat.Feat

class FeatDetailsViewModel(getById: IGetByIdDao<Feat>) : DetailsViewModel<Feat>(getById)