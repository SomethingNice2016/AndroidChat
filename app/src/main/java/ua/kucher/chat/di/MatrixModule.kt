package ua.kucher.chat.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.matrix.android.sdk.api.Matrix
import org.matrix.android.sdk.api.MatrixConfiguration
import org.matrix.android.sdk.api.provider.RoomDisplayNameFallbackProvider
import ua.kucher.chat.R
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object MatrixModule {

    @Singleton
    @Provides
    fun providesMatrix(
        @ApplicationContext
        context: Context,
        displayNameFallbackProvider: RoomDisplayNameFallbackProvider
    ) = Matrix(
        context = context,
        matrixConfiguration = MatrixConfiguration(
            roomDisplayNameFallbackProvider = displayNameFallbackProvider
        )
    )

    @Provides
    fun providesDisplayNameFallbackProvider(
        @ApplicationContext
        context: Context
    ) = object : RoomDisplayNameFallbackProvider {
        override fun getNameForRoomInvite() =
            context.getString(R.string.room_invite)

        override fun getNameForEmptyRoom(isDirect: Boolean, leftMemberNames: List<String>) =
            context.getString(R.string.empty_room)

        override fun getNameFor1member(name: String) =
            name

        override fun getNameFor2members(
            name1: String,
            name2: String
        ) = context.getString(
            R.string.room_name_for_two_members,
            name1,
            name2
        )

        override fun getNameFor3members(
            name1: String,
            name2: String,
            name3: String
        ) = context.getString(
            R.string.room_name_for_three_members,
            name1,
            name2,
            name3
        )

        override fun getNameFor4members(
            name1: String,
            name2: String,
            name3: String,
            name4: String
        ) = context.getString(
            R.string.room_name_for_four_members,
            name1,
            name2,
            name3,
            name4
        )


        override fun getNameFor4membersAndMore(
            name1: String,
            name2: String,
            name3: String,
            remainingCount: Int
        ) = context.getString(
            R.string.room_name_for_many_members,
            name1,
            name2,
            name3,
            remainingCount
        )

        override fun excludedUserIds(roomId: String): List<String> = emptyList()
    }

}