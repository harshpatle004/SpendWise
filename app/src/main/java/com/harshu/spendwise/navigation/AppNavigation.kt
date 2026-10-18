package com.harshu.spendwise.navigation

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.harshu.spendwise.ui.auth.LoginScreen
import com.harshu.spendwise.ui.auth.OTPScreen
import com.harshu.spendwise.ui.auth.AuthViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Otp : Screen("otp")
    object Home : Screen("home")
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    activity: Activity,
    modifier: Modifier = Modifier
) {
    val uiState by authViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        authViewModel.activity = activity
    }

    LaunchedEffect(uiState.isOtpSent) {
        if (uiState.isOtpSent) {
            navController.navigate(Screen.Otp.route)
        }
    }

    LaunchedEffect(uiState.isAuthenticated) {
        if (uiState.isAuthenticated) {
            navController.navigate(Screen.Home.route) {
                popUpTo(Screen.Login.route) { inclusive = true }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                uiState = uiState,
                onPhoneChange = { authViewModel.onPhoneNumberChange(it) },
                onSendOtp = { authViewModel.sendOtp() },
                onHelpClick = { },
                onTermsClick = { }
            )
        }

        composable(Screen.Otp.route) {
            OTPScreen(
                uiState = uiState,
                onOtpChange = { authViewModel.onOtpChange(it) },
                onVerify = { authViewModel.verifyOtp() },
                onResend = { authViewModel.resendOtp() },
                onBack = { navController.popBackStack() },
                onEditPhone = { navController.popBackStack() },
                activity = activity
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(onLogout = {
                authViewModel.logout()
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            })
        }
    }
}

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Welcome to SpendWise!",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Authentication Successful",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onLogout) {
                Text("Logout")
            }
        }
    }
}
