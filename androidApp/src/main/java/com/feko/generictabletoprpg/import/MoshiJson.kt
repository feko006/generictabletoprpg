package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.tracker.Ability
import com.feko.generictabletoprpg.tracker.Health
import com.feko.generictabletoprpg.tracker.Number
import com.feko.generictabletoprpg.tracker.Percentage
import com.feko.generictabletoprpg.tracker.SpellSlot
import com.feko.generictabletoprpg.tracker.TrackedThing
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.EnumJsonAdapter
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class MoshiJson : IJson {
    private val moshiJson =
        Moshi.Builder()
            // Has to be first, otherwise Moshi complains...
            .add(
                PolymorphicJsonAdapterFactory
                    .of(TrackedThing::class.java, TrackedThing::type.name)
                    .withSubtype(Ability::class.java, TrackedThing.Type.Ability.name)
                    .withSubtype(Health::class.java, TrackedThing.Type.Health.name)
                    .withSubtype(Number::class.java, TrackedThing.Type.Number.name)
                    .withSubtype(Percentage::class.java, TrackedThing.Type.Percentage.name)
                    .withSubtype(SpellSlot::class.java, TrackedThing.Type.SpellSlot.name)
            )
            .add(KotlinJsonAdapterFactory())
            .add(
                TrackedThing.Type::class.java,
                EnumJsonAdapter.create(TrackedThing.Type::class.java)
            )
            .build()

    @Suppress("UNCHECKED_CAST")
    override fun <T> from(content: String, type: Class<T>): T =
        moshiJson.adapter(type).fromJson(content) as T

    override fun <T> to(data: T, type: Class<T>): String =
        moshiJson.adapter(type).toJson(data)
}