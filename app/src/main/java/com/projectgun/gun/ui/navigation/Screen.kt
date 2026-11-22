package com.projectgun.gun.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Train : Screen("train")
    object ActiveWorkout : Screen("active_workout/{sessionId}") {
        fun createRoute(sessionId: Long) = "active_workout/$sessionId"
    }
    object Gun : Screen("gun")
    object Progress : Screen("progress")
    object Profile : Screen("profile")
}
