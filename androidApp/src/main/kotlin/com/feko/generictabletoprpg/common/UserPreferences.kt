package com.feko.generictabletoprpg.common

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) : IUserPreferences {
    private var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences("gttrpg-prefs", Context.MODE_PRIVATE)
    }

    override fun getIntOrDefault(key: String, defaultValue: Int): Int =
        sharedPreferences.getInt(key, defaultValue)

    override fun setInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }
}