package com.harshu.spendwise.data

data class AuthUiState(
    val phoneNumber: String = "",
    val otp: String = "",
    val verificationId: String? = null,
    val isLoading: Boolean = false,
    val isOtpSent: Boolean = false,
    val isAuthenticated: Boolean = false,
    val errorMessage: String? = null
)