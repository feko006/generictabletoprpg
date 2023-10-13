package com.feko.generictabletoprpg

import androidx.annotation.StringRes
import com.feko.generictabletoprpg.destinations.ImportScreenDestination
import com.feko.generictabletoprpg.destinations.SearchAllScreenDestination
import com.feko.generictabletoprpg.destinations.TrackerGroupsScreenDestination
import com.ramcosta.composedestinations.spec.Direction

enum class RootDestinations(
    @StringRes
    val title: Int,
    val direction: Direction
) {
    Tracker(R.string.tracker_title, TrackerGroupsScreenDestination()),
    SearchAll(R.string.search_all_title, SearchAllScreenDestination()),
    Import(R.string.import_title, ImportScreenDestination());

    companion object {
        fun refreshables(): List<RootDestinations> =
            entries.dropLast(1)
    }
}