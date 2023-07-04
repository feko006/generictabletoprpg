package com.feko.generictabletoprpg.tracker

class GetAllTrackedThingsUseCaseImpl(
    private val getAllTrackedThingsByGroupPort: GetAllTrackedThingsByGroupPort
) : GetAllTrackedThingsUseCase {
    override fun getAll(parentId: Long): List<TrackedThing> =
        getAllTrackedThingsByGroupPort.getAllSortedByName(parentId)
}