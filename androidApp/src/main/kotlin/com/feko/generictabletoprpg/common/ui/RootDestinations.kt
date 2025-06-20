package com.feko.generictabletoprpg.common.ui

import androidx.annotation.StringRes
import com.feko.generictabletoprpg.R
import com.ramcosta.composedestinations.generated.destinations.EncounterScreenDestination
import com.ramcosta.composedestinations.generated.destinations.ImportScreenDestination
import com.ramcosta.composedestinations.generated.destinations.SearchAllScreenDestination
import com.ramcosta.composedestinations.generated.destinations.TrackerGroupsScreenDestination
import com.ramcosta.composedestinations.spec.Direction

enum class RootDestinations(
    @StringRes
    val title: Int,
    val direction: Direction
) {
    Tracker(R.string.tracker_title, TrackerGroupsScreenDestination()),
    Encounter(R.string.encounters, EncounterScreenDestination()),
    SearchAll(R.string.search_all_title, SearchAllScreenDestination()),
    Import(R.string.import_title, ImportScreenDestination());

    companion object {
        fun refreshables(): List<RootDestinations> =
            entries.dropLast(1)
    }
}