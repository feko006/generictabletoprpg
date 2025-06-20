package com.feko.generictabletoprpg.features.disease.ui

import com.feko.generictabletoprpg.common.ui.viewmodel.DetailsViewModel
import com.feko.generictabletoprpg.common.data.local.IGetByIdDao
import com.feko.generictabletoprpg.features.disease.Disease

class DiseaseDetailsViewModel(getById: IGetByIdDao<Disease>) : DetailsViewModel<Disease>(getById)