package ua.kucher.chat.ui.roomList

import org.matrix.android.sdk.api.session.room.model.RoomSummary

data class RoomItemUi(
    val roomId: String,
    val name: String = "",
    val avatarUrl: String = "",
    val notificationCount: Int = 0,
    val hasUnreadMessages: Boolean = false,
)

fun RoomSummary.toUi() = RoomItemUi(
    roomId = roomId,
    name = displayName,
    avatarUrl = avatarUrl,
    notificationCount = notificationCount,
    hasUnreadMessages = hasUnreadMessages
)
