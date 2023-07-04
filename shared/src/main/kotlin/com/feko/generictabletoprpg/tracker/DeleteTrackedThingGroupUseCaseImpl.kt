package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.Identifiable

class DeleteTrackedThingGroupUseCaseImpl(
    private val deleteTrackedThingGroupPort: DeleteTrackedThingGroupPort
) : DeleteTrackedThingGroupUseCase {
    override fun delete(item: Identifiable) = deleteTrackedThingGroupPort.delete(item.id)
}