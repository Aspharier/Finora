package com.aspharier.finora.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aspharier.finora.ui.screens.expense.AddExpenseScreen
import com.aspharier.finora.ui.screens.home.HomeScreen
import com.aspharier.finora.ui.screens.statistics.StatisticsScreen

private const val ANIMATION_DURATION = 500

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FinoraNavGraph(
        navController: NavHostController,
        modifier: Modifier = Modifier,
        onToggleTheme: () -> Unit = {},
        isDarkTheme: Boolean = true
) {
    NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = modifier,
            enterTransition = {
                val targetRoute = targetState.destination.route
                if (targetRoute == Routes.ADD_EXPENSE) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(800)
                    ) + fadeIn(animationSpec = tween(800))
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(ANIMATION_DURATION)
                    ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
                }
            },
            exitTransition = {
                val targetRoute = targetState.destination.route
                if (targetRoute == Routes.ADD_EXPENSE) {
                     fadeOut(animationSpec = tween(800)) // Fade out home
                } else {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(ANIMATION_DURATION)
                    ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
                }
            },
            popEnterTransition = {
                val initialRoute = initialState.destination.route
                if (initialRoute == Routes.ADD_EXPENSE) {
                     fadeIn(animationSpec = tween(800))
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(ANIMATION_DURATION)
                    ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
                }
            },
            popExitTransition = {
                 val initialRoute = initialState.destination.route
                if (initialRoute == Routes.ADD_EXPENSE) {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(800)
                    ) + fadeOut(animationSpec = tween(800))
                } else {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(ANIMATION_DURATION)
                    ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
                }
            }
    ) {
        composable(Routes.HOME) {
            HomeScreen(onToggleTheme = onToggleTheme, isDarkTheme = isDarkTheme)
        }
        composable(Routes.ADD_EXPENSE) {
            AddExpenseScreen(onNavigateBack = { navController.popBackStack() })
        }
        composable(Routes.STATISTICS) { StatisticsScreen() }
    }
}
