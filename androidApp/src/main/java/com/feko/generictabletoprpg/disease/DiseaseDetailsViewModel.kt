package com.feko.generictabletoprpg.disease

import com.feko.generictabletoprpg.common.DetailsViewModel
import com.feko.generictabletoprpg.common.IGetById

class DiseaseDetailsViewModel(
    private val getDiseaseById: IGetById<Disease>
) : DetailsViewModel<Disease>() {
    override fun getItemById(id: Long): Disease = getDiseaseById.getById(id)
}