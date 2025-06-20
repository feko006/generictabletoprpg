package com.feko.generictabletoprpg.features.feat.ui

import com.feko.generictabletoprpg.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.features.feat.Feat


class FeatDetailsViewModel(getById: IGetByIdDao<Feat>) : DetailsViewModel<Feat>(getById)