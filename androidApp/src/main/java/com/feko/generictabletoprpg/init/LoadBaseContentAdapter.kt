package com.feko.generictabletoprpg.init

import android.content.Context
import android.content.res.Resources
import com.feko.generictabletoprpg.R

class LoadBaseContentAdapter(context: Context) : LoadBaseContentPort {
    private var resources: Resources

    init {
        resources = context.resources
    }

    override fun invoke(): String =
        resources
            .openRawResource(R.raw.base_content)
            .bufferedReader()
            .use {
                it.readText()
            }
}