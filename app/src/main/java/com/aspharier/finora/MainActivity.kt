package com.aspharier.finora

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.aspharier.finora.ui.navigation.FinoraBottomBar
import com.aspharier.finora.ui.navigation.FinoraNavGraph
import com.aspharier.finora.ui.theme.FinTrackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FinTrackTheme {
                FinoraAppRoot()
            }
            }
        }
    }

@Composable
fun FinoraAppRoot() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            FinoraBottomBar(navController = navController)
        }
    ) { paddingValues ->
        FinoraNavGraph(
            navController = navController,
            modifier = Modifier.padding(paddingValues)
        )
    }
}