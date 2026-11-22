package com.projectgun.gun.domain.personality

import com.projectgun.gun.domain.context.GunLocalContext
import com.projectgun.gun.domain.intent.UserIntent

data class DialogueTemplate(
    val intent: UserIntent,
    val text: String,
    val requiresCompletedWorkouts: Boolean = false,
    val requiresPr: Boolean = false,
    val requiresStreak: Boolean = false
)

object DialogueLibrary {

    val templates: List<DialogueTemplate> = listOf(
        // GREETING
        DialogueTemplate(
            intent = UserIntent.GREETING,
            text = "I am here, {userName}. We have work to do. Are you ready to train, or do you have numbers to report?"
        ),
        DialogueTemplate(
            intent = UserIntent.GREETING,
            text = "State your objective, {userName}. Don't waste energy on idle talk. Are we reviewing your progress or executing today's session?"
        ),
        DialogueTemplate(
            intent = UserIntent.GREETING,
            text = "Good. You showed up. In my experience, showing up consistently is the first filter that separates real trainees from talkers."
        ),

        // ASK_TODAY_WORKOUT
        DialogueTemplate(
            intent = UserIntent.ASK_TODAY_WORKOUT,
            text = "Your split dictates the work, not your temporary mood. Today's session is prepared in the TRAIN tab. Focus on strict execution."
        ),
        DialogueTemplate(
            intent = UserIntent.ASK_TODAY_WORKOUT,
            text = "I have your session mapped out. Read the prescribed exercises, respect the rest intervals, and don't rush through warm-up sets. Head to the TRAIN centre."
        ),

        // LOG_WORKOUT
        DialogueTemplate(
            intent = UserIntent.LOG_WORKOUT,
            text = "Recorded. That makes {completedCount} completed sessions in your record. Good work. Now drink water, refuel, and ensure you sleep."
        ),
        DialogueTemplate(
            intent = UserIntent.LOG_WORKOUT,
            text = "The log doesn't lie. Your session is locked in with a lifetime volume of {totalVolume} kg. Take pride in the work, but don't become complacent."
        ),

        // ASK_PROGRESS
        DialogueTemplate(
            intent = UserIntent.ASK_PROGRESS,
            text = "You want an evaluation? Here is the truth: You have completed {completedCount} workouts and moved {totalVolume} kg. Real progress is measured in months, not days. Check the PROGRESS tab for the graphs."
        ),
        DialogueTemplate(
            intent = UserIntent.ASK_PROGRESS,
            text = "Progress is not how you 'feel' in the mirror. It's whether your load increased, your form tightened, and your attendance held. Check your PRs and weekly consistency."
        ),

        // ASK_CONSISTENCY
        DialogueTemplate(
            intent = UserIntent.ASK_CONSISTENCY,
            text = "Your current streak stands at {currentStreak} days. An unbroken line. Anyone can be intense for a week; true discipline is showing up when life gets chaotic."
        ),
        DialogueTemplate(
            intent = UserIntent.ASK_CONSISTENCY,
            text = "Consistency is the only metric that matters to me. You are at a {currentStreak}-day active rhythm. Maintain this standard without excuses."
        ),

        // ASK_RECOVERY
        DialogueTemplate(
            intent = UserIntent.ASK_RECOVERY,
            text = "Your current recovery status is evaluated at {recoveryStatus} (Readiness: {readinessScore}/100). Training without judgement is stupidity. If you are fatigued, reduce volume. If you are primed, attack the weights."
        ),
        DialogueTemplate(
            intent = UserIntent.ASK_RECOVERY,
            text = "Recovery readiness: {readinessScore}/100. Muscles don't grow inside the gym—they tear there. They grow when you feed them and sleep 8 hours. Respect the process."
        ),

        // REPORT_FATIGUE
        DialogueTemplate(
            intent = UserIntent.REPORT_FATIGUE,
            text = "Then we evaluate your recovery before deciding what comes next. Training without judgement is stupidity. If it's pure laziness, push through. If your nervous system is genuinely fried, take an active deload."
        ),
        DialogueTemplate(
            intent = UserIntent.REPORT_FATIGUE,
            text = "Exhaustion is data. If you slept under 6 hours or had heavy sessions consecutively, your readiness drops. Log your recovery stats in the dashboard so I can calibrate your volume."
        ),

        // REPORT_PAIN
        DialogueTemplate(
            intent = UserIntent.REPORT_PAIN,
            text = "Halt. Discomfort and muscular burning are part of training. Sharp joint pain is a red line. I will not allow you to destroy your joints for ego. Rest the affected joint, substitute the movement, and consult a medical professional if it persists."
        ),
        DialogueTemplate(
            intent = UserIntent.REPORT_PAIN,
            text = "Listen carefully: Training through an acute injury does not make you tough; it makes you a liability. Back off the load, eliminate pain-inducing angles, and let tissue heal."
        ),

        // ASK_MOTIVATION
        DialogueTemplate(
            intent = UserIntent.ASK_MOTIVATION,
            text = "Motivation is unreliable. It comes and goes with the weather. Discipline is what you should be building. You don't need to 'feel' like working out—you just need to put your shoes on and execute set one."
        ),
        DialogueTemplate(
            intent = UserIntent.ASK_MOTIVATION,
            text = "Three days without training or complaining won't change your physique. Action will. Stop debating with yourself in your head. The weights are waiting."
        ),
        DialogueTemplate(
            intent = UserIntent.ASK_MOTIVATION,
            text = "You asked for my mentorship. That means you gave up the right to make lazy excuses. Step up to the platform or step aside."
        ),

        // ASK_PR
        DialogueTemplate(
            intent = UserIntent.ASK_PR,
            text = "Show me the numbers. Your top recorded record stands at {topPr}. A measurable improvement. Your previous limit is no longer your limit. Maintain that standard."
        ),
        DialogueTemplate(
            intent = UserIntent.ASK_PR,
            text = "PRs are earned through progressive overload, not wishing. Every kilo added to your lift is a permanent mark of physical capability. Review your trophy board in PROGRESS."
        ),

        // UPDATE_GOALS
        DialogueTemplate(
            intent = UserIntent.UPDATE_GOALS,
            text = "Your current primary directive is: '{primaryGoal}'. If you wish to update or refine your targets, do so in your PROFILE. Once set, commit to it for at least 12 weeks."
        ),

        // ADJUST_STRICTNESS
        DialogueTemplate(
            intent = UserIntent.ADJUST_STRICTNESS,
            text = "You can adjust my strictness level in your PROFILE settings. But remember: a lenient coach creates weak trainees. I treat you with the discipline necessary to forge real results."
        ),

        // PHILOSOPHY / WHO_IS_GUN
        DialogueTemplate(
            intent = UserIntent.PHILOSOPHY,
            text = "I am Gun Park. Some call me the Training Genius, the Nurture Genius, or the White Ghost. I don't care about titles. I care about transforming untalented individuals into undeniable forces through unyielding discipline and precise physical adaptation."
        ),

        // UNKNOWN
        DialogueTemplate(
            intent = UserIntent.UNKNOWN,
            text = "Be precise with your words, {userName}. Are you asking about today's workout, analyzing your progress, checking your recovery, or reporting your sets?"
        ),
        DialogueTemplate(
            intent = UserIntent.UNKNOWN,
            text = "I focus on training, recovery, and measurable performance. Give me a clear command or choose from the action chips below."
        )
    )

    fun interpolate(template: String, context: GunLocalContext): String {
        val name = context.userProfile?.name?.ifBlank { "Trainee" } ?: "Trainee"
        val completedCount = context.completedWorkoutsCount.toString()
        val volume = "%.0f".format(context.totalVolumeKg)
        val streak = context.currentStreak.toString()
        val topPr = context.topPrSummary ?: "None recorded yet. Complete a heavy working set to establish your first PR"
        val goal = context.primaryGoal?.title ?: "Establish consistency & foundation"
        val recoveryStatus = context.recoveryStatusText
        val readinessScore = context.readinessScore.toString()

        return template
            .replace("{userName}", name)
            .replace("{completedCount}", completedCount)
            .replace("{totalVolume}", volume)
            .replace("{currentStreak}", streak)
            .replace("{topPr}", topPr)
            .replace("{primaryGoal}", goal)
            .replace("{recoveryStatus}", recoveryStatus)
            .replace("{readinessScore}", readinessScore)
    }
}
