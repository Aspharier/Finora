package com.aspharier.finora.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Home : BottomNavItem(
        route = Routes.HOME,
        label = "Home",
        icon = Icons.Filled.Home
    )

    object Exchange : BottomNavItem(
        route = Routes.EXCHANGE,
        label = "Exchange",
        icon = Icons.Filled.AccountCircle
    )

    object Statistics : BottomNavItem(
        route = Routes.STATISTICS,
        label = "Stats",
        icon = Icons.Filled.Info
    )
}