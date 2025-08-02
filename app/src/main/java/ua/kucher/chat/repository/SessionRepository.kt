package ua.kucher.chat.repository

import android.util.Log
import org.matrix.android.sdk.api.Matrix
import org.matrix.android.sdk.api.auth.data.Credentials
import org.matrix.android.sdk.api.auth.data.HomeServerConnectionConfig
import org.matrix.android.sdk.api.session.Session
import ua.kucher.chat.BuildConfig
import ua.kucher.chat.core.flatMapAsync
import ua.kucher.chat.core.mapAsync
import ua.kucher.chat.source.local.auth.AuthDatastore

interface SessionRepository {

    suspend fun loginWithPassword(login: String, password: String): Result<Session>

    suspend fun createSessionFromSso(): Result<Session>

    class Impl(
        private val matrix: Matrix,
        private val authDatastore: AuthDatastore
    ) : SessionRepository {

        companion object {
            private const val TAG = "Session"
        }

        private val connectionConfig: HomeServerConnectionConfig
            get() = HomeServerConnectionConfig
                .Builder()
                .withHomeServerUri(BuildConfig.HOME_SERVER)
                .build()

        override suspend fun createSessionFromSso() =
            authDatastore.getUserId().flatMapAsync { userId ->
                authDatastore.getDeviceId().flatMapAsync { deviceId ->
                    authDatastore.getAccessToken().flatMapAsync { accessToken ->
                        authDatastore.getRefreshToken().mapAsync { refreshToken ->
                            matrix.authenticationService().createSessionFromSso(
                                homeServerConnectionConfig = connectionConfig,
                                credentials = Credentials(
                                    userId = userId,
                                    deviceId = deviceId,
                                    accessToken = accessToken,
                                    refreshToken = refreshToken,
                                    homeServer = BuildConfig.HOME_SERVER,
                                )
                            ).apply {
                                Log.d(TAG, sessionParams.credentials.toString())
                                open()
                                syncService().startSync(true)
                            }
                        }
                    }
                }
            }

        override suspend fun loginWithPassword(
            login: String,
            password: String
        ) = runCatching {
            matrix.authenticationService().directAuthentication(
                homeServerConnectionConfig = connectionConfig,
                password = password,
                matrixId = login,
                initialDeviceName = BuildConfig.DEVICE_ID
            ).apply {
                open()
                syncService().startSync(true)
            }
        }.flatMapAsync { session ->
            val credentials = session.sessionParams.credentials
            Log.d(TAG, credentials.toString())
            authDatastore.saveAccessToken(credentials.accessToken).flatMapAsync {
                authDatastore.saveRefreshToken(credentials.refreshToken).flatMapAsync {
                    authDatastore.saveUserId(credentials.userId).flatMapAsync {
                        authDatastore.saveDeviceId(credentials.deviceId)
                    }
                }.map { session }
            }
        }
    }
}