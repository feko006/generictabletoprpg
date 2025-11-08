package com.feko.generictabletoprpg.shared.common.ui

import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.encounters
import com.feko.generictabletoprpg.import_title
import com.feko.generictabletoprpg.search_all_title
import com.feko.generictabletoprpg.shared.common.domain.model.IIcon
import com.feko.generictabletoprpg.shared.common.domain.model.IIcon.DrawableResourceIcon.Companion.asIcon
import com.feko.generictabletoprpg.shared.common.domain.model.IIcon.ImageVectorIcon.Companion.asIcon
import com.feko.generictabletoprpg.shared.common.ui.components.INavigationDestination
import com.feko.generictabletoprpg.shared.common.ui.components.dashboardIcon
import com.feko.generictabletoprpg.shared.common.ui.components.inputIcon
import com.feko.generictabletoprpg.shared.common.ui.components.searchIcon
import com.feko.generictabletoprpg.swords
import com.feko.generictabletoprpg.tracker_title
import org.jetbrains.compose.resources.StringResource

enum class RootDestinations(
    val title: StringResource,
    val destination: INavigationDestination,
    val selectedIcon: IIcon
) {
    Tracker(
        Res.string.tracker_title,
        INavigationDestination.TrackerGroupsDestination,
        dashboardIcon.asIcon()
    ),
    Encounter(
        Res.string.encounters,
        INavigationDestination.EncounterDestination,
        Res.drawable.swords.asIcon()
    ),
    SearchAll(
        Res.string.search_all_title,
        INavigationDestination.SearchAllDestination(
            filterIndex = null,
            isShownForResult = false
        ),
        searchIcon.asIcon()
    ),
    Import(
        Res.string.import_title,
        INavigationDestination.ImportDestination,
        inputIcon.asIcon()
    )
}