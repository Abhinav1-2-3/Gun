package com.projectgun.gun.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectgun.gun.ui.navigation.Screen
import com.projectgun.gun.ui.theme.*

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: ImageVector,
    val isPrimary: Boolean = false
)

@Composable
fun GunBottomBar(
    currentRoute: String,
    onNavigate: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem(Screen.Home, "HOME", Icons.Default.Dashboard),
        BottomNavItem(Screen.Train, "TRAIN", Icons.Default.FitnessCenter),
        BottomNavItem(Screen.Gun, "GUN", Icons.Default.Psychology, isPrimary = true),
        BottomNavItem(Screen.Progress, "PROGRESS", Icons.Default.TrendingUp),
        BottomNavItem(Screen.Profile, "PROFILE", Icons.Default.Person)
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(GunDarkCharcoal)
            .border(width = 1.dp, color = GunBorder)
            .padding(vertical = 8.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.screen.route

            if (item.isPrimary) {
                // Central GUN button
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(if (isSelected) GunRedBright else GunRed)
                        .clickable { onNavigate(item.screen) },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = item.label,
                            style = Typography.labelSmall.copy(
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black
                            )
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onNavigate(item.screen) }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) GunRed else GunTextMuted,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.label,
                        style = Typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) GunTextPrimary else GunTextMuted
                        )
                    )
                }
            }
        }
    }
}
