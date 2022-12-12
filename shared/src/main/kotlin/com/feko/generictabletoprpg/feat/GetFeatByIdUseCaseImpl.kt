package com.feko.generictabletoprpg.feat

class GetFeatByIdUseCaseImpl(
    private val getFeatByIdPort: GetFeatByIdPort
) : GetFeatByIdUseCase {
    override fun invoke(id: Long) = getFeatByIdPort.getById(id)
}