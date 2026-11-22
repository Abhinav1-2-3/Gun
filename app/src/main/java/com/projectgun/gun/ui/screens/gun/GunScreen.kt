package com.projectgun.gun.ui.screens.gun

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.projectgun.gun.data.model.ChatMessage
import com.projectgun.gun.data.model.MessageSender
import com.projectgun.gun.ui.components.GunTopBar
import com.projectgun.gun.ui.navigation.Screen
import com.projectgun.gun.ui.theme.*

@Composable
fun GunScreen(
    viewModel: GunViewModel,
    onNavigate: (Screen) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Scaffold(
        containerColor = GunBlack,
        topBar = {
            GunTopBar(title = "GUN PARK — TRAINER")
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GunDarkCharcoal)
                    .border(1.dp, GunBorder)
                    .padding(12.dp)
            ) {
                // Quick Action Chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.quickPrompts) { prompt ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(GunSurfaceElevated)
                                .border(1.dp, GunBorderLight, RoundedCornerShape(16.dp))
                                .clickable {
                                    viewModel.sendMessage(prompt)
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = prompt,
                                style = Typography.labelSmall.copy(color = GunTextPrimary, fontSize = 11.sp)
                            )
                        }
                    }
                }

                // Input row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = { Text("Consult Gun or report numbers...", color = GunTextMuted, fontSize = 13.sp) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GunRed,
                            unfocusedBorderColor = GunBorder,
                            focusedTextColor = GunTextPrimary,
                            unfocusedTextColor = GunTextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.sendMessage(inputText)
                                inputText = ""
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(GunRed)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    ) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(state.messages) { message ->
                ChatBubble(
                    message = message,
                    onActionClick = { prompt ->
                        when {
                            prompt.contains("TRAIN") -> onNavigate(Screen.Train)
                            prompt.contains("PROGRESS") -> onNavigate(Screen.Progress)
                            prompt.contains("Recovery") -> onNavigate(Screen.Home)
                            prompt.contains("History") -> onNavigate(Screen.Train)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: ChatMessage,
    onActionClick: (String) -> Unit
) {
    val isGun = message.sender == MessageSender.GUN

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isGun) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        if (isGun) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(GunRed),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = "Gun",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
        }

        Column(
            modifier = Modifier
                .widthIn(max = 290.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(if (isGun) GunSurfaceElevated else GunRed)
                .border(
                    width = 1.dp,
                    color = if (isGun) GunBorderLight else GunRedBright,
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(14.dp)
        ) {
            if (isGun) {
                Text(
                    text = "GUN PARK",
                    style = Typography.labelSmall.copy(color = GunRedBright, letterSpacing = 1.sp, fontSize = 9.sp)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = message.content,
                style = Typography.bodyLarge.copy(
                    color = GunTextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            )

            // Optional Action Prompt pill
            if (message.actionPrompt != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(GunBlack)
                        .border(1.dp, GunRed, RoundedCornerShape(6.dp))
                        .clickable { onActionClick(message.actionPrompt) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "→ ${message.actionPrompt}",
                        style = Typography.labelSmall.copy(color = GunRedBright, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
