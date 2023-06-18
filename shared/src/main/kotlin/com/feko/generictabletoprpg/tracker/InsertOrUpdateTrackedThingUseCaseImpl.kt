package com.feko.generictabletoprpg.tracker

class InsertOrUpdateTrackedThingUseCaseImpl(
    private val insertOrUpdateTrackedThingsPort: InsertOrUpdateTrackedThingPort
) : InsertOrUpdateTrackedThingUseCase {
    override fun insertOrUpdate(item: TrackedThing): Long =
        insertOrUpdateTrackedThingsPort.insertOrUpdate(item)
}