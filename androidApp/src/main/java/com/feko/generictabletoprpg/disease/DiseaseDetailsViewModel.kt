package com.feko.generictabletoprpg.disease

import com.feko.generictabletoprpg.common.DetailsViewModel

class DiseaseDetailsViewModel(
    private val getDiseaseByIdUseCase: GetDiseaseByIdUseCase
) : DetailsViewModel<Disease>() {
    override fun getItemById(id: Long): Disease = getDiseaseByIdUseCase.getById(id)
}