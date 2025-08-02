package ua.kucher.chat.repository

import org.matrix.android.sdk.api.session.room.Room
import org.matrix.android.sdk.api.session.room.model.Membership
import org.matrix.android.sdk.api.session.room.model.RoomSummary
import org.matrix.android.sdk.api.session.room.roomSummaryQueryParams
import ua.kucher.chat.core.mapAsync

interface RoomRepository {

    suspend fun getRoomSummaryList(): Result<List<RoomSummary>>

    suspend fun getRoomById(roomId: String): Result<Room>

    class Impl(private val sessionRepository: SessionRepository) : RoomRepository {

        override suspend fun getRoomSummaryList() =
            sessionRepository.createSessionFromSso().mapAsync { session ->
                session.roomService().getRoomSummaries(
                    queryParams = roomSummaryQueryParams {
                        memberships = Membership.activeMemberships()
                    }
                )
            }

        override suspend fun getRoomById(roomId: String) =
            sessionRepository.createSessionFromSso().mapAsync { session ->
                session.roomService().getRoom(roomId)!!
            }
    }
}