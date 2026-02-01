package com.aspharier.finora.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aspharier.finora.ui.screens.exchange.ExchangeScreen
import com.aspharier.finora.ui.screens.home.HomeScreen
import com.aspharier.finora.ui.screens.statistics.StatisticsScreen

@Composable
fun FinoraNavGraph (
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost (
        navController = navController,
        startDestination = Routes.HOME,
        modifier = modifier
    ) {
        composable (Routes.HOME) {
            HomeScreen()
        }
        composable (Routes.EXCHANGE) {
            ExchangeScreen()
        }
        composable (Routes.STATISTICS) {
            StatisticsScreen()
        }
    }
}