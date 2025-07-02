package com.upao.nutrialarm.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upao.nutrialarm.domain.model.User
import com.upao.nutrialarm.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Observar el flujo del usuario actual
                userRepository.getCurrentUserFlow().collect { user ->
                    _currentUser.value = user
                }
            } catch (e: Exception) {
                // Si no hay usuario en el flujo, intentar obtenerlo directamente
                try {
                    val user = userRepository.getCurrentUser()
                    _currentUser.value = user
                } catch (e2: Exception) {
                    // Log error pero no crashear
                    _currentUser.value = null
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshUser() {
        loadCurrentUser()
    }
}