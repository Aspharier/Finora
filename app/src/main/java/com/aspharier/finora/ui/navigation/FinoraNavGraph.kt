package com.aspharier.finora.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aspharier.finora.ui.screens.exchange.ExchangeScreen
import com.aspharier.finora.ui.screens.home.HomeScreen
import com.aspharier.finora.ui.screens.statistics.StatisticsScreen

private const val ANIMATION_DURATION = 350

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
                slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(ANIMATION_DURATION)
                ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
            },
            exitTransition = {
                slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Start,
                        animationSpec = tween(ANIMATION_DURATION)
                ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
            },
            popEnterTransition = {
                slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(ANIMATION_DURATION)
                ) + fadeIn(animationSpec = tween(ANIMATION_DURATION))
            },
            popExitTransition = {
                slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.End,
                        animationSpec = tween(ANIMATION_DURATION)
                ) + fadeOut(animationSpec = tween(ANIMATION_DURATION))
            }
    ) {
        composable(Routes.HOME) {
            HomeScreen(onToggleTheme = onToggleTheme, isDarkTheme = isDarkTheme)
        }
        composable(Routes.EXCHANGE) { ExchangeScreen(isDarkTheme = isDarkTheme) }
        composable(Routes.STATISTICS) { StatisticsScreen() }
    }
}
