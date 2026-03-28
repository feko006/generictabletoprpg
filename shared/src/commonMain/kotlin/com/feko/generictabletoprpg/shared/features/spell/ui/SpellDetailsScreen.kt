package com.feko.generictabletoprpg.shared.features.spell.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.casting_time
import com.feko.generictabletoprpg.classes
import com.feko.generictabletoprpg.components
import com.feko.generictabletoprpg.duration
import com.feko.generictabletoprpg.level
import com.feko.generictabletoprpg.range
import com.feko.generictabletoprpg.school
import com.feko.generictabletoprpg.shared.common.domain.model.IText.StringText.Companion.asText
import com.feko.generictabletoprpg.shared.common.ui.components.DetailsScreen
import com.feko.generictabletoprpg.shared.common.ui.components.GttrpgTopAppBar
import com.feko.generictabletoprpg.shared.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.shared.features.spell.Spell
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SpellDetailsScreen(
    id: Long,
    onNavigationIconClick: () -> Unit
) {
    DetailsScreen<SpellDetailsViewModel, Spell>(
        id,
        koinViewModel(),
        onNavigationIconClick
    ) { spell ->
        SpellDetailsContent(spell)
    }
}

@Composable
fun SimpleSpellDetailsScreen(
    spell: Spell,
    onNavigationIconClick: () -> Unit,
    onNavigateBack: () -> Unit
) {
    NavigationBackHandler(
        rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = true,
        onBackCompleted = onNavigateBack
    )
    Scaffold(
        topBar = { GttrpgTopAppBar(spell.name.asText(), onNavigationIconClick) }
    ) { it: PaddingValues ->
        DetailsScreen(spell, Modifier.padding(it)) {
            SpellDetailsContent(spell)
        }
    }
}

@Composable
private fun SpellDetailsContent(item: Spell) {
    item.run {
        TextWithLabel(Res.string.level, level.toString())
        TextWithLabel(Res.string.school, school)
        TextWithLabel(Res.string.casting_time, castingTimeWithRitualTag)
        TextWithLabel(Res.string.range, range.toString())
        if (hasComponents) {
            TextWithLabel(Res.string.components, components.toString())
        }
        TextWithLabel(Res.string.duration, duration)
        if (classesThatCanCast.isNotEmpty()) {
            TextWithLabel(Res.string.classes, classesThatCanCast.joinToString())
        }
        HorizontalDivider()
        Text(description)
    }
}
