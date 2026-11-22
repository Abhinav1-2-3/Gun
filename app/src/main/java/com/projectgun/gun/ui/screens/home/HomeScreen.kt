package com.projectgun.gun.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectgun.gun.domain.recovery.ReadinessStatus
import com.projectgun.gun.ui.components.*
import com.projectgun.gun.ui.navigation.Screen
import com.projectgun.gun.ui.theme.*

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigate: (Screen) -> Unit,
    onStartWorkout: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var showRecoveryDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadDashboard()
    }

    Scaffold(
        containerColor = GunBlack,
        topBar = {
            GunTopBar(
                title = "COMMAND CENTRE",
                streakDays = state.currentStreak
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Gun's Direct Observation Card
            GunCard(
                borderColor = GunRedMuted,
                backgroundColor = GunSurfaceElevated
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GunRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "Gun",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "GUN PARK — TRAINER",
                            style = Typography.labelSmall.copy(color = GunRedBright, letterSpacing = 1.sp)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = state.gunGreeting,
                            style = Typography.bodyLarge.copy(fontWeight = FontWeight.Medium)
                        )
                    }
                }
            }

            // Quick Metrics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard(
                    title = "Streak",
                    value = "${state.currentStreak} d",
                    subtitle = "Consecutive rhythm",
                    icon = Icons.Default.LocalFireDepartment,
                    iconTint = GunGold,
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "Sessions",
                    value = "${state.completedWorkoutsCount}",
                    subtitle = "Workouts logged",
                    icon = Icons.Default.FitnessCenter,
                    iconTint = GunRedBright,
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
            }

            // Today's Prescribed Workout Card
            GunCard(
                borderColor = GunBorderLight,
                backgroundColor = GunSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TODAY'S TRAINING DIRECTIVE",
                            style = Typography.labelSmall.copy(color = GunRedBright, letterSpacing = 1.sp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.todayWorkoutPlan?.title ?: "Full Body Routine",
                            style = Typography.headlineSmall.copy(fontWeight = FontWeight.Black)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(GunSurfaceElevated, RoundedCornerShape(6.dp))
                            .border(1.dp, GunBorder, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "~${state.todayWorkoutPlan?.estimatedDurationMin ?: 45} min",
                            style = Typography.labelSmall.copy(color = GunTextSecondary)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Exercise Preview List
                state.todayWorkoutPlan?.exercises?.take(4)?.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "• ${item.exercise.name}",
                            style = Typography.bodyMedium.copy(color = GunTextPrimary)
                        )
                        Text(
                            text = "${item.targetSets} sets × ${item.targetReps} reps",
                            style = Typography.labelSmall.copy(color = GunTextMuted)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                GunPrimaryButton(
                    text = "Start Today's Session",
                    onClick = onStartWorkout,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Weekly Consistency Calendar
            GunCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "WEEKLY CONSISTENCY",
                        style = Typography.labelSmall.copy(letterSpacing = 1.sp)
                    )
                    Text(
                        text = "${state.completedDaysThisWeek.size} / 7 days",
                        style = Typography.labelSmall.copy(color = GunTextSecondary)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                ConsistencyGrid(completedDaysThisWeek = state.completedDaysThisWeek)
            }

            // Recovery & Readiness Card
            val rec = state.recoveryEvaluation
            GunCard(
                borderColor = when (rec?.status) {
                    ReadinessStatus.OPTIMAL -> GunSuccessGreen.copy(alpha = 0.5f)
                    ReadinessStatus.OVERREACHED -> GunRed.copy(alpha = 0.8f)
                    else -> GunBorder
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "RECOVERY READINESS",
                            style = Typography.labelSmall.copy(letterSpacing = 1.sp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${rec?.readinessScore ?: 80} / 100",
                            style = Typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = when (rec?.status) {
                                    ReadinessStatus.OPTIMAL -> GunSuccessGreen
                                    ReadinessStatus.OVERREACHED -> GunRedBright
                                    else -> GunGold
                                }
                            )
                        )
                    }

                    GunSecondaryButton(
                        text = "Log Recovery",
                        onClick = { showRecoveryDialog = true },
                        modifier = Modifier.height(38.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = rec?.recommendation ?: "Keep consistent hydration and 8 hours of sleep.",
                    style = Typography.bodyMedium.copy(color = GunTextSecondary)
                )
            }

            // Adaptive Memory Milestone
            state.latestEpisode?.let { ep ->
                GunCard(
                    borderColor = GunBorder,
                    backgroundColor = GunDarkCharcoal
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.HistoryEdu,
                            contentDescription = "Memory",
                            tint = GunGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ADAPTIVE MEMORY RECORD",
                            style = Typography.labelSmall.copy(color = GunGold, letterSpacing = 1.sp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = ep.title,
                        style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = ep.description,
                        style = Typography.bodyMedium.copy(color = GunTextSecondary)
                    )
                }
            }
        }
    }

    // Quick Recovery Logging Dialog
    if (showRecoveryDialog) {
        QuickRecoveryDialog(
            onDismiss = { showRecoveryDialog = false },
            onSave = { fatigue, sleepHours, soreness, painList ->
                viewModel.logQuickRecovery(fatigue, sleepHours, soreness, painList)
                showRecoveryDialog = false
            }
        )
    }
}

@Composable
private fun QuickRecoveryDialog(
    onDismiss: () -> Unit,
    onSave: (fatigue: Int, sleepHours: Float, soreness: Int, painAreas: List<String>) -> Unit
) {
    var fatigue by remember { mutableStateOf(2) }
    var sleepHoursText by remember { mutableStateOf("7.5") }
    var soreness by remember { mutableStateOf(2) }
    var hasJointPain by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GunDarkCharcoal,
        title = {
            Text(
                text = "LOG DAILY RECOVERY",
                style = Typography.titleLarge.copy(fontWeight = FontWeight.Black, color = GunTextPrimary)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Column {
                    Text(text = "FATIGUE LEVEL (1=Fresh, 5=Exhausted): $fatigue", style = Typography.labelSmall)
                    Slider(
                        value = fatigue.toFloat(),
                        onValueChange = { fatigue = it.toInt() },
                        valueRange = 1f..5f,
                        steps = 3,
                        colors = SliderDefaults.colors(thumbColor = GunRed, activeTrackColor = GunRed)
                    )
                }

                Column {
                    Text(text = "MUSCLE SORENESS: $soreness / 5", style = Typography.labelSmall)
                    Slider(
                        value = soreness.toFloat(),
                        onValueChange = { soreness = it.toInt() },
                        valueRange = 1f..5f,
                        steps = 3,
                        colors = SliderDefaults.colors(thumbColor = GunRed, activeTrackColor = GunRed)
                    )
                }

                Column {
                    Text(text = "SLEEP HOURS", style = Typography.labelSmall)
                    Spacer(modifier = Modifier.height(4.dp))
                    OutlinedTextField(
                        value = sleepHoursText,
                        onValueChange = { sleepHoursText = it },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GunRed,
                            unfocusedBorderColor = GunBorder,
                            focusedTextColor = GunTextPrimary,
                            unfocusedTextColor = GunTextPrimary
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { hasJointPain = !hasJointPain }
                ) {
                    Checkbox(
                        checked = hasJointPain,
                        onCheckedChange = { hasJointPain = it },
                        colors = CheckboxDefaults.colors(checkedColor = GunRed)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "I feel sharp pain or injury symptoms",
                        style = Typography.bodyMedium.copy(color = if (hasJointPain) GunRedBright else GunTextPrimary)
                    )
                }
            }
        },
        confirmButton = {
            GunPrimaryButton(
                text = "Submit Record",
                onClick = {
                    val sleep = sleepHoursText.toFloatOrNull() ?: 7.5f
                    val painList = if (hasJointPain) listOf("Joint / Muscular Pain reported") else emptyList()
                    onSave(fatigue, sleep, soreness, painList)
                }
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel", color = GunTextMuted)
            }
        }
    )
}
