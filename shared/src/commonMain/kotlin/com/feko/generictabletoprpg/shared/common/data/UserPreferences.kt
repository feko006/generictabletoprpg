package com.feko.generictabletoprpg.shared.common.data

import com.feko.generictabletoprpg.shared.common.domain.IUserPreferences
import com.feko.generictabletoprpg.shared.settings

class UserPreferences() : IUserPreferences {
    override fun getIntOrDefault(key: String, defaultValue: Int): Int =
        settings.getInt(key, defaultValue)

    override fun setInt(key: String, value: Int) =
        settings.putInt(key, value)
}