package com.feko.generictabletoprpg.tracker

import com.feko.generictabletoprpg.common.DoNotObfuscate

@DoNotObfuscate
abstract class GenericTrackedThing<T>(
    id: Long,
    name: String,
    var amount: T,
    type: Type,
    index: Int,
    groupId: Long
) : TrackedThing(id, name, amount.toString(), type, index, groupId)
