package com.illidant.easykanzicapstone.platform.source.local

import com.illidant.easykanzicapstone.platform.source.UserDataSource

class UserLocalDataSource(
    private val prefs: SharedPrefs
) : UserDataSource.Local {

    override fun saveToken(token: String) {
        prefs.token = token
    }

    companion object {
        private var INSTANCE: UserDataSource.Local? = null

        fun getInstance(prefs: SharedPrefs): UserDataSource.Local {
            return INSTANCE ?: synchronized(UserLocalDataSource::class.java) {
                val instance = UserLocalDataSource(prefs)
                INSTANCE = instance
                instance
            }
        }
    }
}
