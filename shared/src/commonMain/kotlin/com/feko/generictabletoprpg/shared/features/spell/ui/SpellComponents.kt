package com.feko.generictabletoprpg.shared.features.spell.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.Res
import com.feko.generictabletoprpg.casting_time
import com.feko.generictabletoprpg.classes
import com.feko.generictabletoprpg.components
import com.feko.generictabletoprpg.duration
import com.feko.generictabletoprpg.level
import com.feko.generictabletoprpg.name
import com.feko.generictabletoprpg.range
import com.feko.generictabletoprpg.school
import com.feko.generictabletoprpg.shared.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.shared.features.spell.Spell

@Composable
fun ColumnScope.SpellDetailsContent(item: Spell) {
    item.run {
        TextWithLabel(Res.string.name, name)
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