package com.feko.generictabletoprpg.shared.common.domain.model

interface IMutableIdentifiable : IIdentifiable {
    override var id: Long
}