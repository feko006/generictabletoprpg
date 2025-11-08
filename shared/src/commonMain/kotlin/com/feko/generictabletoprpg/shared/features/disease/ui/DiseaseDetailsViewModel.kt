package com.feko.generictabletoprpg.shared.features.disease.ui

import com.feko.generictabletoprpg.shared.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.shared.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.shared.features.disease.Disease

class DiseaseDetailsViewModel(getById: IGetByIdDao<Disease>) : DetailsViewModel<Disease>(getById)