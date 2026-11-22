package com.projectgun.gun.domain.intent

enum class UserIntent {
    GREETING,
    ASK_TODAY_WORKOUT,
    LOG_WORKOUT,
    ASK_PROGRESS,
    ASK_CONSISTENCY,
    ASK_RECOVERY,
    REPORT_FATIGUE,
    REPORT_PAIN,
    ASK_MOTIVATION,
    ASK_PR,
    UPDATE_GOALS,
    ADJUST_STRICTNESS,
    PHILOSOPHY,
    UNKNOWN
}

data class IntentMatch(
    val intent: UserIntent,
    val confidence: Float,
    val extractedEntity: String? = null
)

object IntentRecognitionEngine {

    fun recognize(input: String): IntentMatch {
        val clean = input.trim().lowercase()

        if (clean.isBlank()) {
            return IntentMatch(UserIntent.UNKNOWN, 0.0f)
        }

        // 1. Pain or injury takes precedence
        if (matchesAny(clean, listOf("pain", "hurt", "injury", "injured", "sharp pain", "tweak", "strained", "ache in my", "dislocated"))) {
            return IntentMatch(UserIntent.REPORT_PAIN, 0.95f)
        }

        // 2. Fatigue / exhaustion / tired
        if (matchesAny(clean, listOf("exhausted", "tired", "burned out", "no energy", "fatigued", "sore", "drained", "can't move", "sleepy"))) {
            return IntentMatch(UserIntent.REPORT_FATIGUE, 0.90f)
        }

        // 3. Motivation / excuses / lazy / skipped
        if (matchesAny(clean, listOf("don't feel like", "lazy", "skip", "skipped", "no motivation", "give up", "hard today", "excuse", "missed workout", "didn't workout"))) {
            return IntentMatch(UserIntent.ASK_MOTIVATION, 0.90f)
        }

        // 4. Progress analysis
        if (matchesAny(clean, listOf("progress", "am i improving", "getting stronger", "better than last", "analyse", "analyze", "evaluate my progress", "results", "growth"))) {
            return IntentMatch(UserIntent.ASK_PROGRESS, 0.90f)
        }

        // 5. Personal records / PRs / maxes
        if (matchesAny(clean, listOf("pr", "personal record", "max", "record", "1rm", "heaviest", "how much can i", "strength numbers", "show me the numbers"))) {
            return IntentMatch(UserIntent.ASK_PR, 0.90f)
        }

        // 6. Consistency / streak / adherence
        if (matchesAny(clean, listOf("consistency", "streak", "how often", "missed sessions", "adherence", "regularity", "attendance"))) {
            return IntentMatch(UserIntent.ASK_CONSISTENCY, 0.90f)
        }

        // 7. Today's workout / plan / what should I do
        if (matchesAny(clean, listOf("today's workout", "today workout", "what should i train", "prepare workout", "plan today", "what's the routine", "routine today", "train today", "start workout"))) {
            return IntentMatch(UserIntent.ASK_TODAY_WORKOUT, 0.90f)
        }

        // 8. Log workout / completed / finished
        if (matchesAny(clean, listOf("completed my workout", "finished training", "workout done", "logged workout", "done with gym", "i finished", "recorded session"))) {
            return IntentMatch(UserIntent.LOG_WORKOUT, 0.85f)
        }

        // 9. Recovery status
        if (matchesAny(clean, listOf("recovery", "readiness", "should i rest", "need rest", "sleep status", "check recovery"))) {
            return IntentMatch(UserIntent.ASK_RECOVERY, 0.85f)
        }

        // 10. Goals
        if (matchesAny(clean, listOf("goal", "objective", "target", "change goal", "update goal", "new target"))) {
            return IntentMatch(UserIntent.UPDATE_GOALS, 0.80f)
        }

        // 11. Strictness / personality adjustment
        if (matchesAny(clean, listOf("too strict", "be harsher", "stricter", "easier", "personality", "tone down"))) {
            return IntentMatch(UserIntent.ADJUST_STRICTNESS, 0.80f)
        }

        // 12. Philosophy / who is Gun / Lookism / Yamazaki
        if (matchesAny(clean, listOf("who are you", "gun park", "lookism", "yamazaki", "shiro oni", "training genius", "philosophy", "nurture genius"))) {
            return IntentMatch(UserIntent.PHILOSOPHY, 0.90f)
        }

        // 13. Greeting
        if (matchesAny(clean, listOf("hello", "hi", "hey", "good morning", "good evening", "gun", "boss", "greetings"))) {
            return IntentMatch(UserIntent.GREETING, 0.85f)
        }

        return IntentMatch(UserIntent.UNKNOWN, 0.30f)
    }

    private fun matchesAny(text: String, keywords: List<String>): Boolean {
        return keywords.any { keyword ->
            if (keyword.contains(" ")) {
                text.contains(keyword)
            } else {
                text.split(Regex("[\\s,?.!]+")).contains(keyword)
            }
        }
    }
}
