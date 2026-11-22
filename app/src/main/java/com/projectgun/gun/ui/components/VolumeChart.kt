package com.projectgun.gun.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectgun.gun.ui.theme.*

data class VolumeBarData(
    val label: String,
    val volumeKg: Float
)

@Composable
fun VolumeChart(
    data: List<VolumeBarData>,
    modifier: Modifier = Modifier
) {
    if (data.isEmpty()) {
        Box(
            modifier = modifier.fillMaxWidth().height(150.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No recorded volume yet. Complete workouts to see your progression.",
                style = Typography.bodyMedium.copy(color = GunTextMuted)
            )
        }
        return
    }

    val maxVolume = (data.maxOfOrNull { it.volumeKg } ?: 1000f).coerceAtLeast(100f)

    Column(modifier = modifier.fillMaxWidth()) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            val chartWidth = size.width
            val chartHeight = size.height - 24.dp.toPx()
            val barCount = data.size
            val barSpacing = chartWidth / (barCount + 1)
            val barWidth = (barSpacing * 0.6f).coerceIn(16.dp.toPx(), 40.dp.toPx())

            // Draw horizontal guideline
            drawLine(
                color = GunBorder,
                start = Offset(0f, chartHeight / 2),
                end = Offset(chartWidth, chartHeight / 2),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )

            // Draw baseline
            drawLine(
                color = GunBorderLight,
                start = Offset(0f, chartHeight),
                end = Offset(chartWidth, chartHeight),
                strokeWidth = 1.5.dp.toPx()
            )

            data.forEachIndexed { index, item ->
                val x = barSpacing * (index + 1) - (barWidth / 2)
                val barFraction = (item.volumeKg / maxVolume).coerceIn(0.05f, 1.0f)
                val barHeight = chartHeight * barFraction
                val y = chartHeight - barHeight

                // Draw Bar
                drawRoundRect(
                    color = GunRed,
                    topLeft = Offset(x, y),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
            }
        }

        // Labels below
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            data.forEach { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = item.label,
                        style = Typography.labelSmall.copy(fontSize = 10.sp, color = GunTextSecondary)
                    )
                    Text(
                        text = "%.0f kg".format(item.volumeKg),
                        style = Typography.labelSmall.copy(
                            fontSize = 9.sp,
                            color = GunTextMuted,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}
