package com.projectgun.gun.domain.training

import com.projectgun.gun.data.model.*

object ExerciseCatalog {

    val allExercises: List<Exercise> = listOf(
        // CHEST
        Exercise(
            id = "barbell_bench_press",
            name = "Barbell Bench Press",
            category = ExerciseCategory.CHEST,
            movementPattern = MovementPattern.HORIZONTAL_PUSH,
            primaryMuscle = "Pectoralis Major",
            secondaryMuscles = listOf("Anterior Deltoids", "Triceps Brachii"),
            equipmentRequired = EquipmentType.BARBELL,
            difficulty = DifficultyLevel.INTERMEDIATE,
            instructions = listOf(
                "Lie flat on the bench with eyes under the bar.",
                "Grip the bar slightly wider than shoulder-width.",
                "Retract shoulder blades, plant feet firmly into the floor.",
                "Unrack bar, lower with control to mid-chest.",
                "Drive bar up smoothly until elbows are extended without flaring shoulders."
            ),
            defaultSets = 4,
            minReps = 6,
            maxReps = 10,
            defaultRestSec = 120,
            safetyTips = listOf(
                "Never bounce the barbell off your sternum.",
                "Maintain wrist neutral alignment; do not bend wrists backward.",
                "Always use safety pins or a spotter on heavy attempts."
            ),
            alternatives = listOf("dumbbell_bench_press", "push_up", "dips")
        ),
        Exercise(
            id = "dumbbell_bench_press",
            name = "Dumbbell Bench Press",
            category = ExerciseCategory.CHEST,
            movementPattern = MovementPattern.HORIZONTAL_PUSH,
            primaryMuscle = "Pectoralis Major",
            secondaryMuscles = listOf("Anterior Deltoids", "Triceps Brachii"),
            equipmentRequired = EquipmentType.DUMBBELL,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Sit on bench with dumbbells resting on thighs.",
                "Kick dumbbells up into position as you lie back.",
                "Keep a 45-degree elbow tuck; do not flare out at 90 degrees.",
                "Lower dumbbells until hands are level with chest, feeling stretch.",
                "Press up in a slight arc until dumbbells meet over chest."
            ),
            defaultSets = 3,
            minReps = 8,
            maxReps = 12,
            defaultRestSec = 90,
            safetyTips = listOf(
                "Keep core tight and maintain an arched upper back.",
                "Do not drop dumbbells abruptly to the sides when finished."
            ),
            alternatives = listOf("barbell_bench_press", "push_up")
        ),
        Exercise(
            id = "incline_dumbbell_press",
            name = "Incline Dumbbell Press",
            category = ExerciseCategory.CHEST,
            movementPattern = MovementPattern.HORIZONTAL_PUSH,
            primaryMuscle = "Clavicular Pectoralis (Upper Chest)",
            secondaryMuscles = listOf("Anterior Deltoids", "Triceps Brachii"),
            equipmentRequired = EquipmentType.DUMBBELL,
            difficulty = DifficultyLevel.INTERMEDIATE,
            instructions = listOf(
                "Set bench to 30-45 degree incline.",
                "Press dumbbells directly above upper chest.",
                "Lower slowly until dumbbells approach upper chest level.",
                "Drive dumbbells straight up with control."
            ),
            defaultSets = 3,
            minReps = 8,
            maxReps = 12,
            defaultRestSec = 90,
            safetyTips = listOf("Avoid setting the incline too high (above 45 degrees shifts work into anterior deltoids)."),
            alternatives = listOf("dumbbell_bench_press", "push_up")
        ),
        Exercise(
            id = "push_up",
            name = "Standard Push-Up",
            category = ExerciseCategory.CHEST,
            movementPattern = MovementPattern.HORIZONTAL_PUSH,
            primaryMuscle = "Pectoralis Major",
            secondaryMuscles = listOf("Triceps Brachii", "Anterior Deltoids", "Core"),
            equipmentRequired = EquipmentType.BODYWEIGHT,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Assume high plank position with hands just outside shoulder-width.",
                "Engage glutes and brace abdominal wall.",
                "Lower chest towards floor until elbow angle is 90 degrees.",
                "Press ground away, extending elbows fully."
            ),
            defaultSets = 3,
            minReps = 10,
            maxReps = 25,
            defaultRestSec = 60,
            safetyTips = listOf(
                "Keep hips inline with shoulders; do not let lower back sag.",
                "Keep head neutral; do not crane neck forward."
            ),
            alternatives = listOf("dumbbell_bench_press", "dips")
        ),
        Exercise(
            id = "dips",
            name = "Parallel Bar Dips",
            category = ExerciseCategory.CHEST,
            movementPattern = MovementPattern.VERTICAL_PUSH,
            primaryMuscle = "Lower Pectoralis & Triceps",
            secondaryMuscles = listOf("Anterior Deltoids"),
            equipmentRequired = EquipmentType.BODYWEIGHT,
            difficulty = DifficultyLevel.INTERMEDIATE,
            instructions = listOf(
                "Mount parallel bars with locked elbows.",
                "Lean forward 15-20 degrees to bias chest.",
                "Lower until shoulders are just below elbow level.",
                "Drive through palms back to top lockout."
            ),
            defaultSets = 3,
            minReps = 6,
            maxReps = 12,
            defaultRestSec = 90,
            safetyTips = listOf(
                "Do not descend past comfortable shoulder range.",
                "Avoid shrugging shoulders up to ears."
            ),
            alternatives = listOf("push_up", "tricep_pushdown")
        ),
        Exercise(
            id = "cable_crossover",
            name = "Cable Chest Crossover",
            category = ExerciseCategory.CHEST,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Pectoralis Major",
            secondaryMuscles = listOf("Anterior Deltoids"),
            equipmentRequired = EquipmentType.CABLE,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Set pulleys at chest height or slightly higher.",
                "Step forward into a staggered stance with elbows slightly bent.",
                "Bring handles together in front of chest in a hugging motion.",
                "Squeeze pecs hard at peak contraction, return under controlled tension."
            ),
            defaultSets = 3,
            minReps = 12,
            maxReps = 15,
            defaultRestSec = 60,
            safetyTips = listOf("Keep elbow flexion static; do not turn the fly into a press."),
            alternatives = listOf("push_up", "dumbbell_bench_press")
        ),

        // BACK
        Exercise(
            id = "barbell_deadlift",
            name = "Conventional Barbell Deadlift",
            category = ExerciseCategory.BACK,
            movementPattern = MovementPattern.HINGE,
            primaryMuscle = "Posterior Chain (Erector Spinae, Glutes, Hamstrings)",
            secondaryMuscles = listOf("Latissimus Dorsi", "Trapezius", "Forearms"),
            equipmentRequired = EquipmentType.BARBELL,
            difficulty = DifficultyLevel.ADVANCED,
            instructions = listOf(
                "Stand with midfoot under the barbell, hip-width stance.",
                "Hinge at hips and grip the bar just outside legs.",
                "Pull chest proud, take slack out of the bar, brace abdominal core.",
                "Drive through the floor, pulling bar close along shins.",
                "Lock hips and knees simultaneously at the top; do not hyperextend spine."
            ),
            defaultSets = 3,
            minReps = 5,
            maxReps = 8,
            defaultRestSec = 180,
            safetyTips = listOf(
                "Never round the lumbar spine during lift-off.",
                "Keep bar path strictly vertical against legs.",
                "Do not bounce repetitions off the platform."
            ),
            alternatives = listOf("romanian_deadlift", "barbell_row")
        ),
        Exercise(
            id = "pull_up",
            name = "Strict Pull-Up",
            category = ExerciseCategory.BACK,
            movementPattern = MovementPattern.VERTICAL_PULL,
            primaryMuscle = "Latissimus Dorsi",
            secondaryMuscles = listOf("Biceps Brachii", "Rhomboids", "Middle Trapezius"),
            equipmentRequired = EquipmentType.BODYWEIGHT,
            difficulty = DifficultyLevel.INTERMEDIATE,
            instructions = listOf(
                "Grip bar with overhand grip slightly wider than shoulder-width.",
                "Hang at full stretch, depress scapulae first.",
                "Pull chest toward bar by driving elbows down into back pockets.",
                "Clear bar with chin, hold momentary pause, descend under control."
            ),
            defaultSets = 4,
            minReps = 5,
            maxReps = 12,
            defaultRestSec = 90,
            safetyTips = listOf(
                "Eliminate swinging or kipping motion. True strength is strict.",
                "Control negative descent all the way to full arm extension."
            ),
            alternatives = listOf("lat_pulldown", "barbell_row")
        ),
        Exercise(
            id = "barbell_row",
            name = "Barbell Bent-Over Row",
            category = ExerciseCategory.BACK,
            movementPattern = MovementPattern.HORIZONTAL_PULL,
            primaryMuscle = "Latissimus Dorsi & Rhomboids",
            secondaryMuscles = listOf("Posterior Deltoids", "Biceps", "Spinal Erectors"),
            equipmentRequired = EquipmentType.BARBELL,
            difficulty = DifficultyLevel.INTERMEDIATE,
            instructions = listOf(
                "Hinge forward at waist until torso is roughly 45 degrees.",
                "Grip bar overhand, arms hanging vertical.",
                "Pull bar towards lower ribs/umbilicus, squeezing shoulder blades.",
                "Lower weight slowly under constant back tension."
            ),
            defaultSets = 3,
            minReps = 8,
            maxReps = 12,
            defaultRestSec = 90,
            safetyTips = listOf(
                "Keep spine neutral and resist jerking torso upwards.",
                "Brace core throughout every repetition."
            ),
            alternatives = listOf("dumbbell_single_arm_row", "seated_cable_row")
        ),
        Exercise(
            id = "lat_pulldown",
            name = "Wide-Grip Lat Pulldown",
            category = ExerciseCategory.BACK,
            movementPattern = MovementPattern.VERTICAL_PULL,
            primaryMuscle = "Latissimus Dorsi",
            secondaryMuscles = listOf("Biceps", "Teres Major"),
            equipmentRequired = EquipmentType.CABLE,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Secure knees firmly under thigh pads.",
                "Grip bar with wide overhand grip.",
                "Lean back slightly (10 degrees), pull bar to upper clavicle.",
                "Focus on leading with elbows; pause and return slowly."
            ),
            defaultSets = 3,
            minReps = 10,
            maxReps = 12,
            defaultRestSec = 75,
            safetyTips = listOf(
                "Never pull bar behind neck.",
                "Do not use excessive momentum to swing the weight down."
            ),
            alternatives = listOf("pull_up", "seated_cable_row")
        ),
        Exercise(
            id = "seated_cable_row",
            name = "Seated Cable Row",
            category = ExerciseCategory.BACK,
            movementPattern = MovementPattern.HORIZONTAL_PULL,
            primaryMuscle = "Rhomboids & Mid Trapezius",
            secondaryMuscles = listOf("Latissimus Dorsi", "Biceps"),
            equipmentRequired = EquipmentType.CABLE,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Sit with knees slightly bent, feet on footplates.",
                "Grab handle with neutral grip, sit upright with flat back.",
                "Pull handle into abdomen, driving elbows backwards.",
                "Extend arms forward with control, allowing scapulae to protract slightly."
            ),
            defaultSets = 3,
            minReps = 10,
            maxReps = 12,
            defaultRestSec = 75,
            safetyTips = listOf("Do not rock back and forth excessively with your hips."),
            alternatives = listOf("barbell_row", "dumbbell_single_arm_row")
        ),
        Exercise(
            id = "dumbbell_single_arm_row",
            name = "Single-Arm Dumbbell Row",
            category = ExerciseCategory.BACK,
            movementPattern = MovementPattern.HORIZONTAL_PULL,
            primaryMuscle = "Latissimus Dorsi",
            secondaryMuscles = listOf("Rhomboids", "Biceps", "Posterior Deltoids"),
            equipmentRequired = EquipmentType.DUMBBELL,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Place one knee and one hand on flat bench for support.",
                "Keep spine straight and parallel to floor.",
                "Pull dumbbell straight up to hip crease, squeezing lat.",
                "Lower dumbbell smoothly to full arm extension."
            ),
            defaultSets = 3,
            minReps = 8,
            maxReps = 12,
            defaultRestSec = 60,
            safetyTips = listOf("Avoid rotating torso violently during the pull."),
            alternatives = listOf("barbell_row", "seated_cable_row")
        ),
        Exercise(
            id = "face_pull",
            name = "Cable Face Pull",
            category = ExerciseCategory.BACK,
            movementPattern = MovementPattern.HORIZONTAL_PULL,
            primaryMuscle = "Rear Deltoids & Rotator Cuff",
            secondaryMuscles = listOf("Middle and Lower Trapezius"),
            equipmentRequired = EquipmentType.CABLE,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Attach rope attachment to cable pulley at face level.",
                "Grip rope with thumbs facing backwards.",
                "Step back, pull rope directly towards face while separating hands.",
                "Rotate shoulders externally, aiming for a double-bicep pose finish."
            ),
            defaultSets = 3,
            minReps = 12,
            maxReps = 15,
            defaultRestSec = 60,
            safetyTips = listOf("Use moderate weight; prioritize external rotation over heavy load."),
            alternatives = listOf("rear_delt_fly")
        ),

        // LEGS
        Exercise(
            id = "barbell_squat",
            name = "Barbell Back Squat",
            category = ExerciseCategory.LEGS,
            movementPattern = MovementPattern.SQUAT,
            primaryMuscle = "Quadriceps & Gluteus Maximus",
            secondaryMuscles = listOf("Hamstrings", "Adductors", "Erector Spinae"),
            equipmentRequired = EquipmentType.BARBELL,
            difficulty = DifficultyLevel.ADVANCED,
            instructions = listOf(
                "Rest barbell across upper traps (high bar) or rear delts (low bar).",
                "Stance shoulder-width apart with toes flared out 15-30 degrees.",
                "Inhale, brace core with Valsalva maneuver.",
                "Break at hips and knees simultaneously, descending until hip crease is below knee cap.",
                "Drive through whole foot to stand back up powerfully."
            ),
            defaultSets = 4,
            minReps = 6,
            maxReps = 10,
            defaultRestSec = 150,
            safetyTips = listOf(
                "Do not allow knees to cave inwards (valgus collapse).",
                "Keep heels firmly planted on ground at all times."
            ),
            alternatives = listOf("leg_press", "bulgarian_split_squat", "bodyweight_squat")
        ),
        Exercise(
            id = "romanian_deadlift",
            name = "Romanian Deadlift (RDL)",
            category = ExerciseCategory.LEGS,
            movementPattern = MovementPattern.HINGE,
            primaryMuscle = "Hamstrings & Gluteus Maximus",
            secondaryMuscles = listOf("Erector Spinae", "Forearms"),
            equipmentRequired = EquipmentType.BARBELL,
            difficulty = DifficultyLevel.INTERMEDIATE,
            instructions = listOf(
                "Stand upright holding barbell with hip-width stance.",
                "Slight soft bend in knees (maintain this angle throughout).",
                "Push hips directly backwards, skimming bar down thighs.",
                "Stop once deep hamstring stretch is reached (usually below knees).",
                "Drive hips forward to return to upright position."
            ),
            defaultSets = 3,
            minReps = 8,
            maxReps = 12,
            defaultRestSec = 90,
            safetyTips = listOf(
                "This is a hip hinge, not a squat. Do not bend knees more during descent.",
                "Keep back flat and lats engaged."
            ),
            alternatives = listOf("barbell_deadlift", "leg_curl")
        ),
        Exercise(
            id = "bulgarian_split_squat",
            name = "Bulgarian Split Squat",
            category = ExerciseCategory.LEGS,
            movementPattern = MovementPattern.LUNGE,
            primaryMuscle = "Quadriceps & Glutes",
            secondaryMuscles = listOf("Hamstrings", "Calves", "Core"),
            equipmentRequired = EquipmentType.DUMBBELL,
            difficulty = DifficultyLevel.INTERMEDIATE,
            instructions = listOf(
                "Place rear foot on bench behind you.",
                "Step front foot out into comfortable lunge position.",
                "Lower back knee towards floor in controlled vertical descent.",
                "Drive through front heel to return to top."
            ),
            defaultSets = 3,
            minReps = 8,
            maxReps = 12,
            defaultRestSec = 75,
            safetyTips = listOf("Keep front knee tracking over second toe; avoid lateral wobble."),
            alternatives = listOf("walking_lunges", "barbell_squat")
        ),
        Exercise(
            id = "leg_press",
            name = "45-Degree Leg Press",
            category = ExerciseCategory.LEGS,
            movementPattern = MovementPattern.SQUAT,
            primaryMuscle = "Quadriceps",
            secondaryMuscles = listOf("Glutes", "Hamstrings"),
            equipmentRequired = EquipmentType.MACHINE,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Sit with back flat against backrest.",
                "Place feet shoulder-width in middle of sled.",
                "Release safety locks, lower weight until knees are at 90 degrees.",
                "Press sled away, stopping just short of locking knees."
            ),
            defaultSets = 3,
            minReps = 10,
            maxReps = 15,
            defaultRestSec = 90,
            safetyTips = listOf(
                "NEVER lock out knees completely with heavy weight.",
                "Keep pelvis pushed firmly into seat; do not allow lower back to round off pad."
            ),
            alternatives = listOf("barbell_squat", "bodyweight_squat")
        ),
        Exercise(
            id = "leg_extension",
            name = "Leg Extension Machine",
            category = ExerciseCategory.LEGS,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Quadriceps",
            secondaryMuscles = emptyList(),
            equipmentRequired = EquipmentType.MACHINE,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Align knee joints with pivot point of machine.",
                "Place pad against lower shins just above ankles.",
                "Extend knees smoothly to full extension, hold peak squeeze.",
                "Lower under control over 2 seconds."
            ),
            defaultSets = 3,
            minReps = 12,
            maxReps = 15,
            defaultRestSec = 60,
            safetyTips = listOf("Avoid ballistic kicking at bottom; maintain constant quadriceps tension."),
            alternatives = listOf("bodyweight_squat", "bulgarian_split_squat")
        ),
        Exercise(
            id = "leg_curl",
            name = "Lying Hamstring Leg Curl",
            category = ExerciseCategory.LEGS,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Hamstrings",
            secondaryMuscles = listOf("Calves (Gastrocnemius)"),
            equipmentRequired = EquipmentType.MACHINE,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Lie face down on machine with pad resting against lower calves.",
                "Hold handles, keep hips flat on pad.",
                "Curl heels up towards glutes as far as possible.",
                "Lower weight slowly to full knee extension."
            ),
            defaultSets = 3,
            minReps = 10,
            maxReps = 15,
            defaultRestSec = 60,
            safetyTips = listOf("Do not allow hips to lift off the bench during the curl."),
            alternatives = listOf("romanian_deadlift")
        ),
        Exercise(
            id = "standing_calf_raise",
            name = "Standing Calf Raise",
            category = ExerciseCategory.LEGS,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Gastrocnemius & Soleus",
            secondaryMuscles = emptyList(),
            equipmentRequired = EquipmentType.MACHINE,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Place balls of feet on platform, balls of feet supported.",
                "Lower heels into deep stretch below platform level.",
                "Drive up onto big toes, squeezing calves at top.",
                "Hold peak contraction for 1 full second."
            ),
            defaultSets = 4,
            minReps = 12,
            maxReps = 20,
            defaultRestSec = 45,
            safetyTips = listOf("Perform with full range of motion; bouncing reps produces zero results."),
            alternatives = listOf("bodyweight_squat")
        ),
        Exercise(
            id = "bodyweight_squat",
            name = "Bodyweight Air Squat",
            category = ExerciseCategory.LEGS,
            movementPattern = MovementPattern.SQUAT,
            primaryMuscle = "Quadriceps & Glutes",
            secondaryMuscles = listOf("Core", "Calves"),
            equipmentRequired = EquipmentType.BODYWEIGHT,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Stand tall with feet shoulder-width apart.",
                "Extend arms forward for counter-balance.",
                "Squat down until thighs are parallel with floor.",
                "Drive through heels to stand upright."
            ),
            defaultSets = 3,
            minReps = 15,
            maxReps = 30,
            defaultRestSec = 45,
            safetyTips = listOf("Keep chest proud and heels flat on ground."),
            alternatives = listOf("barbell_squat", "bulgarian_split_squat")
        ),
        Exercise(
            id = "walking_lunges",
            name = "Dumbbell Walking Lunges",
            category = ExerciseCategory.LEGS,
            movementPattern = MovementPattern.LUNGE,
            primaryMuscle = "Quadriceps & Gluteus",
            secondaryMuscles = listOf("Hamstrings", "Calves"),
            equipmentRequired = EquipmentType.DUMBBELL,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Hold dumbbells at sides with tall posture.",
                "Step forward with right foot, sinking left knee toward ground.",
                "Push off front foot and step immediately into next stride."
            ),
            defaultSets = 3,
            minReps = 10,
            maxReps = 16,
            defaultRestSec = 60,
            safetyTips = listOf("Maintain torso upright; avoid leaning forward onto front thigh."),
            alternatives = listOf("bulgarian_split_squat", "bodyweight_squat")
        ),

        // SHOULDERS
        Exercise(
            id = "overhead_barbell_press",
            name = "Overhead Barbell Press (OHP)",
            category = ExerciseCategory.SHOULDERS,
            movementPattern = MovementPattern.VERTICAL_PUSH,
            primaryMuscle = "Anterior Deltoids",
            secondaryMuscles = listOf("Lateral Deltoids", "Triceps", "Upper Trapezius", "Core"),
            equipmentRequired = EquipmentType.BARBELL,
            difficulty = DifficultyLevel.ADVANCED,
            instructions = listOf(
                "Stand with bar on front shoulders, hands just outside shoulders.",
                "Squeeze glutes and brace core solidly.",
                "Press bar vertically, moving head back slightly to clear chin.",
                "Push head forward once bar clears, locking bar directly over midfoot."
            ),
            defaultSets = 4,
            minReps = 5,
            maxReps = 8,
            defaultRestSec = 120,
            safetyTips = listOf(
                "Do not lean back excessively through lumbar spine.",
                "Keep glutes clenched throughout to protect spine."
            ),
            alternatives = listOf("dumbbell_shoulder_press", "push_up")
        ),
        Exercise(
            id = "dumbbell_shoulder_press",
            name = "Seated Dumbbell Shoulder Press",
            category = ExerciseCategory.SHOULDERS,
            movementPattern = MovementPattern.VERTICAL_PUSH,
            primaryMuscle = "Anterior & Lateral Deltoids",
            secondaryMuscles = listOf("Triceps", "Upper Traps"),
            equipmentRequired = EquipmentType.DUMBBELL,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Sit with back supported on upright bench.",
                "Hold dumbbells at ear height with elbows tucked slightly forward.",
                "Press dumbbells straight overhead until arms are extended.",
                "Lower smoothly back to ear height."
            ),
            defaultSets = 3,
            minReps = 8,
            maxReps = 12,
            defaultRestSec = 90,
            safetyTips = listOf("Do not bang dumbbells together at the top."),
            alternatives = listOf("overhead_barbell_press", "push_up")
        ),
        Exercise(
            id = "dumbbell_lateral_raise",
            name = "Dumbbell Lateral Raise",
            category = ExerciseCategory.SHOULDERS,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Lateral Deltoids (Side Delts)",
            secondaryMuscles = listOf("Upper Trapezius"),
            equipmentRequired = EquipmentType.DUMBBELL,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Stand with dumbbells resting at sides, slight forward torso tilt.",
                "Slight bend in elbows (keep locked).",
                "Raise dumbbells out to sides until arms are parallel to floor.",
                "Lead with elbows, pouring water motion at top. Lower slowly."
            ),
            defaultSets = 4,
            minReps = 12,
            maxReps = 15,
            defaultRestSec = 60,
            safetyTips = listOf(
                "Do not use body momentum or swing hips.",
                "Keep weights moderate to isolate side delts."
            ),
            alternatives = listOf("face_pull")
        ),
        Exercise(
            id = "rear_delt_fly",
            name = "Bent-Over Dumbbell Rear Delt Fly",
            category = ExerciseCategory.SHOULDERS,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Posterior Deltoids (Rear Delts)",
            secondaryMuscles = listOf("Rhomboids", "Middle Trapezius"),
            equipmentRequired = EquipmentType.DUMBBELL,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Hinge at hips until torso is parallel with floor.",
                "Hold light dumbbells beneath chest.",
                "Raise arms out to sides like wings, squeezing rear delts.",
                "Lower with control back to starting position."
            ),
            defaultSets = 3,
            minReps = 12,
            maxReps = 15,
            defaultRestSec = 60,
            safetyTips = listOf("Do not use heavy weights; preserve strict isolation."),
            alternatives = listOf("face_pull")
        ),

        // ARMS
        Exercise(
            id = "barbell_curl",
            name = "Barbell Bicep Curl",
            category = ExerciseCategory.ARMS,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Biceps Brachii",
            secondaryMuscles = listOf("Brachialis", "Forearms"),
            equipmentRequired = EquipmentType.BARBELL,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Stand upright holding barbell with underhand shoulder-width grip.",
                "Pin elbows to sides of ribcage.",
                "Curl bar up toward shoulders by flexing biceps.",
                "Squeeze at top, lower slowly over 2-3 seconds."
            ),
            defaultSets = 3,
            minReps = 8,
            maxReps = 12,
            defaultRestSec = 60,
            safetyTips = listOf("Do not swing hips back and forth to hoist the bar."),
            alternatives = listOf("hammer_curl", "incline_dumbbell_curl")
        ),
        Exercise(
            id = "hammer_curl",
            name = "Dumbbell Hammer Curl",
            category = ExerciseCategory.ARMS,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Brachialis & Brachioradialis",
            secondaryMuscles = listOf("Biceps Brachii"),
            equipmentRequired = EquipmentType.DUMBBELL,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Hold dumbbells with neutral (palms facing inward) grip.",
                "Keep elbows stationary at sides.",
                "Curl dumbbells upwards until forearms are near vertical.",
                "Lower with total control."
            ),
            defaultSets = 3,
            minReps = 10,
            maxReps = 12,
            defaultRestSec = 60,
            safetyTips = listOf("Builds forearm thickness and elbow joint durability."),
            alternatives = listOf("barbell_curl")
        ),
        Exercise(
            id = "incline_dumbbell_curl",
            name = "Incline Dumbbell Curl",
            category = ExerciseCategory.ARMS,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Long Head of Biceps",
            secondaryMuscles = emptyList(),
            equipmentRequired = EquipmentType.DUMBBELL,
            difficulty = DifficultyLevel.INTERMEDIATE,
            instructions = listOf(
                "Lie back on incline bench set to 45-60 degrees.",
                "Let arms hang straight down behind torso.",
                "Curl dumbbells up while keeping elbows pointed down.",
                "Lower slowly into deep stretch."
            ),
            defaultSets = 3,
            minReps = 10,
            maxReps = 12,
            defaultRestSec = 60,
            safetyTips = listOf("Do not heave shoulders forward to lift dumbbells."),
            alternatives = listOf("barbell_curl", "hammer_curl")
        ),
        Exercise(
            id = "tricep_pushdown",
            name = "Cable Tricep Rope Pushdown",
            category = ExerciseCategory.ARMS,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Triceps Brachii (Lateral & Medial Heads)",
            secondaryMuscles = emptyList(),
            equipmentRequired = EquipmentType.CABLE,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Attach rope to high pulley.",
                "Tuck elbows tight to ribcage.",
                "Push rope down, spreading ends apart at bottom lockout.",
                "Allow hands to return to chest height while keeping elbows pinned."
            ),
            defaultSets = 3,
            minReps = 10,
            maxReps = 15,
            defaultRestSec = 60,
            safetyTips = listOf("Do not let elbows drift forward during reps."),
            alternatives = listOf("dips", "skull_crushers")
        ),
        Exercise(
            id = "skull_crushers",
            name = "EZ-Bar Skull Crushers (Lying Triceps Extension)",
            category = ExerciseCategory.ARMS,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Triceps Brachii (Long Head)",
            secondaryMuscles = emptyList(),
            equipmentRequired = EquipmentType.BARBELL,
            difficulty = DifficultyLevel.INTERMEDIATE,
            instructions = listOf(
                "Lie flat on bench holding EZ bar straight up.",
                "Angle upper arms back slightly (10 degrees).",
                "Hinge at elbows, lowering bar toward crown of head.",
                "Extend elbows back to starting position."
            ),
            defaultSets = 3,
            minReps = 8,
            maxReps = 12,
            defaultRestSec = 75,
            safetyTips = listOf(
                "Lower with complete control to avoid striking forehead.",
                "Keep elbows tucked, not flared outward."
            ),
            alternatives = listOf("tricep_pushdown", "dips")
        ),

        // CORE
        Exercise(
            id = "hanging_leg_raise",
            name = "Hanging Leg Raise",
            category = ExerciseCategory.CORE,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Rectus Abdominis & Hip Flexors",
            secondaryMuscles = listOf("Forearms"),
            equipmentRequired = EquipmentType.BODYWEIGHT,
            difficulty = DifficultyLevel.INTERMEDIATE,
            instructions = listOf(
                "Hang from pull-up bar with overhand grip.",
                "Keep legs straight or soft knees.",
                "Curl pelvis upwards, raising feet towards bar height.",
                "Lower legs slowly without swinging."
            ),
            defaultSets = 3,
            minReps = 8,
            maxReps = 15,
            defaultRestSec = 60,
            safetyTips = listOf("Do not kip or swing hips; strict abdominal contraction only."),
            alternatives = listOf("plank", "ab_wheel")
        ),
        Exercise(
            id = "plank",
            name = "Hardstyle Forearm Plank",
            category = ExerciseCategory.CORE,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Transverse Abdominis & Rectus Abdominis",
            secondaryMuscles = listOf("Glutes", "Shoulders"),
            equipmentRequired = EquipmentType.BODYWEIGHT,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Rest on forearms and balls of feet.",
                "Squeeze glutes maximally and pull belly button to spine.",
                "Pull elbows toward toes isometrically to create intense tension.",
                "Hold rigid line from head to heels."
            ),
            defaultSets = 3,
            minReps = 30, // seconds
            maxReps = 60,
            defaultRestSec = 45,
            safetyTips = listOf("If lower back begins to sag, end set immediately."),
            alternatives = listOf("hanging_leg_raise", "ab_wheel")
        ),
        Exercise(
            id = "ab_wheel",
            name = "Ab Wheel Rollout",
            category = ExerciseCategory.CORE,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Rectus Abdominis",
            secondaryMuscles = listOf("Lats", "Shoulders"),
            equipmentRequired = EquipmentType.BODYWEIGHT,
            difficulty = DifficultyLevel.ADVANCED,
            instructions = listOf(
                "Kneel on floor holding wheel directly under shoulders.",
                "Round upper back slightly and brace abs hard.",
                "Roll wheel forward slowly until body is extended.",
                "Use abs to pull wheel back under shoulders."
            ),
            defaultSets = 3,
            minReps = 6,
            maxReps = 12,
            defaultRestSec = 60,
            safetyTips = listOf(
                "Do not allow lower back to arch into extension.",
                "Only roll as far as you can maintain abdominal control."
            ),
            alternatives = listOf("plank", "hanging_leg_raise")
        ),

        // CONDITIONING / YAMAZAKI MERITOCRACY SUITE (Safe Real-world Training)
        Exercise(
            id = "farmers_walk",
            name = "Heavy Dumbbell Farmer's Walk",
            category = ExerciseCategory.FULL_BODY,
            movementPattern = MovementPattern.CARRY,
            primaryMuscle = "Grip & Forearms",
            secondaryMuscles = listOf("Trapezius", "Core", "Calves"),
            equipmentRequired = EquipmentType.DUMBBELL,
            difficulty = DifficultyLevel.INTERMEDIATE,
            instructions = listOf(
                "Deadlift heavy dumbbells up to sides.",
                "Stand tall with shoulders pulled back and down.",
                "Walk forward with short, deliberate, controlled strides.",
                "Maintain complete spinal rigidity without swaying."
            ),
            defaultSets = 3,
            minReps = 40, // meters or steps
            maxReps = 60,
            defaultRestSec = 90,
            safetyTips = listOf("Do not allow weights to pull shoulders into slouching posture."),
            alternatives = listOf("barbell_deadlift")
        ),
        Exercise(
            id = "jump_rope_intervals",
            name = "Jump Rope Conditioning Intervals",
            category = ExerciseCategory.FULL_BODY,
            movementPattern = MovementPattern.ISOLATION,
            primaryMuscle = "Calves & Cardiovascular System",
            secondaryMuscles = listOf("Forearms", "Shoulders"),
            equipmentRequired = EquipmentType.BODYWEIGHT,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "Hold rope handles at hip height.",
                "Turn rope with wrist rotation, staying light on balls of feet.",
                "Perform 60 seconds of steady jumps followed by 30 seconds rest."
            ),
            defaultSets = 5,
            minReps = 60, // seconds
            maxReps = 120,
            defaultRestSec = 30,
            safetyTips = listOf("Land softly on balls of feet; do not strike with heels."),
            alternatives = listOf("burpees")
        ),
        Exercise(
            id = "burpees",
            name = "Conditioning Burpees",
            category = ExerciseCategory.FULL_BODY,
            movementPattern = MovementPattern.FULL_BODY,
            primaryMuscle = "Full Body Cardiovascular & Muscular Endurance",
            secondaryMuscles = listOf("Chest", "Quads", "Core"),
            equipmentRequired = EquipmentType.BODYWEIGHT,
            difficulty = DifficultyLevel.BEGINNER,
            instructions = listOf(
                "From standing, drop hands to floor.",
                "Kick feet back into push-up position, touch chest to floor.",
                "Jump feet back to hands, jump vertically with hands overhead."
            ),
            defaultSets = 4,
            minReps = 10,
            maxReps = 20,
            defaultRestSec = 60,
            safetyTips = listOf("Keep pace controlled; prioritize smooth rhythm over chaotic flailing."),
            alternatives = listOf("jump_rope_intervals")
        )
    )

    fun getById(id: String): Exercise? = allExercises.find { it.id == id }

    fun getByCategory(category: ExerciseCategory): List<Exercise> =
        allExercises.filter { it.category == category }

    fun getRecommendedForSplit(splitName: String, equipment: List<String>): List<Exercise> {
        val hasGym = equipment.contains("Full Gym") || equipment.contains("COMMERCIAL_GYM")
        val hasDumbbells = hasGym || equipment.contains("Dumbbells") || equipment.contains("HOME_DUMBBELL")

        val available = allExercises.filter { exercise ->
            when (exercise.equipmentRequired) {
                EquipmentType.BODYWEIGHT -> true
                EquipmentType.DUMBBELL -> hasDumbbells
                EquipmentType.BARBELL, EquipmentType.CABLE, EquipmentType.MACHINE -> hasGym
                EquipmentType.RESISTANCE_BAND -> true
            }
        }

        return when (splitName.uppercase()) {
            "FULL_BODY" -> listOfNotNull(
                available.find { it.id == "barbell_squat" } ?: available.find { it.id == "bodyweight_squat" },
                available.find { it.id == "barbell_bench_press" } ?: available.find { it.id == "push_up" },
                available.find { it.id == "barbell_row" } ?: available.find { it.id == "pull_up" },
                available.find { it.id == "overhead_barbell_press" } ?: available.find { it.id == "dumbbell_lateral_raise" },
                available.find { it.id == "romanian_deadlift" } ?: available.find { it.id == "plank" },
                available.find { it.id == "barbell_curl" } ?: available.find { it.id == "hammer_curl" }
            )
            "UPPER" -> listOfNotNull(
                available.find { it.id == "barbell_bench_press" } ?: available.find { it.id == "push_up" },
                available.find { it.id == "barbell_row" } ?: available.find { it.id == "pull_up" },
                available.find { it.id == "incline_dumbbell_press" } ?: available.find { it.id == "dips" },
                available.find { it.id == "lat_pulldown" } ?: available.find { it.id == "dumbbell_single_arm_row" },
                available.find { it.id == "dumbbell_lateral_raise" },
                available.find { it.id == "tricep_pushdown" } ?: available.find { it.id == "push_up" },
                available.find { it.id == "barbell_curl" }
            )
            "LOWER" -> listOfNotNull(
                available.find { it.id == "barbell_squat" } ?: available.find { it.id == "bodyweight_squat" },
                available.find { it.id == "romanian_deadlift" } ?: available.find { it.id == "bulgarian_split_squat" },
                available.find { it.id == "leg_press" } ?: available.find { it.id == "walking_lunges" },
                available.find { it.id == "leg_curl" } ?: available.find { it.id == "standing_calf_raise" },
                available.find { it.id == "hanging_leg_raise" } ?: available.find { it.id == "plank" }
            )
            "PUSH" -> listOfNotNull(
                available.find { it.id == "barbell_bench_press" } ?: available.find { it.id == "push_up" },
                available.find { it.id == "overhead_barbell_press" } ?: available.find { it.id == "dumbbell_shoulder_press" },
                available.find { it.id == "incline_dumbbell_press" } ?: available.find { it.id == "dips" },
                available.find { it.id == "dumbbell_lateral_raise" },
                available.find { it.id == "tricep_pushdown" } ?: available.find { it.id == "skull_crushers" }
            )
            "PULL" -> listOfNotNull(
                available.find { it.id == "barbell_deadlift" } ?: available.find { it.id == "pull_up" },
                available.find { it.id == "barbell_row" } ?: available.find { it.id == "dumbbell_single_arm_row" },
                available.find { it.id == "lat_pulldown" } ?: available.find { it.id == "pull_up" },
                available.find { it.id == "face_pull" } ?: available.find { it.id == "rear_delt_fly" },
                available.find { it.id == "barbell_curl" } ?: available.find { it.id == "hammer_curl" }
            )
            "LEGS" -> listOfNotNull(
                available.find { it.id == "barbell_squat" } ?: available.find { it.id == "bodyweight_squat" },
                available.find { it.id == "romanian_deadlift" } ?: available.find { it.id == "bulgarian_split_squat" },
                available.find { it.id == "leg_press" } ?: available.find { it.id == "walking_lunges" },
                available.find { it.id == "standing_calf_raise" },
                available.find { it.id == "plank" }
            )
            else -> allExercises.take(5)
        }
    }
}
