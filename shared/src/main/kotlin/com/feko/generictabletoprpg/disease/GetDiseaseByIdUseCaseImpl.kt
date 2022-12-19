package com.feko.generictabletoprpg.disease

class GetDiseaseByIdUseCaseImpl(
    private val getDiseaseByIdPort: GetDiseaseByIdPort
) : GetDiseaseByIdUseCase {
    override fun getById(id: Long) = getDiseaseByIdPort.getById(id)
}