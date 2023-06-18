package com.feko.generictabletoprpg.tracker

class GetAllTrackedThingsUseCaseImpl(
    private val getAllTrackedThingsPort: GetAllTrackedThingsPort
) : GetAllTrackedThingsUseCase {
    override fun getAll(): List<TrackedThing> =
        getAllTrackedThingsPort.getAllSortedByName()
}