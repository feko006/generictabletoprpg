package com.feko.generictabletoprpg.feat

interface GetFeatByIdPort {
    fun getById(featId: Long): Feat
}