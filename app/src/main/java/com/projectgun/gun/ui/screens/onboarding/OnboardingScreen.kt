package com.projectgun.gun.ui.screens.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectgun.gun.data.model.FitnessLevel
import com.projectgun.gun.data.model.GoalType
import com.projectgun.gun.data.model.TrainingLocation
import com.projectgun.gun.ui.components.GunCard
import com.projectgun.gun.ui.components.GunPrimaryButton
import com.projectgun.gun.ui.components.GunSecondaryButton
import com.projectgun.gun.ui.theme.*

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel,
    onComplete: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isComplete) {
        onComplete()
    }

    Scaffold(
        containerColor = GunBlack
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "GUN INITIAL ASSESSMENT",
                        style = Typography.labelSmall.copy(letterSpacing = 2.sp, color = GunRedBright)
                    )
                    Text(
                        text = "STEP ${state.currentStep + 1} OF ${state.totalSteps}",
                        style = Typography.labelSmall.copy(color = GunTextMuted)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Progress Indicator
                LinearProgressIndicator(
                    progress = { (state.currentStep + 1).toFloat() / state.totalSteps.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = GunRed,
                    trackColor = GunBorder
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Step content
                when (state.currentStep) {
                    0 -> StepWelcomeAndBasics(state, viewModel)
                    1 -> StepExperienceAndLevel(state, viewModel)
                    2 -> StepPrimaryGoal(state, viewModel)
                    3 -> StepEquipmentAndLocation(state, viewModel)
                    4 -> StepScheduleAndSafety(state, viewModel)
                }
            }

            // Bottom Navigation Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (state.currentStep > 0) {
                    GunSecondaryButton(
                        text = "Back",
                        onClick = { viewModel.prevStep() },
                        modifier = Modifier.weight(1f)
                    )
                }
                GunPrimaryButton(
                    text = if (state.currentStep == state.totalSteps - 1) "Lock In Baseline" else "Next",
                    onClick = { viewModel.nextStep() },
                    modifier = Modifier.weight(if (state.currentStep > 0) 2f else 1f)
                )
            }
        }
    }
}

@Composable
private fun StepWelcomeAndBasics(state: OnboardingUiState, viewModel: OnboardingViewModel) {
    Column {
        Text(
            text = "WHO ARE YOU?",
            style = Typography.headlineMedium.copy(fontWeight = FontWeight.Black)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "\"I don't train casual hobbyists. If you step under my instruction, we measure performance, enforce discipline, and leave excuses at the door.\"",
            style = Typography.bodyMedium.copy(color = GunTextSecondary, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
        )

        Spacer(modifier = Modifier.height(24.dp))

        GunCard {
            Text(text = "CALLSIGN / NAME", style = Typography.labelSmall)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.name,
                onValueChange = { viewModel.updateName(it) },
                placeholder = { Text("Enter your name", color = GunTextMuted) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GunRed,
                    unfocusedBorderColor = GunBorder,
                    focusedTextColor = GunTextPrimary,
                    unfocusedTextColor = GunTextPrimary
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "AGE", style = Typography.labelSmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.ageText,
                        onValueChange = { viewModel.updateAge(it) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GunRed,
                            unfocusedBorderColor = GunBorder,
                            focusedTextColor = GunTextPrimary,
                            unfocusedTextColor = GunTextPrimary
                        )
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "HEIGHT (CM)", style = Typography.labelSmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.heightText,
                        onValueChange = { viewModel.updateHeight(it) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GunRed,
                            unfocusedBorderColor = GunBorder,
                            focusedTextColor = GunTextPrimary,
                            unfocusedTextColor = GunTextPrimary
                        )
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "WEIGHT (KG)", style = Typography.labelSmall)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = state.weightText,
                        onValueChange = { viewModel.updateWeight(it) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GunRed,
                            unfocusedBorderColor = GunBorder,
                            focusedTextColor = GunTextPrimary,
                            unfocusedTextColor = GunTextPrimary
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun StepExperienceAndLevel(state: OnboardingUiState, viewModel: OnboardingViewModel) {
    Column {
        Text(
            text = "TRAINING EXPERIENCE",
            style = Typography.headlineMedium.copy(fontWeight = FontWeight.Black)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Be honest. Overestimating your level leads to bad form, fatigue, or injury. We build monsters step by step.",
            style = Typography.bodyMedium.copy(color = GunTextSecondary)
        )

        Spacer(modifier = Modifier.height(24.dp))

        val levels = listOf(
            Triple(FitnessLevel.BEGINNER, "BEGINNER", "Under 1 year of consistent lifting. Needs motor pattern mastery."),
            Triple(FitnessLevel.INTERMEDIATE, "INTERMEDIATE", "1 to 3 years of structured lifting. Familiar with main barbell lifts."),
            Triple(FitnessLevel.ADVANCED, "ADVANCED", "3+ years of serious training. Requires high volume & periodization.")
        )

        levels.forEach { (level, title, desc) ->
            val isSelected = state.fitnessLevel == level
            GunCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { viewModel.setFitnessLevel(level) },
                borderColor = if (isSelected) GunRed else GunBorder,
                backgroundColor = if (isSelected) GunSurfaceElevated else GunSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = title,
                            style = Typography.titleLarge.copy(
                                color = if (isSelected) GunRedBright else GunTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = desc, style = Typography.bodyMedium)
                    }
                    RadioButton(
                        selected = isSelected,
                        onClick = { viewModel.setFitnessLevel(level) },
                        colors = RadioButtonDefaults.colors(selectedColor = GunRed)
                    )
                }
            }
        }
    }
}

@Composable
private fun StepPrimaryGoal(state: OnboardingUiState, viewModel: OnboardingViewModel) {
    Column {
        Text(
            text = "PRIMARY OBJECTIVE",
            style = Typography.headlineMedium.copy(fontWeight = FontWeight.Black)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "\"Strength and talent decide worth. But ambition and hunger are non-negotiable.\" Select your primary focus.",
            style = Typography.bodyMedium.copy(color = GunTextSecondary)
        )

        Spacer(modifier = Modifier.height(20.dp))

        val goals = listOf(
            GoalType.MUSCLE_BUILDING to "MUSCLE HYPERTROPHY (Build Size & Armor)",
            GoalType.STRENGTH to "RAW POWER (Increase Maximum 1RM)",
            GoalType.FAT_LOSS to "LEAN DEFINITION (Body Composition)",
            GoalType.DISCIPLINE to "DISCIPLINE & HABIT (Zero Excuses)",
            GoalType.GENERAL_FITNESS to "ATHLETIC FUNCTION (Conditioning & Durability)"
        )

        goals.forEach { (type, label) ->
            val isSelected = state.primaryGoalType == type
            GunCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { viewModel.setGoal(type) },
                borderColor = if (isSelected) GunRed else GunBorder,
                backgroundColor = if (isSelected) GunSurfaceElevated else GunSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        style = Typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) GunRedBright else GunTextPrimary
                        )
                    )
                    RadioButton(
                        selected = isSelected,
                        onClick = { viewModel.setGoal(type) },
                        colors = RadioButtonDefaults.colors(selectedColor = GunRed)
                    )
                }
            }
        }
    }
}

@Composable
private fun StepEquipmentAndLocation(state: OnboardingUiState, viewModel: OnboardingViewModel) {
    Column {
        Text(
            text = "WHERE WILL YOU TRAIN?",
            style = Typography.headlineMedium.copy(fontWeight = FontWeight.Black)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "GUN will adapt your exercises to whatever equipment is genuinely available.",
            style = Typography.bodyMedium.copy(color = GunTextSecondary)
        )

        Spacer(modifier = Modifier.height(20.dp))

        val locations = listOf(
            Triple(TrainingLocation.COMMERCIAL_GYM, "COMMERCIAL GYM", "Barbells, cables, racks, dumbbells, machines."),
            Triple(TrainingLocation.HOME_DUMBBELL, "HOME DUMBBELLS", "Adjustable or fixed dumbbells and bodyweight."),
            Triple(TrainingLocation.HOME_BODYWEIGHT, "CALISTHENICS / BODYWEIGHT", "Pull-up bar, floor, and pure bodyweight movements.")
        )

        locations.forEach { (loc, label, desc) ->
            val isSelected = state.trainingLocation == loc
            GunCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { viewModel.setLocation(loc) },
                borderColor = if (isSelected) GunRed else GunBorder,
                backgroundColor = if (isSelected) GunSurfaceElevated else GunSurface
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = label,
                            style = Typography.titleLarge.copy(
                                color = if (isSelected) GunRedBright else GunTextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = desc, style = Typography.bodyMedium)
                    }
                    RadioButton(
                        selected = isSelected,
                        onClick = { viewModel.setLocation(loc) },
                        colors = RadioButtonDefaults.colors(selectedColor = GunRed)
                    )
                }
            }
        }
    }
}

@Composable
private fun StepScheduleAndSafety(state: OnboardingUiState, viewModel: OnboardingViewModel) {
    Column {
        Text(
            text = "SCHEDULE & SAFETY",
            style = Typography.headlineMedium.copy(fontWeight = FontWeight.Black)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "A demanding trainer does not mean a reckless one. Declare your schedule and any physical limitations.",
            style = Typography.bodyMedium.copy(color = GunTextSecondary)
        )

        Spacer(modifier = Modifier.height(20.dp))

        GunCard {
            Text(text = "TRAINING FREQUENCY", style = Typography.labelSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf(3, 4, 5, 6).forEach { days ->
                    val isSelected = state.daysPerWeek == days
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isSelected) GunRed else GunSurfaceElevated)
                            .border(1.dp, if (isSelected) GunRedBright else GunBorder, RoundedCornerShape(8.dp))
                            .clickable { viewModel.setDaysPerWeek(days) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$days Days",
                            style = Typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else GunTextPrimary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "PHYSICAL LIMITATIONS & INJURIES", style = Typography.labelSmall)
            Spacer(modifier = Modifier.height(8.dp))
            val commonInjuries = listOf("Shoulder Impingement", "Lower Back Sensitivity", "Knee Discomfort", "Wrist Sprain")
            commonInjuries.forEach { injury ->
                val checked = state.injuries.contains(injury)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.toggleInjury(injury) }
                        .padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = { viewModel.toggleInjury(injury) },
                        colors = CheckboxDefaults.colors(checkedColor = GunRed)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = injury, style = Typography.bodyLarge)
                }
            }
        }
    }
}
