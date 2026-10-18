package com.harshu.spendwise.ui.auth

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.harshu.spendwise.data.AuthUiState
import com.harshu.spendwise.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    var activity: Activity? = null

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        _uiState.value = _uiState.value.copy(
            isAuthenticated = auth.currentUser != null
        )
    }

    fun onPhoneNumberChange(value: String) {
        _uiState.value = _uiState.value.copy(
            phoneNumber = value,
            errorMessage = null
        )
    }

    fun onOtpChange(value: String) {
        if (value.length <= 6 && value.all(Char::isDigit)) {
            _uiState.value = _uiState.value.copy(
                otp = value,
                errorMessage = null
            )
        }
    }

    private fun signInWithCredential(
        credential: PhoneAuthCredential
    ) {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        auth.signInWithCredential(credential)
            .addOnSuccessListener {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = true,
                    isOtpSent = false
                )
            }
            .addOnFailureListener { exception ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = exception.localizedMessage
                        ?: "Authentication failed"
                )
            }
    }

    fun sendOtp() {
        val phone = _uiState.value.phoneNumber
        val currentActivity = activity

        if (currentActivity == null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Activity not available"
            )
            return
        }

        if (!phone.matches(Regex("^[6-9]\\d{9}$"))) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Enter a valid 10-digit mobile number"
            )
            return
        }

        if (_uiState.value.isLoading) {
            return
        }

        val fullPhoneNumber = "+91$phone"

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        repository.sendOtp(
            activity = currentActivity,
            phoneNumber = fullPhoneNumber,
            onCodeSent = { verificationId ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isOtpSent = true,
                    verificationId = verificationId
                )
            },
            onVerificationCompleted = { credential ->
                signInWithCredential(credential)
            },
            onError = { message ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = message
                )
            }
        )
    }

    fun verifyOtp() {
        val otp = _uiState.value.otp
        val verificationId = _uiState.value.verificationId

        if (otp.length != 6) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Please enter a valid 6-digit OTP"
            )
            return
        }

        if (verificationId == null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Session expired. Please request OTP again."
            )
            return
        }

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null
        )

        repository.verifyOtp(
            verificationId = verificationId,
            otp = otp,
            onSuccess = {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isAuthenticated = true,
                    isOtpSent = false
                )
            },
            onError = { message ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = message
                )
            }
        )
    }

    fun resendOtp() {
        val phone = _uiState.value.phoneNumber
        val currentActivity = activity

        if (currentActivity == null) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Activity not available"
            )
            return
        }

        if (!phone.matches(Regex("^[6-9]\\d{9}$"))) {
            _uiState.value = _uiState.value.copy(
                errorMessage = "Enter a valid 10-digit mobile number"
            )
            return
        }

        if (_uiState.value.isLoading) {
            return
        }

        val fullPhoneNumber = "+91$phone"

        _uiState.value = _uiState.value.copy(
            isLoading = true,
            errorMessage = null,
            otp = ""
        )

        repository.sendOtp(
            activity = currentActivity,
            phoneNumber = fullPhoneNumber,
            onCodeSent = { verificationId ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    isOtpSent = true,
                    verificationId = verificationId
                )
            },
            onVerificationCompleted = { credential ->
                signInWithCredential(credential)
            },
            onError = { message ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = message
                )
            }
        )
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun logout() {
        viewModelScope.launch {
            auth.signOut()
            _uiState.value = AuthUiState()
        }
    }
}