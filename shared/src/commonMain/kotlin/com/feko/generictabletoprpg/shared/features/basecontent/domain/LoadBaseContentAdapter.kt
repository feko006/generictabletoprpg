package com.feko.generictabletoprpg.shared.features.basecontent.domain

import com.feko.generictabletoprpg.shared.loadResourceAsString

class LoadBaseContentAdapter() : ILoadBaseContent {
    override suspend fun loadOrcbrewBaseContent(): String =
        loadResourceAsString("files/base_content_ob.orcbrew")

    override suspend fun loadJsonBaseContent(): String =
        loadResourceAsString("files/base_content_j.json")
}