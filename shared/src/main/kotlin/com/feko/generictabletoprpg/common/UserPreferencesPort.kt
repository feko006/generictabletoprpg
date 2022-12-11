package com.feko.generictabletoprpg.common

interface UserPreferencesPort {
    fun getIntOrDefault(key: String, defaultValue: Int): Int
    fun setInt(key: String, value: Int)
}
