package com.projectgun.gun.domain.personality

import com.projectgun.gun.domain.context.GunLocalContext
import com.projectgun.gun.domain.intent.IntentRecognitionEngine
import com.projectgun.gun.domain.intent.UserIntent
import com.projectgun.gun.domain.recovery.SafetyCheckResult
import com.projectgun.gun.domain.recovery.SafetyEngine

data class GunResponse(
    val reply: String,
    val intentDetected: UserIntent,
    val confidence: Float,
    val actionPrompt: String? = null,
    val isSafetyAlert: Boolean = false
)

class PersonalityEngine {

    private var lastTemplateIndex: Int = -1

    fun processInput(
        userInput: String,
        context: GunLocalContext
    ): GunResponse {
        val match = IntentRecognitionEngine.recognize(userInput)

        // Safety check first: If pain or extreme fatigue is reported
        if (match.intent == UserIntent.REPORT_PAIN || context.hasActivePain) {
            val safetyResult = SafetyEngine.evaluateSafety(
                latestRecovery = null,
                consecutiveTrainingDays = 0,
                userLimitations = context.userProfile?.injuriesAndLimitations ?: emptyList()
            )
            val painText = "Hold. Physical pain is not an obstacle to blindly overcome; it is an alarm. Cease loading the aggravated joint immediately. Take 48 hours to evaluate before returning with adjusted movement patterns."
            return GunResponse(
                reply = painText,
                intentDetected = UserIntent.REPORT_PAIN,
                confidence = 0.98f,
                actionPrompt = "Log Recovery in Dashboard",
                isSafetyAlert = true
            )
        }

        // Match templates for this intent
        val candidates = DialogueLibrary.templates.filter { it.intent == match.intent }
        val template = if (candidates.isNotEmpty()) {
            val nextIndex = (lastTemplateIndex + 1) % candidates.size
            lastTemplateIndex = nextIndex
            candidates[nextIndex]
        } else {
            DialogueLibrary.templates.first { it.intent == UserIntent.UNKNOWN }
        }

        val formattedReply = DialogueLibrary.interpolate(template.text, context)

        val actionPrompt = when (match.intent) {
            UserIntent.ASK_TODAY_WORKOUT -> "Open TRAIN Centre"
            UserIntent.ASK_PROGRESS, UserIntent.ASK_PR -> "View PROGRESS Charts"
            UserIntent.ASK_RECOVERY, UserIntent.REPORT_FATIGUE -> "Check Recovery Status"
            UserIntent.LOG_WORKOUT -> "View Workout History"
            else -> null
        }

        return GunResponse(
            reply = formattedReply,
            intentDetected = match.intent,
            confidence = match.confidence,
            actionPrompt = actionPrompt
        )
    }
}
