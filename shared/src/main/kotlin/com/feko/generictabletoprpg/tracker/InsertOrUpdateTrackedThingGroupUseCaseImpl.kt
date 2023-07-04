package com.feko.generictabletoprpg.tracker

class InsertOrUpdateTrackedThingGroupUseCaseImpl(
    private val insertOrUpdateTrackedThingGroupPort: InsertOrUpdateTrackedThingGroupPort
) : InsertOrUpdateTrackedThingGroupUseCase {
    override fun insertOrUpdate(item: TrackedThingGroup): Long =
        insertOrUpdateTrackedThingGroupPort.insertOrUpdate(item)
}