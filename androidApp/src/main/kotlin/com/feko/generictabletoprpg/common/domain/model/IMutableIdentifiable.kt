package com.feko.generictabletoprpg.common.domain.model

interface IMutableIdentifiable : IIdentifiable {
    override var id: Long
}