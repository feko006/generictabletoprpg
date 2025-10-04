package com.feko.generictabletoprpg.shared.common.domain

interface IUserPreferences {
    fun getIntOrDefault(key: String, defaultValue: Int): Int
    fun setInt(key: String, value: Int)
}