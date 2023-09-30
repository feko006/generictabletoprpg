package com.feko.generictabletoprpg.tracker

abstract class GenericTrackedThing<T>(
    id: Long,
    name: String,
    var amount: T,
    type: Type,
    index: Int,
    groupId: Long
) : TrackedThing(id, name, amount.toString(), type, index, groupId)
