package com.projectgun.gun.ui.screens.profile

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectgun.gun.ui.components.GunCard
import com.projectgun.gun.ui.components.GunPrimaryButton
import com.projectgun.gun.ui.components.GunSecondaryButton
import com.projectgun.gun.ui.components.GunTopBar
import com.projectgun.gun.ui.theme.*

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onResetToOnboarding: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showResetDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadProfileData()
    }

    Scaffold(
        containerColor = GunBlack,
        topBar = {
            GunTopBar(title = "PROFILE & MEMORY")
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Card
            item {
                GunCard(borderColor = GunBorderLight) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(GunSurfaceElevated)
                                .border(1.dp, GunRed, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = GunRed,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = state.userProfile?.name ?: "Trainee",
                                style = Typography.headlineSmall.copy(fontWeight = FontWeight.Black)
                            )
                            Text(
                                text = "${state.userProfile?.fitnessLevel ?: "BEGINNER"}  •  ${state.userProfile?.weightKg ?: 75} kg  •  ${state.userProfile?.heightCm?.toInt() ?: 178} cm",
                                style = Typography.bodyMedium.copy(color = GunTextSecondary)
                            )
                        }
                    }
                }
            }

            // Training Split Selector
            item {
                GunCard {
                    Text(
                        text = "ACTIVE TRAINING SPLIT",
                        style = Typography.labelSmall.copy(letterSpacing = 1.sp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val splits = listOf(
                            "FULL_BODY" to "Full Body",
                            "UPPER_LOWER" to "Upper/Lower",
                            "PUSH_PULL_LEGS" to "PPL"
                        )
                        splits.forEach { (splitKey, label) ->
                            val isSelected = state.activeSplit == splitKey
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) GunRed else GunSurfaceElevated)
                                    .border(1.dp, if (isSelected) GunRedBright else GunBorder, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.updateSplit(splitKey) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
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
                }
            }

            // Gun Personality Calibration
            item {
                GunCard {
                    Text(
                        text = "GUN PERSONALITY CALIBRATION",
                        style = Typography.labelSmall.copy(letterSpacing = 1.sp)
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "STRICTNESS: ${state.strictness}%", style = Typography.labelSmall)
                    Slider(
                        value = state.strictness.toFloat(),
                        onValueChange = { viewModel.updatePersonality(it.toInt(), state.directness) },
                        valueRange = 50f..100f,
                        colors = SliderDefaults.colors(thumbColor = GunRed, activeTrackColor = GunRed)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(text = "DIRECTNESS: ${state.directness}%", style = Typography.labelSmall)
                    Slider(
                        value = state.directness.toFloat(),
                        onValueChange = { viewModel.updatePersonality(state.strictness, it.toInt()) },
                        valueRange = 50f..100f,
                        colors = SliderDefaults.colors(thumbColor = GunRed, activeTrackColor = GunRed)
                    )
                }
            }

            // Adaptive Memory Inspector Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ADAPTIVE MEMORY LOGS (${state.storedEpisodes.size})",
                        style = Typography.labelSmall.copy(letterSpacing = 1.sp, color = GunTextMuted)
                    )
                    TextButton(onClick = { viewModel.exportData() }) {
                        Icon(imageVector = Icons.Default.Download, contentDescription = "Export", tint = GunRedBright, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Export JSON", style = Typography.labelSmall.copy(color = GunRedBright))
                    }
                }
            }

            // Stored Memory Episodes List
            if (state.storedEpisodes.isEmpty()) {
                item {
                    GunCard {
                        Text(
                            text = "No episodes stored in memory yet.",
                            style = Typography.bodyMedium.copy(color = GunTextMuted)
                        )
                    }
                }
            } else {
                items(state.storedEpisodes) { ep ->
                    GunCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "[${ep.type}] ${ep.title}",
                                    style = Typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = ep.description,
                                    style = Typography.bodyMedium.copy(color = GunTextSecondary, fontSize = 12.sp)
                                )
                            }
                            IconButton(onClick = { viewModel.deleteEpisode(ep.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Memory",
                                    tint = GunTextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Factory Reset Button
            item {
                Spacer(modifier = Modifier.height(16.dp))
                GunSecondaryButton(
                    text = "Factory Reset & Wipe Memory",
                    onClick = { showResetDialog = true },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }

    // Export Data Dialog
    state.exportedJson?.let { json ->
        AlertDialog(
            onDismissRequest = { viewModel.clearExport() },
            containerColor = GunDarkCharcoal,
            title = {
                Text(text = "EXPORTED TRAINING RECORDS", style = Typography.titleLarge.copy(color = GunTextPrimary, fontWeight = FontWeight.Bold))
            },
            text = {
                Text(
                    text = json,
                    style = Typography.bodyMedium.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace, fontSize = 11.sp)
                )
            },
            confirmButton = {
                GunPrimaryButton(
                    text = "Copy to Clipboard",
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                        clipboard?.setPrimaryClip(ClipData.newPlainText("GUN Data", json))
                        Toast.makeText(context, "Data copied to clipboard", Toast.LENGTH_SHORT).show()
                        viewModel.clearExport()
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { viewModel.clearExport() }) {
                    Text("Close", color = GunTextMuted)
                }
            }
        )
    }

    // Factory Reset Confirmation Dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            containerColor = GunDarkCharcoal,
            icon = {
                Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = GunRedBright)
            },
            title = {
                Text(text = "RESET ALL DATA?", style = Typography.titleLarge.copy(fontWeight = FontWeight.Black))
            },
            text = {
                Text(
                    text = "This will permanently erase all local training sessions, personal records, and Gun's adaptive memory. Are you certain?",
                    style = Typography.bodyMedium.copy(color = GunTextSecondary)
                )
            },
            confirmButton = {
                GunPrimaryButton(
                    text = "Yes, Reset Everything",
                    backgroundColor = GunRedDark,
                    onClick = {
                        showResetDialog = false
                        viewModel.factoryReset(onResetToOnboarding)
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel", color = GunTextMuted)
                }
            }
        )
    }
}
