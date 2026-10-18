package com.harshu.spendwise.ui.auth

import android.app.Activity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harshu.spendwise.data.AuthUiState
import kotlinx.coroutines.delay

private val Accent = Color(0xFF1B6B50)
private val Ground = Color(0xFFF6F7F4)
private val Ink = Color(0xFF11201B)
private val Muted = Color(0xFF4A5A53)
private val Outline = Color(0xFFD5DAD3)

private const val OTP_LENGTH = 6
private const val RESEND_SECONDS = 60

@Composable
fun OTPScreen(
    uiState: AuthUiState,
    onOtpChange: (String) -> Unit,
    onVerify: () -> Unit,
    onResend: () -> Unit,
    onBack: () -> Unit,
    onEditPhone: () -> Unit,
    activity: Activity? = null
) {
    var secondsLeft by rememberSaveable { mutableIntStateOf(RESEND_SECONDS) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(secondsLeft == RESEND_SECONDS) {
        while (secondsLeft > 0) {
            delay(1_000)
            secondsLeft--
        }
    }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ground)
            .systemBarsPadding()
            .imePadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp),
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier
                .size(44.dp)
                .background(Color.White, CircleShape)
                .border(1.dp, Outline, CircleShape),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Go back",
                tint = Ink,
                modifier = Modifier.size(20.dp),
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                text = "Verify your number",
                color = Ink,
                fontSize = 32.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Enter the 6-digit code we just sent to your phone",
                color = Muted,
                fontSize = 16.sp,
                lineHeight = 24.sp,
            )
        }

        uiState.errorMessage?.let { error ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEBEE)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = error,
                    color = Color(0xFFD32F2F),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        OtpField(
            value = uiState.otp,
            onValueChange = { new ->
                if (new.length <= OTP_LENGTH && new.all(Char::isDigit)) onOtpChange(new)
            },
            modifier = Modifier.focusRequester(focusRequester),
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            if (secondsLeft > 0) {
                Text(
                    text = "Resend code in 0:%02d".format(secondsLeft),
                    color = Muted,
                    fontSize = 15.sp,
                )
            } else {
                Text(
                    text = "Resend code",
                    color = Accent,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable {
                        onOtpChange("")
                        secondsLeft = RESEND_SECONDS
                        onResend()
                    },
                )
            }
            Text(
                text = "Edit phone number",
                color = Accent,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable(onClick = onEditPhone),
            )
        }

        Spacer(Modifier.weight(1f))

        Button(
            onClick = onVerify,
            enabled = uiState.otp.length == OTP_LENGTH && !uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Accent,
                contentColor = Color.White,
                disabledContainerColor = Accent.copy(alpha = 0.35f),
                disabledContentColor = Color.White,
            ),
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            } else {
                Text("Verify", fontSize = 17.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}


@Composable
private fun OtpField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        singleLine = true,
        cursorBrush = SolidColor(Color.Transparent),
        textStyle = TextStyle(color = Color.Transparent),
        decorationBox = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                repeat(OTP_LENGTH) { index ->
                    val isActive = index == value.length
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(60.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(
                                border = BorderStroke(
                                    width = if (isActive) 2.dp else 1.5.dp,
                                    color = if (isActive) Accent else Outline,
                                ),
                                shape = RoundedCornerShape(12.dp),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = value.getOrNull(index)?.toString() ?: "",
                            color = Ink,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        },
    )
}
