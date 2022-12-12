package com.feko.generictabletoprpg.feat

class GetAllFeatsUseCaseImpl(
    private val getAllFeatsPort: GetAllFeatsPort
) : GetAllFeatsUseCase {
    override fun invoke() = getAllFeatsPort.getAllSortedByName()
}