package com.example.proyectoalmacen.core.preferences

import android.content.Context


class Preferens(val context: Context) {
    val SHARED_NAME = "MyConfig"
    val SHARED_USER_NAME = "username"
    val SHARED_USER_ID = "userId"
    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveUserId(userId: String = "1") {
        storage.edit().putString(SHARED_USER_ID, userId).apply()
    }
    fun getUserId(): String {
        return storage.getString(SHARED_USER_ID, "")!!
    }

    fun saveName(name: String = "default") {
        storage.edit().putString(SHARED_USER_NAME, name).apply()
    }
    fun getName(): String {
        return storage.getString(SHARED_USER_NAME, "")!!
    }
}