package com.feko.generictabletoprpg.features.tracker.domain.model

import com.feko.generictabletoprpg.common.domain.model.DoNotObfuscate

@DoNotObfuscate
abstract class GenericTrackedThing<T>(
    id: Long,
    name: String,
    var amount: T,
    type: Type,
    index: Int,
    groupId: Long
) : TrackedThing(id, name, amount.toString(), type, index, groupId)
