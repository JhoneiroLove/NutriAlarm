package com.upao.nutrialarm.domain.usecase.auth

import com.upao.nutrialarm.data.remote.firebase.AuthService
import javax.inject.Inject

class LogoutUseCase @Inject constructor(
    private val authService: AuthService
) {
    operator fun invoke() {
        authService.signOut()
    }
}