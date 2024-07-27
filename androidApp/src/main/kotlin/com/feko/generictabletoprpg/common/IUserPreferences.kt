package com.feko.generictabletoprpg.common

interface IUserPreferences {
    fun getIntOrDefault(key: String, defaultValue: Int): Int
    fun setInt(key: String, value: Int)
}
