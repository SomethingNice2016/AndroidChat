package ua.kucher.chat.ui.roomList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ua.kucher.chat.repository.RoomRepository
import javax.inject.Inject

@HiltViewModel
class RoomListViewModel @Inject constructor(private val roomRepository: RoomRepository) :
    ViewModel() {

    private val _rooms = MutableStateFlow<List<RoomItemUi>>(emptyList())

    val rooms: StateFlow<List<RoomItemUi>>
        get() = _rooms

    init {
        viewModelScope.launch {
            roomRepository.getRoomSummaryList().onSuccess { rooms ->
                _rooms.value = rooms.map { room ->
                    room.toUi()
                }
            }
        }
    }

}