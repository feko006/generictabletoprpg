package com.feko.generictabletoprpg.features.tracker.domain.model

import androidx.annotation.Keep

@Keep
abstract class GenericTrackedThing<T>(
    id: Long,
    name: String,
    var amount: T,
    type: Type,
    index: Int,
    groupId: Long
) : TrackedThing(id, name, amount.toString(), type, index, groupId)
