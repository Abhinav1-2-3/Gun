package com.projectgun.gun.data.model

enum class UnitSystem {
    METRIC, IMPERIAL
}

enum class FitnessLevel {
    BEGINNER, INTERMEDIATE, ADVANCED
}

enum class ActivityLevel {
    SEDENTARY, MODERATELY_ACTIVE, HIGHLY_ACTIVE
}

enum class TrainingLocation {
    HOME_BODYWEIGHT, HOME_DUMBBELL, COMMERCIAL_GYM
}

enum class GoalType {
    MUSCLE_BUILDING,
    STRENGTH,
    FAT_LOSS,
    GENERAL_FITNESS,
    ENDURANCE,
    DISCIPLINE,
    CUSTOM
}

enum class EpisodeType {
    FIRST_WORKOUT,
    PERSONAL_RECORD,
    MISSED_STREAK,
    GOAL_CHANGE,
    SETBACK,
    MILESTONE,
    USER_DECISION,
    INJURY_REPORT
}

enum class ExerciseCategory {
    CHEST, BACK, LEGS, SHOULDERS, ARMS, CORE, FULL_BODY
}

enum class MovementPattern {
    HORIZONTAL_PUSH,
    HORIZONTAL_PULL,
    VERTICAL_PUSH,
    VERTICAL_PULL,
    SQUAT,
    HINGE,
    LUNGE,
    CARRY,
    ISOLATION,
    FULL_BODY
}

enum class EquipmentType {
    BODYWEIGHT,
    DUMBBELL,
    BARBELL,
    CABLE,
    MACHINE,
    RESISTANCE_BAND
}

enum class DifficultyLevel {
    BEGINNER,
    INTERMEDIATE,
    ADVANCED
}
