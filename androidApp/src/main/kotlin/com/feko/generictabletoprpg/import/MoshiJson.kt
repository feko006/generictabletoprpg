package com.feko.generictabletoprpg.import

import com.feko.generictabletoprpg.tracker.Ability
import com.feko.generictabletoprpg.tracker.Health
import com.feko.generictabletoprpg.tracker.Number
import com.feko.generictabletoprpg.tracker.Percentage
import com.feko.generictabletoprpg.tracker.SpellList
import com.feko.generictabletoprpg.tracker.SpellSlot
import com.feko.generictabletoprpg.tracker.Text
import com.feko.generictabletoprpg.tracker.TrackedThing
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
                    .withSubtype(Ability::class.java, TrackedThing.Type.Ability.name)
                    .withSubtype(Health::class.java, TrackedThing.Type.Health.name)
                    .withSubtype(Number::class.java, TrackedThing.Type.Number.name)
                    .withSubtype(Percentage::class.java, TrackedThing.Type.Percentage.name)
                    .withSubtype(SpellSlot::class.java, TrackedThing.Type.SpellSlot.name)
                    .withSubtype(SpellList::class.java, TrackedThing.Type.SpellList.name)
                    .withSubtype(Text::class.java, TrackedThing.Type.Text.name)
            )
            .add(KotlinJsonAdapterFactory())
            .build()

    override fun <T> from(content: String, type: Type): T =
        moshiJson.adapter<T>(type).fromJson(content)!!

    override fun <T> to(data: T, type: Type): String =
        moshiJson.adapter<T>(type).toJson(data)
}