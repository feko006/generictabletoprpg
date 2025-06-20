package com.feko.generictabletoprpg.common.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.feko.generictabletoprpg.common.domain.IUserPreferences

class UserPreferences(context: Context) : IUserPreferences {
    private var sharedPreferences: SharedPreferences =
        context.getSharedPreferences("gttrpg-prefs", Context.MODE_PRIVATE)

    override fun getIntOrDefault(key: String, defaultValue: Int): Int =
        sharedPreferences.getInt(key, defaultValue)

    override fun setInt(key: String, value: Int) =
        sharedPreferences.edit { putInt(key, value) }
}