package ua.kucher.chat.source.local.auth

import androidx.datastore.preferences.core.stringPreferencesKey

interface AuthDatastore {

    companion object {
        val userId = stringPreferencesKey("userId")
        val accessToken = stringPreferencesKey("accessToken")
        val refreshToken = stringPreferencesKey("refresh_token")
        val deviceId = stringPreferencesKey("device_id")
    }

    suspend fun saveAccessToken(token: String): Result<Unit>

    suspend fun saveRefreshToken(token: String?): Result<Unit>

    suspend fun saveUserId(id: String): Result<Unit>

    suspend fun saveDeviceId(id: String): Result<Unit>

    suspend fun getAccessToken(): Result<String>

    suspend fun getRefreshToken(): Result<String?>

    suspend fun getUserId(): Result<String>

    suspend fun getDeviceId(): Result<String>

}