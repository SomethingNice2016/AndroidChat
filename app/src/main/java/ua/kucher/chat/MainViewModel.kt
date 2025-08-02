package ua.kucher.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ua.kucher.chat.repository.SessionRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            sessionRepository.createSessionFromSso()
        }
    }
}