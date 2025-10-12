package com.feko.generictabletoprpg.shared.common.ui

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.encounters
import com.feko.generictabletoprpg.import_title
import com.feko.generictabletoprpg.search_all_title
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination
import com.feko.generictabletoprpg.tracker_title
import org.jetbrains.compose.resources.StringResource

enum class RootDestinations(
    val title: StringResource,
    val destination: INavigationDestination
) {
    Tracker(Res.string.tracker_title, INavigationDestination.TrackerGroupsDestination),
    Encounter(Res.string.encounters, INavigationDestination.EncounterDestination),
    SearchAll(
        Res.string.search_all_title,
        INavigationDestination.SearchAllDestination(
            filterIndex = null,
            isShownForResult = false
        )
    ),
    Import(Res.string.import_title, INavigationDestination.ImportDestination)
}