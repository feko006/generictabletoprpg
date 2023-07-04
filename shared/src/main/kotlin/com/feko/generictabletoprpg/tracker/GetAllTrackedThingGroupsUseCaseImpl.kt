package com.feko.generictabletoprpg.tracker

class GetAllTrackedThingGroupsUseCaseImpl(
    private val getAllTrackedThingGroupsPort: GetAllTrackedThingGroupsPort
) : GetAllTrackedThingGroupsUseCase {
    override fun getAll(): List<TrackedThingGroup> =
        getAllTrackedThingGroupsPort.getAllSortedByName()
}