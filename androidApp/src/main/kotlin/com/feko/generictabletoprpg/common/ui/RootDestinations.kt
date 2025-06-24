package com.feko.generictabletoprpg.common.ui

import androidx.annotation.StringRes
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.ui.components.INavigationDestination

enum class RootDestinations(
    @StringRes
    val title: Int,
    val destination: INavigationDestination
) {
    Tracker(R.string.tracker_title, INavigationDestination.TrackerGroupsDestination),
    Encounter(R.string.encounters, INavigationDestination.EncounterDestination),
    SearchAll(
        R.string.search_all_title,
        INavigationDestination.SearchAllDestination(
            filterIndex = null,
            isShownForResult = false
        )
    ),
    Import(R.string.import_title, INavigationDestination.ImportDestination)
}