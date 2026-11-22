package com.projectgun.gun.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.projectgun.gun.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun RestTimerDialog(
    initialSeconds: Int = 90,
    onDismiss: () -> Unit
) {
    var totalSeconds by remember { mutableStateOf(initialSeconds) }
    var secondsRemaining by remember { mutableStateOf(initialSeconds) }
    var isRunning by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(isRunning, secondsRemaining) {
        if (isRunning && secondsRemaining > 0) {
            delay(1000L)
            secondsRemaining -= 1
        } else if (secondsRemaining == 0) {
            // Trigger vibration feedback
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator?.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(500)
            }
            onDismiss()
        }
    }

    val progress = if (totalSeconds > 0) secondsRemaining.toFloat() / totalSeconds.toFloat() else 0f
    val minutes = secondsRemaining / 60
    val seconds = secondsRemaining % 60
    val timeFormatted = "%02d:%02d".format(minutes, seconds)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = GunDarkCharcoal,
            border = androidx.compose.foundation.BorderStroke(1.dp, GunBorder),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "REST INTERVAL",
                    style = Typography.labelSmall.copy(letterSpacing = 2.sp, color = GunRedBright)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Recover for the next set.",
                    style = Typography.bodyMedium.copy(color = GunTextSecondary)
                )
                Spacer(modifier = Modifier.height(24.dp))

                // Circular Progress Ring
                Box(
                    modifier = Modifier.size(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawCircle(
                            color = GunBorder,
                            style = Stroke(width = 8.dp.toPx())
                        )
                        drawArc(
                            color = GunRed,
                            startAngle = -90f,
                            sweepAngle = 360f * progress,
                            useCenter = false,
                            style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = timeFormatted,
                            style = Typography.headlineLarge.copy(
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Black
                            )
                        )
                        Text(
                            text = "REMAINING",
                            style = Typography.labelSmall.copy(fontSize = 10.sp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Control buttons: -15s, +30s
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            secondsRemaining = (secondsRemaining - 15).coerceAtLeast(0)
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GunTextPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GunBorder)
                    ) {
                        Text("-15s", style = Typography.labelSmall)
                    }

                    OutlinedButton(
                        onClick = {
                            secondsRemaining += 30
                            totalSeconds += 30
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GunTextPrimary),
                        border = androidx.compose.foundation.BorderStroke(1.dp, GunBorder)
                    ) {
                        Text("+30s", style = Typography.labelSmall)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Skip / Ready button
                GunPrimaryButton(
                    text = "Skip Rest & Continue",
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
