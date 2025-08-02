package ua.kucher.chat.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.kucher.chat.source.local.DatastoreImpl
import ua.kucher.chat.source.local.auth.AuthDatastore

@InstallIn(SingletonComponent::class)
@Module
object LocalModule {

    @Provides
    fun providesAuthDataStore(impl: DatastoreImpl): AuthDatastore = impl

}