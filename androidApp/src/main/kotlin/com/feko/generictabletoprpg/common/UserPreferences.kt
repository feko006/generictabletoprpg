package com.feko.generictabletoprpg.common

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreferences(context: Context) : IUserPreferences {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("gttrpg-prefs", Context.MODE_PRIVATE)

    override fun getIntOrDefault(key: String, defaultValue: Int): Int =
        sharedPreferences.getInt(key, defaultValue)

    override fun setInt(key: String, value: Int) =
        sharedPreferences.edit { putInt(key, value) }
}