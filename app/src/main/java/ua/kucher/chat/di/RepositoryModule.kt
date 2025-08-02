package ua.kucher.chat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.matrix.android.sdk.api.Matrix
import ua.kucher.chat.repository.RoomRepository
import ua.kucher.chat.repository.SessionRepository
import ua.kucher.chat.source.local.auth.AuthDatastore
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Singleton
    @Provides
    fun providesSessionRepository(
        matrix: Matrix,
        datastore: AuthDatastore
    ): SessionRepository = SessionRepository.Impl(matrix, datastore)

    @Singleton
    @Provides
    fun providesRoomRepository(sessionRepository: SessionRepository): RoomRepository =
        RoomRepository.Impl(sessionRepository)
}