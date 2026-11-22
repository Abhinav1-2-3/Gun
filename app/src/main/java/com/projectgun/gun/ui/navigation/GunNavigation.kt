package com.projectgun.gun.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.projectgun.gun.GunApplication
import com.projectgun.gun.ui.components.GunBottomBar
import com.projectgun.gun.ui.screens.gun.GunScreen
import com.projectgun.gun.ui.screens.gun.GunViewModel
import com.projectgun.gun.ui.screens.gun.GunViewModelFactory
import com.projectgun.gun.ui.screens.home.HomeScreen
import com.projectgun.gun.ui.screens.home.HomeViewModel
import com.projectgun.gun.ui.screens.home.HomeViewModelFactory
import com.projectgun.gun.ui.screens.onboarding.OnboardingScreen
import com.projectgun.gun.ui.screens.onboarding.OnboardingViewModel
import com.projectgun.gun.ui.screens.onboarding.OnboardingViewModelFactory
import com.projectgun.gun.ui.screens.profile.ProfileScreen
import com.projectgun.gun.ui.screens.profile.ProfileViewModel
import com.projectgun.gun.ui.screens.profile.ProfileViewModelFactory
import com.projectgun.gun.ui.screens.progress.ProgressScreen
import com.projectgun.gun.ui.screens.progress.ProgressViewModel
import com.projectgun.gun.ui.screens.progress.ProgressViewModelFactory
import com.projectgun.gun.ui.screens.train.ActiveWorkoutScreen
import com.projectgun.gun.ui.screens.train.TrainScreen
import com.projectgun.gun.ui.screens.train.TrainViewModel
import com.projectgun.gun.ui.screens.train.TrainViewModelFactory
import com.projectgun.gun.ui.theme.GunBlack

@Composable
fun GunNavigation(
    app: GunApplication,
    navController: NavHostController = rememberNavController()
) {
    val isOnboarded by app.dataStore.isOnboardingCompleted.collectAsState(initial = false)
    val startDestination = if (isOnboarded) Screen.Home.route else Screen.Onboarding.route

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: startDestination

    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.Train.route,
        Screen.Gun.route,
        Screen.Progress.route,
        Screen.Profile.route
    )

    Scaffold(
        containerColor = GunBlack,
        bottomBar = {
            if (showBottomBar) {
                GunBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { screen ->
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // ONBOARDING
            composable(Screen.Onboarding.route) {
                val vm: OnboardingViewModel = viewModel(
                    factory = OnboardingViewModelFactory(
                        app.userRepository,
                        app.goalRepository,
                        app.episodeRepository,
                        app.dataStore
                    )
                )
                OnboardingScreen(
                    viewModel = vm,
                    onComplete = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }

            // HOME
            composable(Screen.Home.route) {
                val vm: HomeViewModel = viewModel(
                    factory = HomeViewModelFactory(
                        app.userRepository,
                        app.goalRepository,
                        app.workoutRepository,
                        app.recoveryRepository,
                        app.episodeRepository,
                        app.dataStore,
                        app.contextEngine
                    )
                )
                HomeScreen(
                    viewModel = vm,
                    onNavigate = { screen -> navController.navigate(screen.route) },
                    onStartWorkout = { navController.navigate(Screen.Train.route) }
                )
            }

            // TRAIN
            composable(Screen.Train.route) {
                val vm: TrainViewModel = viewModel(
                    factory = TrainViewModelFactory(
                        app.workoutRepository,
                        app.exerciseRepository,
                        app.userRepository,
                        app.recoveryRepository,
                        app.dataStore
                    )
                )
                TrainScreen(
                    viewModel = vm,
                    onStartWorkout = { sessionId ->
                        navController.navigate(Screen.ActiveWorkout.createRoute(sessionId))
                    },
                    onResumeWorkout = { sessionId ->
                        navController.navigate(Screen.ActiveWorkout.createRoute(sessionId))
                    }
                )
            }

            // ACTIVE WORKOUT
            composable(
                route = Screen.ActiveWorkout.route,
                arguments = listOf(navArgument("sessionId") { type = NavType.LongType })
            ) { backStackEntry ->
                val sessionId = backStackEntry.arguments?.getLong("sessionId") ?: 0L
                val vm: TrainViewModel = viewModel(
                    factory = TrainViewModelFactory(
                        app.workoutRepository,
                        app.exerciseRepository,
                        app.userRepository,
                        app.recoveryRepository,
                        app.dataStore
                    )
                )
                ActiveWorkoutScreen(
                    sessionId = sessionId,
                    viewModel = vm,
                    onFinish = { navController.popBackStack() },
                    onCancel = { navController.popBackStack() }
                )
            }

            // GUN CONVERSATION INTERFACE
            composable(Screen.Gun.route) {
                val vm: GunViewModel = viewModel(
                    factory = GunViewModelFactory(
                        app.chatRepository,
                        app.contextEngine,
                        app.personalityEngine
                    )
                )
                GunScreen(
                    viewModel = vm,
                    onNavigate = { screen -> navController.navigate(screen.route) }
                )
            }

            // PROGRESS
            composable(Screen.Progress.route) {
                val vm: ProgressViewModel = viewModel(
                    factory = ProgressViewModelFactory(app.workoutRepository)
                )
                ProgressScreen(viewModel = vm)
            }

            // PROFILE
            composable(Screen.Profile.route) {
                val vm: ProfileViewModel = viewModel(
                    factory = ProfileViewModelFactory(
                        app.userRepository,
                        app.goalRepository,
                        app.episodeRepository,
                        app.workoutRepository,
                        app.recoveryRepository,
                        app.dataStore
                    )
                )
                ProfileScreen(
                    viewModel = vm,
                    onResetToOnboarding = {
                        navController.navigate(Screen.Onboarding.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
