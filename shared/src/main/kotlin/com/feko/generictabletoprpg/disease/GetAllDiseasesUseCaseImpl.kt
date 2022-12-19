package com.feko.generictabletoprpg.disease

class GetAllDiseasesUseCaseImpl(
    private val getAllDiseasesPort: GetAllDiseasesPort
) : GetAllDiseasesUseCase {
    override fun getAll() = getAllDiseasesPort.getAllSortedByName()
}