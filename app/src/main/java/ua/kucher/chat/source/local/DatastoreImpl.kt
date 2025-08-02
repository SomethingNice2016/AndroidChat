package ua.kucher.chat.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import ua.kucher.chat.core.flatMapAsync
import ua.kucher.chat.core.toUnit
import ua.kucher.chat.source.local.auth.AuthDatastore
import javax.inject.Inject
import javax.inject.Singleton


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ChatSettings")

@Singleton
class DatastoreImpl @Inject constructor(@ApplicationContext private val context: Context) :
    AuthDatastore {

    private val datastore = context.dataStore

    override suspend fun saveAccessToken(token: String) = runCatching {
        datastore.edit { preferences ->
            preferences[AuthDatastore.accessToken] = token
        }
    }.toUnit()

    override suspend fun saveRefreshToken(token: String?) = runCatching {
        datastore.edit { preferences ->
            preferences[AuthDatastore.refreshToken] = token ?: ""
        }
    }.toUnit()

    override suspend fun saveUserId(id: String) = runCatching {
        datastore.edit { preferences ->
            preferences[AuthDatastore.userId] = id
        }
    }.toUnit()

    override suspend fun saveDeviceId(id: String) = runCatching {
        datastore.edit { preferences ->
            preferences[AuthDatastore.deviceId] = id
        }
    }.toUnit()

    override suspend fun getAccessToken() = runCatching {
        datastore.data.map { preferences ->
            preferences[AuthDatastore.accessToken]
        }.firstOrNull()
    }.flatMapAsync { token ->
        token?.let { nonNullToken ->
            Result.success(nonNullToken)
        } ?: Result.failure(Exception("Non authorized"))
    }

    override suspend fun getRefreshToken() = runCatching {
        datastore.data.map { preferences ->
            preferences[AuthDatastore.refreshToken]
        }.firstOrNull()
    }

    override suspend fun getUserId() = runCatching {
        datastore.data.map { preferences ->
            preferences[AuthDatastore.userId]
        }.firstOrNull()
    }.flatMapAsync { id ->
        id?.let { nonNullId ->
            Result.success(nonNullId)
        } ?: Result.failure(Exception("Non authorized"))
    }

    override suspend fun getDeviceId() = runCatching {
        datastore.data.map { preferences ->
            preferences[AuthDatastore.deviceId]
        }.firstOrNull()
    }.flatMapAsync { id ->
        id?.let { nonNullId ->
            Result.success(nonNullId)
        } ?: Result.failure(Exception("Non authorized"))
    }
}