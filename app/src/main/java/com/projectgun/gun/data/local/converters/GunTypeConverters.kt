package com.projectgun.gun.data.local.converters

import androidx.room.TypeConverter
import com.projectgun.gun.data.model.*

class GunTypeConverters {

    @TypeConverter
    fun fromStringList(list: List<String>?): String {
        return list?.joinToString(";;;") ?: ""
    }

    @TypeConverter
    fun toStringList(data: String?): List<String> {
        if (data.isNullOrBlank()) return emptyList()
        return data.split(";;;").filter { it.isNotBlank() }
    }

    @TypeConverter
    fun fromUnitSystem(value: UnitSystem?): String = value?.name ?: UnitSystem.METRIC.name

    @TypeConverter
    fun toUnitSystem(value: String?): UnitSystem =
        value?.let { runCatching { enumValueOf<UnitSystem>(it) }.getOrNull() } ?: UnitSystem.METRIC

    @TypeConverter
    fun fromFitnessLevel(value: FitnessLevel?): String = value?.name ?: FitnessLevel.BEGINNER.name

    @TypeConverter
    fun toFitnessLevel(value: String?): FitnessLevel =
        value?.let { runCatching { enumValueOf<FitnessLevel>(it) }.getOrNull() } ?: FitnessLevel.BEGINNER

    @TypeConverter
    fun fromActivityLevel(value: ActivityLevel?): String = value?.name ?: ActivityLevel.MODERATELY_ACTIVE.name

    @TypeConverter
    fun toActivityLevel(value: String?): ActivityLevel =
        value?.let { runCatching { enumValueOf<ActivityLevel>(it) }.getOrNull() } ?: ActivityLevel.MODERATELY_ACTIVE

    @TypeConverter
    fun fromTrainingLocation(value: TrainingLocation?): String = value?.name ?: TrainingLocation.COMMERCIAL_GYM.name

    @TypeConverter
    fun toTrainingLocation(value: String?): TrainingLocation =
        value?.let { runCatching { enumValueOf<TrainingLocation>(it) }.getOrNull() } ?: TrainingLocation.COMMERCIAL_GYM

    @TypeConverter
    fun fromGoalType(value: GoalType?): String = value?.name ?: GoalType.GENERAL_FITNESS.name

    @TypeConverter
    fun toGoalType(value: String?): GoalType =
        value?.let { runCatching { enumValueOf<GoalType>(it) }.getOrNull() } ?: GoalType.GENERAL_FITNESS

    @TypeConverter
    fun fromEpisodeType(value: EpisodeType?): String = value?.name ?: EpisodeType.MILESTONE.name

    @TypeConverter
    fun toEpisodeType(value: String?): EpisodeType =
        value?.let { runCatching { enumValueOf<EpisodeType>(it) }.getOrNull() } ?: EpisodeType.MILESTONE

    @TypeConverter
    fun fromExerciseCategory(value: ExerciseCategory?): String = value?.name ?: ExerciseCategory.FULL_BODY.name

    @TypeConverter
    fun toExerciseCategory(value: String?): ExerciseCategory =
        value?.let { runCatching { enumValueOf<ExerciseCategory>(it) }.getOrNull() } ?: ExerciseCategory.FULL_BODY

    @TypeConverter
    fun fromMovementPattern(value: MovementPattern?): String = value?.name ?: MovementPattern.SQUAT.name

    @TypeConverter
    fun toMovementPattern(value: String?): MovementPattern =
        value?.let { runCatching { enumValueOf<MovementPattern>(it) }.getOrNull() } ?: MovementPattern.SQUAT

    @TypeConverter
    fun fromEquipmentType(value: EquipmentType?): String = value?.name ?: EquipmentType.BODYWEIGHT.name

    @TypeConverter
    fun toEquipmentType(value: String?): EquipmentType =
        value?.let { runCatching { enumValueOf<EquipmentType>(it) }.getOrNull() } ?: EquipmentType.BODYWEIGHT

    @TypeConverter
    fun fromDifficultyLevel(value: DifficultyLevel?): String = value?.name ?: DifficultyLevel.BEGINNER.name

    @TypeConverter
    fun toDifficultyLevel(value: String?): DifficultyLevel =
        value?.let { runCatching { enumValueOf<DifficultyLevel>(it) }.getOrNull() } ?: DifficultyLevel.BEGINNER

    @TypeConverter
    fun fromMessageSender(value: MessageSender?): String = value?.name ?: MessageSender.GUN.name

    @TypeConverter
    fun toMessageSender(value: String?): MessageSender =
        value?.let { runCatching { enumValueOf<MessageSender>(it) }.getOrNull() } ?: MessageSender.GUN
}
