package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.Identifiable

class DeleteTrackedThingUseCaseImpl(
    private val deletePort: DeleteTrackedThingPort
) : DeleteTrackedThingUseCase {
    override fun delete(item: Identifiable) = deletePort.delete(item.id)
}