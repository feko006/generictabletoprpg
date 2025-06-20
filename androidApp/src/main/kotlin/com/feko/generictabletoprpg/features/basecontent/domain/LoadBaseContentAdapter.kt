package com.feko.generictabletoprpg.features.basecontent.domain

import android.content.Context
import android.content.res.Resources
import com.feko.generictabletoprpg.R

class LoadBaseContentAdapter(context: Context) : ILoadBaseContent {
    private var resources: Resources = context.resources

    override fun loadOrcbrewBaseContent(): String =
        resources
            .openRawResource(R.raw.base_content_ob)
            .bufferedReader()
            .use {
                it.readText()
            }

    override fun loadJsonBaseContent(): String =
        resources
            .openRawResource(R.raw.base_content_j)
            .bufferedReader()
            .use {
                it.readText()
            }
}