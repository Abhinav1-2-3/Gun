package com.projectgun.gun.ui.screens.train

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.projectgun.gun.data.model.Exercise
import com.projectgun.gun.data.model.ExerciseCategory
import com.projectgun.gun.ui.components.GunCard
import com.projectgun.gun.ui.components.GunPrimaryButton
import com.projectgun.gun.ui.components.GunSecondaryButton
import com.projectgun.gun.ui.components.GunTopBar
import com.projectgun.gun.ui.theme.*

@Composable
fun TrainScreen(
    viewModel: TrainViewModel,
    onStartWorkout: (Long) -> Unit,
    onResumeWorkout: (Long) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) } // 0 = Routine, 1 = Exercise Library, 2 = History
    var selectedExerciseForModal by remember { mutableStateOf<Exercise?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Scaffold(
        containerColor = GunBlack,
        topBar = {
            GunTopBar(title = "TRAINING CENTRE")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Active Workout Banner (if user navigated away while workout is live)
            if (state.activeSession != null) {
                GunCard(
                    borderColor = GunRedBright,
                    backgroundColor = GunRedMuted,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "WORKOUT IN PROGRESS",
                                style = Typography.labelSmall.copy(color = GunRedBright, letterSpacing = 1.sp)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = state.activeSession?.title ?: "Current Workout",
                                style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        GunPrimaryButton(
                            text = "Resume",
                            onClick = { state.activeSession?.id?.let(onResumeWorkout) },
                            modifier = Modifier.height(38.dp)
                        )
                    }
                }
            }

            // Tab Selector
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GunDarkCharcoal, RoundedCornerShape(8.dp))
                    .padding(4.dp)
            ) {
                listOf("Today's Plan", "Exercises", "History").forEachIndexed { index, label ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isSelected) GunSurfaceElevated else Color.Transparent)
                            .clickable { selectedTab = index }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label.uppercase(),
                            style = Typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) GunRedBright else GunTextMuted
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> TabTodayRoutine(
                    state = state,
                    onStart = {
                        viewModel.startWorkout { sessionId ->
                            onStartWorkout(sessionId)
                        }
                    },
                    onExerciseClick = { selectedExerciseForModal = it }
                )
                1 -> TabExerciseCatalog(
                    exercises = state.allExercises,
                    onExerciseClick = { selectedExerciseForModal = it }
                )
                2 -> TabWorkoutHistory(state = state)
            }
        }
    }

    // Exercise Detail Dialog
    selectedExerciseForModal?.let { exercise ->
        ExerciseDetailDialog(
            exercise = exercise,
            onDismiss = { selectedExerciseForModal = null }
        )
    }
}

@Composable
private fun TabTodayRoutine(
    state: TrainUiState,
    onStart: () -> Unit,
    onExerciseClick: (Exercise) -> Unit
) {
    val plan = state.todayPlan

    if (plan == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GunRed)
        }
        return
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            GunCard(borderColor = GunBorderLight) {
                Text(
                    text = plan.splitType,
                    style = Typography.labelSmall.copy(color = GunRedBright, letterSpacing = 1.sp)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = plan.title,
                    style = Typography.headlineMedium.copy(fontWeight = FontWeight.Black)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total Exercises: ${plan.exercises.size}  •  Estimated Duration: ${plan.estimatedDurationMin} min",
                    style = Typography.bodyMedium.copy(color = GunTextSecondary)
                )
                Spacer(modifier = Modifier.height(16.dp))
                GunPrimaryButton(
                    text = "Begin Workout Execution",
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        item {
            Text(
                text = "PLANNED MOVEMENTS",
                style = Typography.labelSmall.copy(letterSpacing = 1.sp, color = GunTextMuted),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(plan.exercises) { item ->
            GunCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onExerciseClick(item.exercise) }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.exercise.name,
                            style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "${item.exercise.primaryMuscle}  •  ${item.exercise.equipmentRequired}",
                            style = Typography.bodyMedium.copy(fontSize = 12.sp, color = GunTextSecondary)
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${item.targetSets} sets",
                            style = Typography.titleMedium.copy(color = GunRedBright, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${item.targetReps} reps",
                            style = Typography.labelSmall.copy(color = GunTextMuted)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TabExerciseCatalog(
    exercises: List<Exercise>,
    onExerciseClick: (Exercise) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<ExerciseCategory?>(null) }

    val filtered = if (selectedCategory == null) exercises else exercises.filter { it.category == selectedCategory }

    Column {
        // Category Filter Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val categories = listOf(null, ExerciseCategory.CHEST, ExerciseCategory.BACK, ExerciseCategory.LEGS, ExerciseCategory.SHOULDERS, ExerciseCategory.ARMS, ExerciseCategory.CORE)
            categories.forEach { cat ->
                val isSelected = selectedCategory == cat
                val label = cat?.name ?: "ALL"
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) GunRed else GunSurface)
                        .border(1.dp, if (isSelected) GunRedBright else GunBorder, RoundedCornerShape(16.dp))
                        .clickable { selectedCategory = cat }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = label,
                        style = Typography.labelSmall.copy(
                            color = if (isSelected) Color.White else GunTextMuted,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    )
                }
            }
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { exercise ->
                GunCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onExerciseClick(exercise) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = exercise.name, style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${exercise.primaryMuscle}  •  ${exercise.equipmentRequired}",
                                style = Typography.bodyMedium.copy(fontSize = 11.sp, color = GunTextMuted)
                            )
                        }
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = GunTextMuted)
                    }
                }
            }
        }
    }
}

@Composable
private fun TabWorkoutHistory(state: TrainUiState) {
    if (state.pastWorkouts.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "No completed workouts on record yet. Finish a session to see its logs.",
                style = Typography.bodyMedium.copy(color = GunTextMuted)
            )
        }
        return
    }

    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(state.pastWorkouts) { item ->
            GunCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = item.session.title, style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Duration: ${item.session.durationMinutes} min  •  Volume: %.0f kg".format(item.session.totalVolumeKg),
                            style = Typography.bodyMedium.copy(fontSize = 12.sp, color = GunTextSecondary)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(GunRedMuted, RoundedCornerShape(4.dp))
                            .border(1.dp, GunRed, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "DONE",
                            style = Typography.labelSmall.copy(color = GunRedBright, fontWeight = FontWeight.Bold)
                        )
                    }
                }

                if (item.sets.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Completed Sets (${item.sets.size}):",
                        style = Typography.labelSmall.copy(color = GunTextMuted)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    item.sets.take(5).forEach { set ->
                        Text(
                            text = "• ${set.exerciseName}: ${set.weightKg} kg × ${set.actualReps} reps (RPE ${set.rpe})",
                            style = Typography.bodyMedium.copy(fontSize = 11.sp, color = GunTextPrimary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseDetailDialog(exercise: Exercise, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GunDarkCharcoal,
        title = {
            Text(
                text = exercise.name.uppercase(),
                style = Typography.titleLarge.copy(fontWeight = FontWeight.Black, color = GunTextPrimary)
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "PRIMARY TARGET: ${exercise.primaryMuscle.uppercase()}",
                    style = Typography.labelSmall.copy(color = GunRedBright)
                )

                Text(
                    text = "INSTRUCTIONS",
                    style = Typography.labelSmall.copy(letterSpacing = 1.sp)
                )
                exercise.instructions.forEachIndexed { i, step ->
                    Text(
                        text = "${i + 1}. $step",
                        style = Typography.bodyMedium.copy(color = GunTextPrimary)
                    )
                }

                if (exercise.safetyTips.isNotEmpty()) {
                    Text(
                        text = "SAFETY CONSIDERATIONS",
                        style = Typography.labelSmall.copy(color = GunGold, letterSpacing = 1.sp)
                    )
                    exercise.safetyTips.forEach { tip ->
                        Text(
                            text = "⚠ $tip",
                            style = Typography.bodyMedium.copy(color = GunTextSecondary)
                        )
                    }
                }
            }
        },
        confirmButton = {
            GunPrimaryButton(text = "Close", onClick = onDismiss)
        }
    )
}
