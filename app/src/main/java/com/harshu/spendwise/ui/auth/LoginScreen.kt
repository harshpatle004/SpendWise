
package com.harshu.spendwise.ui.auth

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import com.harshu.spendwise.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.harshu.spendwise.data.AuthUiState

/* ----------------------------- Colors ----------------------------- */

private val Green = Color(0xFF0B8A4F)
private val GreenDark = Color(0xFF066B3C)
private val GreenSoft = Color(0xFFE4F4EB)
private val GreenLeaf = Color(0xFF3DAE6B)
private val Ink = Color(0xFF0E1A14)
private val Muted = Color(0xFF6B7570)
private val Hint = Color(0xFFA3ABA7)
private val Red = Color(0xFFE5533D)


@Composable
fun LoginScreen(
    uiState: AuthUiState,
    onPhoneChange: (String) -> Unit,
    onSendOtp: () -> Unit,
    onHelpClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
) {
    val phone = uiState.phoneNumber

    Box(
        Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(Color(0xFFEFF9F3), Color.White, Color.White))
            )
    ) {
        // Big decorative circle top-right
        Box(
            Modifier
                .size(230.dp)
                .align(Alignment.TopEnd)
                .offset(x = 110.dp, y = 90.dp)
                .background(
                    Brush.linearGradient(listOf(Green, GreenDark)),
                    CircleShape
                )
        )

        Column(
            Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            // "Need help?"
            Row(
                Modifier
                    .align(Alignment.End)
                    .padding(top = 12.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(onClick = onHelpClick)
                    .padding(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.SupportAgent, null, tint = Green, modifier = Modifier.size(22.dp))
                Spacer(Modifier.width(8.dp))
                Text("Need help?", color = Green, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            }

            Hero()

            Spacer(Modifier.height(8.dp))


            LoginCard(
                phone = phone,
                onPhoneChange = onPhoneChange,
                onSendOtp = onSendOtp,
                isLoading = uiState.isLoading,
                errorMessage = uiState.errorMessage
            )


            Spacer(Modifier.height(20.dp))
            FeatureRow()
            Spacer(Modifier.height(20.dp))
            TermsText(onTermsClick)
            Spacer(Modifier.height(16.dp))
        }
    }
}

/* ------------------------------ Hero ------------------------------ */

@Composable
private fun Hero() {
    Box(
        Modifier
            .fillMaxWidth()
            .height(430.dp)
    ) {
        Column(Modifier.align(Alignment.TopStart)) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "SpendWise logo",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(width = 72.dp, height = 66.dp)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Ink)) { append("Spend") }
                    withStyle(SpanStyle(color = Green)) { append("Wise") }
                },
                fontSize = 38.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp
            )
            Text(
                "YOUR MONEY, UNDER CONTROL",
                color = Muted,
                fontSize = 9.sp,
                letterSpacing = 2.5.sp,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(Modifier.height(36.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = Ink)) { append("Track.\nPlan.\n") }
                    withStyle(SpanStyle(color = Green)) { append("Grow.") }
                },
                style = TextStyle(
                    fontSize = 46.sp,
                    lineHeight = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-1).sp
                )
            )
            Spacer(Modifier.height(12.dp))
            Text(
                "A smarter way to\nmanage your money\nevery day.",
                color = Muted,
                fontSize = 17.sp,
                lineHeight = 22.sp
            )
        }

        HeroIllustration(
            Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 28.dp)
        )
    }
}

@Composable
private fun HeroIllustration(modifier: Modifier = Modifier) {
    Box(modifier.size(width = 230.dp, height = 300.dp)) {

        // Leaves behind the phone
        Canvas(Modifier.matchParentSize()) {
            leaf(Offset(size.width * 0.28f, size.height * 0.80f), Offset(size.width * 0.30f, size.height * 0.28f), 40f, Brush.verticalGradient(listOf(Color(0xFFB7E3C4), GreenLeaf)))
            leaf(Offset(size.width * 0.30f, size.height * 0.85f), Offset(size.width * 0.00f, size.height * 0.55f), 34f, SolidColor(GreenDark))
        }

        // Tilted phone mock
        Box(
            Modifier
                .align(Alignment.TopEnd)
                .padding(top = 20.dp)
                .size(width = 160.dp, height = 210.dp)
                .graphicsLayer { rotationZ = -8f }
                .shadow(16.dp, RoundedCornerShape(26.dp), ambientColor = Green, spotColor = Green)
                .background(Color.White.copy(alpha = 0.92f), RoundedCornerShape(26.dp))
                .border(BorderStroke(1.dp, Color(0xFFDDEFE4)), RoundedCornerShape(26.dp))
                .padding(14.dp)
        ) {
            Column {
                Text("Total Balance", fontSize = 9.sp, color = Muted)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("₹24,850", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Ink)
                    Spacer(Modifier.weight(1f))
                    Text(
                        "↑ 12%",
                        fontSize = 8.sp,
                        color = Green,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .background(GreenSoft, RoundedCornerShape(50))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(Modifier.height(10.dp))
                MiniBarChart(Modifier.fillMaxWidth().height(70.dp))
            }
        }

        // Income card
        MiniStatCard(
            label = "Income",
            amount = "₹32,000",
            icon = Icons.Rounded.NorthEast,
            iconBg = Green,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(x = 4.dp, y = (-40).dp)
                .graphicsLayer { rotationZ = -8f }
        )

        // Expenses card
        MiniStatCard(
            label = "Expenses",
            amount = "₹7,150",
            icon = Icons.Rounded.SouthEast,
            iconBg = Red,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = (-6).dp, y = (-4).dp)
                .graphicsLayer { rotationZ = -8f }
        )

        // Floating add button
        Box(
            Modifier
                .align(Alignment.CenterEnd)
                .offset(x = 8.dp, y = 28.dp)
                .size(36.dp)
                .background(Green, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Rounded.Add, null, tint = Color.White)
        }
    }
}

@Composable
private fun MiniBarChart(modifier: Modifier = Modifier) {
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May")
    val heights = listOf(0.30f, 0.45f, 0.90f, 0.55f, 0.85f)
    val colors = listOf(
        Green.copy(alpha = 0.15f), Green.copy(alpha = 0.25f),
        Green, Green.copy(alpha = 0.9f), Green
    )
    Row(modifier, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
        months.forEachIndexed { i, m ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    Modifier
                        .width(12.dp)
                        .height(52.dp * heights[i])
                        .background(colors[i], RoundedCornerShape(4.dp))
                )
                Text(m, fontSize = 7.sp, color = Hint, modifier = Modifier.padding(top = 4.dp))
            }
        }
    }
}

@Composable
private fun MiniStatCard(
    label: String,
    amount: String,
    icon: ImageVector,
    iconBg: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier
            .shadow(10.dp, RoundedCornerShape(14.dp), ambientColor = Green.copy(alpha = .4f))
            .background(Color.White, RoundedCornerShape(14.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(26.dp).background(iconBg, CircleShape),
            contentAlignment = Alignment.Center
        ) { Icon(icon, null, tint = Color.White, modifier = Modifier.size(14.dp)) }
        Spacer(Modifier.width(8.dp))
        Column {
            Text(label, fontSize = 8.sp, color = Muted)
            Text(amount, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Ink)
        }
    }
}



private fun DrawScope.leaf(base: Offset, tip: Offset, bulge: Float, brush: Brush) {
    val dir = tip - base
    val len = dir.getDistance().coerceAtLeast(1f)
    val normal = Offset(-dir.y / len, dir.x / len) * bulge
    val mid = (base + tip) / 2f
    val path = Path().apply {
        moveTo(base.x, base.y)
        quadraticBezierTo(mid.x + normal.x, mid.y + normal.y, tip.x, tip.y)
        quadraticBezierTo(mid.x - normal.x, mid.y - normal.y, base.x, base.y)
        close()
    }
    drawPath(path, brush)
}

/* --------------------------- Login card --------------------------- */

@Composable
private fun LoginCard(
    phone: String,
    onPhoneChange: (String) -> Unit,
    onSendOtp: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?
) {
    Column(
        Modifier
            .fillMaxWidth()
            .shadow(20.dp, RoundedCornerShape(28.dp), ambientColor = Green.copy(.25f), spotColor = Green.copy(.25f))
            .background(Color.White, RoundedCornerShape(28.dp))
            .padding(20.dp)
    ) {
        Text("Enter your mobile number", color = Muted, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(14.dp))

        // Phone field
        Row(
            Modifier
                .fillMaxWidth()
                .height(62.dp)
                .border(1.dp, Color(0xFFE9EEEB), RoundedCornerShape(18.dp))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IndiaFlag()
            Spacer(Modifier.width(10.dp))
            Text("+91", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Ink)
            Icon(Icons.Rounded.KeyboardArrowDown, null, tint = Muted, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(10.dp))
            Box(Modifier.width(1.dp).height(28.dp).background(Color(0xFFE3E8E5)))
            Spacer(Modifier.width(16.dp))

            BasicTextField(
                value = phone,
                onValueChange = { value ->
                    val digits = value.filter { it.isDigit() }.take(10)
                    onPhoneChange(digits)
                },
                singleLine = true,
                textStyle = TextStyle(fontSize = 20.sp, color = Ink, fontWeight = FontWeight.Medium, letterSpacing = 1.sp),
                keyboardOptions = KeyboardOptions(keyboardType = androidx.compose.ui.text.input.KeyboardType.Phone),
                cursorBrush = SolidColor(Green),
                modifier = Modifier.weight(1f),
                decorationBox = { inner ->
                    if (phone.isEmpty()) {
                        Text("98765 43210", fontSize = 20.sp, color = Hint, letterSpacing = 1.sp)
                    }
                    inner()
                }
            )
        }

        Spacer(Modifier.height(18.dp))

        // Send OTP button
        val enabled = phone.matches(Regex("[6-9]\\d{9}")) &&
                !isLoading
        Button(
            onClick = onSendOtp,
            enabled = enabled,
            shape = RoundedCornerShape(18.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .shadow(12.dp, RoundedCornerShape(18.dp), ambientColor = Green, spotColor = Green)
        ) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            if (enabled) listOf(Color(0xFF14A25F), GreenDark)
                            else listOf(Color(0xFF7CC8A0), Color(0xFF5BAE85))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Send OTP", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(10.dp))
                    Icon(Icons.Rounded.ArrowForward, null, tint = Color.White)
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        Row(
            Modifier.align(Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Rounded.Lock, null, tint = Muted, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                "Your number is secure with us. We never share it with anyone.",
                color = Muted,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.widthIn(max = 260.dp)
            )
        }
    }
}

@Composable
private fun IndiaFlag() {
    Canvas(
        Modifier
            .size(width = 34.dp, height = 24.dp)
            .clip(RoundedCornerShape(3.dp))
    ) {
        val band = size.height / 3f
        drawRect(Color(0xFFFF9933), size = Size(size.width, band))
        drawRect(Color.White, topLeft = Offset(0f, band), size = Size(size.width, band))
        drawRect(Color(0xFF138808), topLeft = Offset(0f, band * 2), size = Size(size.width, band))
        drawCircle(
            Color(0xFF000080),
            radius = band * 0.4f,
            center = Offset(size.width / 2, size.height / 2),
            style = Stroke(width = 2f)
        )
    }
}

/* ------------------------ Features & terms ------------------------ */

@Composable
private fun FeatureRow() {
    Row(
        Modifier.fillMaxWidth().height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Feature(Icons.Rounded.VerifiedUser, "Secure", "Your data is safe", Modifier.weight(1f))
        VerticalDivider(Modifier.height(48.dp), color = Color(0xFFE3E8E5))
        Feature(Icons.Rounded.Bolt, "Fast", "Quick verification", Modifier.weight(1f))
        VerticalDivider(Modifier.height(48.dp), color = Color(0xFFE3E8E5))
        Feature(Icons.Rounded.BarChart, "Smarter", "Better control", Modifier.weight(1f))
    }
}

@Composable
private fun Feature(icon: ImageVector, title: String, subtitle: String, modifier: Modifier = Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier.size(52.dp).background(GreenSoft, CircleShape),
            contentAlignment = Alignment.Center
        ) { Icon(icon, null, tint = Green, modifier = Modifier.size(26.dp)) }
        Spacer(Modifier.height(8.dp))
        Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Ink)
        Text(subtitle, fontSize = 12.sp, color = Muted, textAlign = TextAlign.Center)
    }
}

@Composable
private fun TermsText(onClick: () -> Unit) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("By continuing, you agree to our ", color = Muted, fontSize = 13.sp)
        Text(
            "Terms & Privacy Policy",
            color = Green,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}

//,,

