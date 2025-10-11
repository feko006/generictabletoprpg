package com.feko.generictabletoprpg.shared.features.basecontent.domain

interface ILoadBaseContent {
    suspend fun loadOrcbrewBaseContent(): String
    suspend fun loadJsonBaseContent(): String
}