package com.harshu.spendwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.harshu.spendwise.navigation.AppNavigation
import com.harshu.spendwise.repository.AuthRepository
import com.harshu.spendwise.ui.auth.AuthViewModel
import com.harshu.spendwise.ui.theme.SpendWiseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpendWiseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val auth = FirebaseAuth.getInstance()
                    val repository = AuthRepository(auth)
                    val authViewModel: AuthViewModel = viewModel {
                        AuthViewModel(repository, auth)
                    }
                    val navController = rememberNavController()

                    AppNavigation(
                        navController = navController,
                        authViewModel = authViewModel,
                        activity = this
                    )
                }
            }
        }
    }
}
