package com.aspharier.finora.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem(route = Routes.HOME, label = "Home", icon = Icons.Filled.Home)

    object Exchange :
            BottomNavItem(route = Routes.ADD_EXPENSE, label = "Add", icon = Icons.Filled.Add)

    object Statistics :
            BottomNavItem(route = Routes.STATISTICS, label = "Stats", icon = Icons.Filled.Info)
}
