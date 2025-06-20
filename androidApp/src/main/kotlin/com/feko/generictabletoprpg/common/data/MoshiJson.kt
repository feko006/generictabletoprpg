package com.feko.generictabletoprpg.common.data

import com.feko.generictabletoprpg.common.domain.IJson
import com.feko.generictabletoprpg.features.tracker.domain.model.AbilityTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.HealthTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.HitDiceTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.NumberTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.PercentageTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.SpellListTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.SpellSlotTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.StatsTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.TextTrackedThing
import com.feko.generictabletoprpg.features.tracker.domain.model.TrackedThing
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

class MoshiJson : IJson {
    private val moshiJson =
        Moshi.Builder()
            .add(
                TrackedThing.Type::class.java,
                EnumJsonAdapter.create(TrackedThing.Type::class.java)
            )
            .add(
                PolymorphicJsonAdapterFactory
                    .of(TrackedThing::class.java, TrackedThing::type.name)
                    .withSubtype(AbilityTrackedThing::class.java, TrackedThing.Type.Ability.name)
                    .withSubtype(HealthTrackedThing::class.java, TrackedThing.Type.Health.name)
                    .withSubtype(NumberTrackedThing::class.java, TrackedThing.Type.Number.name)
                    .withSubtype(PercentageTrackedThing::class.java, TrackedThing.Type.Percentage.name)
                    .withSubtype(SpellSlotTrackedThing::class.java, TrackedThing.Type.SpellSlot.name)
                    .withSubtype(SpellListTrackedThing::class.java, TrackedThing.Type.SpellList.name)
                    .withSubtype(TextTrackedThing::class.java, TrackedThing.Type.Text.name)
                    .withSubtype(HitDiceTrackedThing::class.java, TrackedThing.Type.HitDice.name)
                    .withSubtype(StatsTrackedThing::class.java, TrackedThing.Type.FiveEStats.name)
            )
            .add(KotlinJsonAdapterFactory())
            .build()

    override fun <T> from(content: String, type: Type): T =
        moshiJson.adapter<T>(type).fromJson(content)!!

    override fun <T> to(data: T, type: Type): String =
        moshiJson.adapter<T>(type).toJson(data)
}