package com.projectgun.gun.ui.screens.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectgun.gun.domain.training.ProgressiveOverloadEngine
import com.projectgun.gun.ui.components.GunCard
import com.projectgun.gun.ui.components.GunTopBar
import com.projectgun.gun.ui.components.MetricCard
import com.projectgun.gun.ui.components.VolumeChart
import com.projectgun.gun.ui.theme.*

@Composable
fun ProgressScreen(
    viewModel: ProgressViewModel
) {
    val state by viewModel.uiState.collectAsState()

    var calcWeight by remember { mutableStateOf("100") }
    var calcReps by remember { mutableStateOf("5") }

    val calc1RM = remember(calcWeight, calcReps) {
        val w = calcWeight.toFloatOrNull() ?: 0f
        val r = calcReps.toIntOrNull() ?: 0
        ProgressiveOverloadEngine.calculateEstimated1RM(w, r)
    }

    LaunchedEffect(Unit) {
        viewModel.loadAnalytics()
    }

    Scaffold(
        containerColor = GunBlack,
        topBar = {
            GunTopBar(title = "PERFORMANCE CENTRE")
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Metrics Summary Row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MetricCard(
                        title = "Sessions",
                        value = "${state.totalWorkouts}",
                        subtitle = "Total logged",
                        icon = Icons.Default.FitnessCenter,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "Volume",
                        value = "%.0f".format(state.totalVolumeKg),
                        subtitle = "Total kg moved",
                        icon = Icons.Default.TrendingUp,
                        iconTint = GunSuccessGreen,
                        modifier = Modifier.weight(1f)
                    )
                    MetricCard(
                        title = "PRs",
                        value = "${state.personalRecords.size}",
                        subtitle = "Trophies",
                        icon = Icons.Default.EmojiEvents,
                        iconTint = GunGold,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Gun's Evaluation Card
            item {
                GunCard(borderColor = GunBorderLight, backgroundColor = GunSurfaceElevated) {
                    Text(
                        text = "GUN'S MERITOCRATIC EVALUATION",
                        style = Typography.labelSmall.copy(color = GunRedBright, letterSpacing = 1.sp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = state.gunProgressObservation,
                        style = Typography.bodyLarge.copy(color = GunTextPrimary, lineHeight = 22.sp)
                    )
                }
            }

            // Volume Progression Chart
            item {
                GunCard {
                    Text(
                        text = "TRAINING VOLUME TREND (RECENT SESSIONS)",
                        style = Typography.labelSmall.copy(letterSpacing = 1.sp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    VolumeChart(data = state.volumeChartData)
                }
            }

            // Interactive 1RM Calculator Card
            item {
                GunCard {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = "Calculator",
                            tint = GunRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "EPLEY 1-REP MAX (1RM) CALCULATOR",
                            style = Typography.labelSmall.copy(letterSpacing = 1.sp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = calcWeight,
                            onValueChange = { calcWeight = it },
                            label = { Text("Weight (kg)", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GunRed,
                                unfocusedBorderColor = GunBorder
                            )
                        )

                        OutlinedTextField(
                            value = calcReps,
                            onValueChange = { calcReps = it },
                            label = { Text("Reps", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GunRed,
                                unfocusedBorderColor = GunBorder
                            )
                        )

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GunSurfaceElevated)
                                .border(1.dp, GunBorder, RoundedCornerShape(8.dp))
                                .padding(vertical = 10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "EST. 1RM", style = Typography.labelSmall.copy(fontSize = 9.sp))
                            Text(
                                text = "%.1f kg".format(calc1RM),
                                style = Typography.titleMedium.copy(color = GunGold, fontWeight = FontWeight.Black)
                            )
                        }
                    }
                }
            }

            // Verified PRs Section Header
            item {
                Text(
                    text = "PERSONAL RECORDS TROPHY WALL",
                    style = Typography.labelSmall.copy(letterSpacing = 1.sp, color = GunTextMuted),
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // PR Items
            if (state.personalRecords.isEmpty()) {
                item {
                    GunCard {
                        Text(
                            text = "No PRs logged yet. During your workouts, completing a heavy set will automatically lock in your record and record a milestone in Gun's memory.",
                            style = Typography.bodyMedium.copy(color = GunTextMuted)
                        )
                    }
                }
            } else {
                items(state.personalRecords) { pr ->
                    GunCard(borderColor = GunBorderLight) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(CircleShape)
                                        .background(GunSurfaceElevated)
                                        .border(1.dp, GunGold, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.EmojiEvents,
                                        contentDescription = "PR",
                                        tint = GunGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = pr.exerciseName,
                                        style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = "Top set: ${pr.weightKg} kg × ${pr.reps} reps",
                                        style = Typography.bodyMedium.copy(fontSize = 11.sp, color = GunTextSecondary)
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "%.1f kg".format(pr.estimated1RM),
                                    style = Typography.titleLarge.copy(color = GunGold, fontWeight = FontWeight.Black)
                                )
                                Text(
                                    text = "EST. 1RM",
                                    style = Typography.labelSmall.copy(fontSize = 9.sp, color = GunTextMuted)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
