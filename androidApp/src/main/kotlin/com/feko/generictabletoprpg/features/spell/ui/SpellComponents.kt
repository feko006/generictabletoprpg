package com.feko.generictabletoprpg.features.spell.ui

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.feko.generictabletoprpg.R
import com.feko.generictabletoprpg.common.ui.components.TextWithLabel
import com.feko.generictabletoprpg.features.spell.Spell

@Composable
fun ColumnScope.SpellDetailsContent(item: Spell) {
    item.run {
        TextWithLabel(R.string.name, name)
        TextWithLabel(R.string.level, level.toString())
        TextWithLabel(R.string.school, school)
        TextWithLabel(R.string.casting_time, castingTimeWithRitualTag)
        TextWithLabel(R.string.range, range.toString())
        if (hasComponents) {
            TextWithLabel(R.string.components, components.toString())
        }
        TextWithLabel(R.string.duration, duration)
        if (classesThatCanCast.isNotEmpty()) {
            TextWithLabel(R.string.classes, classesThatCanCast.joinToString())
        }
        HorizontalDivider()
        Text(description)
    }
}
