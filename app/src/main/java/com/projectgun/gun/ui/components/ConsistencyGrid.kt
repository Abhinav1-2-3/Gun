package com.projectgun.gun.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectgun.gun.ui.theme.*
import java.time.DayOfWeek
import java.time.LocalDate

@Composable
fun ConsistencyGrid(
    completedDaysThisWeek: Set<DayOfWeek>,
    modifier: Modifier = Modifier
) {
    val days = listOf(
        DayOfWeek.MONDAY to "M",
        DayOfWeek.TUESDAY to "T",
        DayOfWeek.WEDNESDAY to "W",
        DayOfWeek.THURSDAY to "T",
        DayOfWeek.FRIDAY to "F",
        DayOfWeek.SATURDAY to "S",
        DayOfWeek.SUNDAY to "S"
    )

    val today = LocalDate.now().dayOfWeek

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEach { (dayOfWeek, label) ->
            val isCompleted = completedDaysThisWeek.contains(dayOfWeek)
            val isToday = dayOfWeek == today

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCompleted -> GunRed
                                isToday -> GunSurfaceElevated
                                else -> GunSurface
                            }
                        )
                        .border(
                            width = if (isToday) 1.5.dp else 1.dp,
                            color = if (isToday) GunRedBright else GunBorder,
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Completed",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    } else {
                        Text(
                            text = label,
                            style = Typography.labelSmall.copy(
                                color = if (isToday) GunTextPrimary else GunTextMuted,
                                fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    style = Typography.labelSmall.copy(
                        fontSize = 10.sp,
                        color = if (isToday) GunRedBright else GunTextMuted
                    )
                )
            }
        }
    }
}
