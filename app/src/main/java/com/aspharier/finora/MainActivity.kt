package com.aspharier.finora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aspharier.finora.ui.navigation.FinoraBottomBar
import com.aspharier.finora.ui.navigation.FinoraNavGraph
import com.aspharier.finora.ui.navigation.Routes
import com.aspharier.finora.ui.theme.FinTrackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkTheme by remember { mutableStateOf(true) }

            FinTrackTheme(isDarkTheme = isDarkTheme) {
                FinoraAppRoot(
                        onToggleTheme = { isDarkTheme = !isDarkTheme },
                        isDarkTheme = isDarkTheme
                )
            }
        }
    }
}

@Composable
fun FinoraAppRoot(onToggleTheme: () -> Unit = {}, isDarkTheme: Boolean = true) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide navigation bar on Add Expense screen
    val showBottomBar = currentRoute != Routes.ADD_EXPENSE

    Scaffold(
            bottomBar = {
                AnimatedVisibility(
                        visible = showBottomBar,
                        enter =
                                slideInVertically(
                                        initialOffsetY = { it },
                                        animationSpec = tween(300)
                                ),
                        exit =
                                slideOutVertically(
                                        targetOffsetY = { it },
                                        animationSpec = tween(300)
                                )
                ) { FinoraBottomBar(navController = navController) }
            },
            containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        // Use Box to allow content to extend behind the navigation bar
        Box(modifier = Modifier.fillMaxSize()) {
            FinoraNavGraph(
                    navController = navController,
                    modifier =
                            Modifier.padding(
                                    top = paddingValues.calculateTopPadding(),
                                    // Don't apply bottom padding - let content extend behind nav
                                    // bar
                                    ),
                    onToggleTheme = onToggleTheme,
                    isDarkTheme = isDarkTheme
            )
        }
    }
}
