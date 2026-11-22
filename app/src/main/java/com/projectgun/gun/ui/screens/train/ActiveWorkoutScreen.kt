package com.projectgun.gun.ui.screens.train

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectgun.gun.data.model.WorkoutSet
import com.projectgun.gun.ui.components.GunCard
import com.projectgun.gun.ui.components.GunPrimaryButton
import com.projectgun.gun.ui.components.GunSecondaryButton
import com.projectgun.gun.ui.components.RestTimerDialog
import com.projectgun.gun.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun ActiveWorkoutScreen(
    sessionId: Long,
    viewModel: TrainViewModel,
    onFinish: () -> Unit,
    onCancel: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var elapsedSeconds by remember { mutableStateOf(0L) }
    var showFinishDialog by remember { mutableStateOf(false) }

    LaunchedEffect(sessionId) {
        viewModel.loadActiveWorkout(sessionId)
    }

    // Workout duration timer ticker
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            elapsedSeconds += 1
        }
    }

    val elapsedMinutes = elapsedSeconds / 60
    val elapsedRemainderSec = elapsedSeconds % 60
    val elapsedFormatted = "%02d:%02d".format(elapsedMinutes, elapsedRemainderSec)

    // Group active sets by exercise
    val setsByExercise = remember(state.activeSets) {
        state.activeSets.groupBy { it.exerciseName }
    }

    Scaffold(
        containerColor = GunBlack,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GunDarkCharcoal)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "ACTIVE SESSION",
                        style = Typography.labelSmall.copy(color = GunRedBright, letterSpacing = 1.sp)
                    )
                    Text(
                        text = state.activeSession?.title ?: "Workout Execution",
                        style = Typography.titleMedium.copy(fontWeight = FontWeight.Black)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Timer",
                        tint = GunRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = elapsedFormatted,
                        style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = GunTextPrimary)
                    )
                }
            }
        },
        bottomBar = {
            Surface(
                color = GunDarkCharcoal,
                border = androidx.compose.foundation.BorderStroke(1.dp, GunBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GunSecondaryButton(
                        text = "Cancel",
                        onClick = onCancel,
                        modifier = Modifier.weight(1f)
                    )
                    GunPrimaryButton(
                        text = "Finish Workout",
                        onClick = { showFinishDialog = true },
                        modifier = Modifier.weight(2f)
                    )
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            setsByExercise.forEach { (exerciseName, sets) ->
                item {
                    GunCard(
                        borderColor = GunBorder,
                        backgroundColor = GunSurface
                    ) {
                        Text(
                            text = exerciseName,
                            style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = GunTextPrimary)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Table Header
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "SET", style = Typography.labelSmall.copy(fontSize = 10.sp), modifier = Modifier.width(36.dp))
                            Text(text = "KG", style = Typography.labelSmall.copy(fontSize = 10.sp), modifier = Modifier.width(60.dp))
                            Text(text = "REPS", style = Typography.labelSmall.copy(fontSize = 10.sp), modifier = Modifier.width(60.dp))
                            Text(text = "RPE", style = Typography.labelSmall.copy(fontSize = 10.sp), modifier = Modifier.width(48.dp))
                            Text(text = "DONE", style = Typography.labelSmall.copy(fontSize = 10.sp), modifier = Modifier.width(36.dp))
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        sets.forEach { setItem ->
                            ActiveSetRow(
                                setItem = setItem,
                                onToggleComplete = { viewModel.toggleSetCompleted(setItem) },
                                onUpdateValues = { w, r, rpe -> viewModel.updateSetValues(setItem.id, w, r, rpe) }
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                        }
                    }
                }
            }
        }
    }

    // Rest Timer Dialog
    if (state.isRestTimerVisible) {
        RestTimerDialog(
            initialSeconds = state.restTimerDurationSec,
            onDismiss = { viewModel.dismissRestTimer() }
        )
    }

    // Finish Workout Dialog
    if (showFinishDialog) {
        FinishWorkoutDialog(
            elapsedMinutes = elapsedMinutes.toInt().coerceAtLeast(1),
            totalVolumeKg = state.activeSets.filter { it.isCompleted }.sumOf { (it.weightKg * it.actualReps).toDouble() }.toFloat(),
            onDismiss = { showFinishDialog = false },
            onConfirm = { notes ->
                showFinishDialog = false
                viewModel.finishWorkout(notes) {
                    onFinish()
                }
            }
        )
    }
}

@Composable
private fun ActiveSetRow(
    setItem: WorkoutSet,
    onToggleComplete: () -> Unit,
    onUpdateValues: (weight: Float, reps: Int, rpe: Float) -> Unit
) {
    var weightText by remember(setItem.weightKg) { mutableStateOf(if (setItem.weightKg == 0f) "" else setItem.weightKg.toString()) }
    var repsText by remember(setItem.actualReps) { mutableStateOf(setItem.actualReps.toString()) }
    var rpeText by remember(setItem.rpe) { mutableStateOf(setItem.rpe.toString()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(if (setItem.isCompleted) GunRedMuted else GunSurfaceElevated)
            .border(1.dp, if (setItem.isCompleted) GunRed else GunBorder, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Set Order
        Text(
            text = "${setItem.setOrder}",
            style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.width(36.dp)
        )

        // Weight Input
        OutlinedTextField(
            value = weightText,
            onValueChange = {
                weightText = it
                val w = it.toFloatOrNull() ?: setItem.weightKg
                onUpdateValues(w, setItem.actualReps, setItem.rpe)
            },
            placeholder = { Text("0", fontSize = 11.sp, color = GunTextMuted) },
            singleLine = true,
            modifier = Modifier.width(60.dp).height(48.dp),
            textStyle = Typography.bodyMedium.copy(fontSize = 12.sp, color = GunTextPrimary),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GunRed,
                unfocusedBorderColor = GunBorder
            )
        )

        // Reps Input
        OutlinedTextField(
            value = repsText,
            onValueChange = {
                repsText = it
                val r = it.toIntOrNull() ?: setItem.actualReps
                onUpdateValues(setItem.weightKg, r, setItem.rpe)
            },
            singleLine = true,
            modifier = Modifier.width(60.dp).height(48.dp),
            textStyle = Typography.bodyMedium.copy(fontSize = 12.sp, color = GunTextPrimary),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GunRed,
                unfocusedBorderColor = GunBorder
            )
        )

        // RPE Input
        OutlinedTextField(
            value = rpeText,
            onValueChange = {
                rpeText = it
                val rpe = it.toFloatOrNull() ?: setItem.rpe
                onUpdateValues(setItem.weightKg, setItem.actualReps, rpe)
            },
            singleLine = true,
            modifier = Modifier.width(50.dp).height(48.dp),
            textStyle = Typography.bodyMedium.copy(fontSize = 12.sp, color = GunTextPrimary),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = GunRed,
                unfocusedBorderColor = GunBorder
            )
        )

        // Completed Checkbox Button
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(if (setItem.isCompleted) GunRed else GunDarkCharcoal)
                .border(1.dp, if (setItem.isCompleted) GunRedBright else GunBorder, RoundedCornerShape(6.dp))
                .clickable { onToggleComplete() },
            contentAlignment = Alignment.Center
        ) {
            if (setItem.isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun FinishWorkoutDialog(
    elapsedMinutes: Int,
    totalVolumeKg: Float,
    onDismiss: () -> Unit,
    onConfirm: (notes: String?) -> Unit
) {
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = GunDarkCharcoal,
        title = {
            Text(
                text = "LOG COMPLETED SESSION",
                style = Typography.titleLarge.copy(fontWeight = FontWeight.Black, color = GunTextPrimary)
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Gun Park evaluates the numbers:",
                    style = Typography.labelSmall.copy(color = GunRedBright)
                )
                Text(
                    text = "• Elapsed Duration: $elapsedMinutes minutes",
                    style = Typography.bodyMedium
                )
                Text(
                    text = "• Total Volume Moved: %.0f kg".format(totalVolumeKg),
                    style = Typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "TRAINING NOTES (OPTIONAL)", style = Typography.labelSmall)
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    placeholder = { Text("How was technical form? Any sticking points?", color = GunTextMuted, fontSize = 12.sp) },
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GunRed,
                        unfocusedBorderColor = GunBorder,
                        focusedTextColor = GunTextPrimary,
                        unfocusedTextColor = GunTextPrimary
                    )
                )
            }
        },
        confirmButton = {
            GunPrimaryButton(
                text = "Seal Record",
                onClick = { onConfirm(notes.ifBlank { null }) }
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Keep Training", color = GunTextMuted)
            }
        }
    )
}
