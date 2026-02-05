package com.aspharier.finora.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.aspharier.finora.ui.theme.DarkSurface
import com.aspharier.finora.ui.theme.NeonGreen
import com.aspharier.finora.ui.theme.PureWhite

@Composable
fun FinoraBottomBar(navController: NavController) {
        val items = listOf(BottomNavItem.Home, BottomNavItem.Exchange, BottomNavItem.Statistics)
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        Box(
                modifier =
                        Modifier.fillMaxWidth()
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .padding(bottom = 12.dp),
                contentAlignment = Alignment.Center
        ) {
                // Floating pill container
                Row(
                        modifier =
                                Modifier.clip(RoundedCornerShape(32.dp))
                                        .background(DarkSurface)
                                        .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        items.forEach { item ->
                                val selected = currentRoute == item.route

                                ExpressiveNavItem(
                                        item = item,
                                        selected = selected,
                                        onClick = {
                                                navController.navigate(item.route) {
                                                        popUpTo(Routes.HOME) { saveState = true }
                                                        launchSingleTop = true
                                                        restoreState = true
                                                }
                                        }
                                )
                        }
                }
        }
}

@Composable
private fun ExpressiveNavItem(item: BottomNavItem, selected: Boolean, onClick: () -> Unit) {
        val interactionSource = remember { MutableInteractionSource() }

        // Animate padding for smooth size transition
        val horizontalPadding by
                animateDpAsState(
                        targetValue = if (selected) 16.dp else 12.dp,
                        animationSpec =
                                spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessVeryLow
                                ),
                        label = "horizontalPadding"
                )

        val verticalPadding by
                animateDpAsState(
                        targetValue = if (selected) 10.dp else 10.dp,
                        animationSpec =
                                spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessVeryLow
                                ),
                        label = "verticalPadding"
                )

        Box(
                modifier =
                        Modifier.clip(RoundedCornerShape(24.dp))
                                .graphicsLayer {} // Hardware acceleration
                                .background(if (selected) NeonGreen else Color.Transparent)
                                .clickable(
                                        interactionSource = interactionSource,
                                        indication = null,
                                        onClick = onClick
                                )
                                .padding(
                                        horizontal = horizontalPadding,
                                        vertical = verticalPadding
                                ),
                contentAlignment = Alignment.Center
        ) {
                Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                        Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                modifier = Modifier.size(22.dp),
                                tint = if (selected) DarkSurface else PureWhite.copy(alpha = 0.7f)
                        )

                        // Animated label - only visible when selected with smooth expand/shrink
                        AnimatedVisibility(
                                visible = selected,
                                enter =
                                        expandHorizontally(
                                                animationSpec =
                                                        spring(
                                                                dampingRatio =
                                                                        Spring.DampingRatioMediumBouncy,
                                                                stiffness = Spring.StiffnessVeryLow
                                                        ),
                                                expandFrom = Alignment.Start
                                        ) +
                                                fadeIn(
                                                        animationSpec =
                                                                spring(
                                                                        dampingRatio =
                                                                                Spring.DampingRatioNoBouncy,
                                                                        stiffness =
                                                                                Spring.StiffnessVeryLow
                                                                )
                                                ),
                                exit =
                                        shrinkHorizontally(
                                                animationSpec =
                                                        spring(
                                                                dampingRatio =
                                                                        Spring.DampingRatioNoBouncy,
                                                                stiffness =
                                                                        Spring.StiffnessMediumLow
                                                        ),
                                                shrinkTowards = Alignment.Start
                                        ) +
                                                fadeOut(
                                                        animationSpec =
                                                                spring(
                                                                        dampingRatio =
                                                                                Spring.DampingRatioNoBouncy,
                                                                        stiffness =
                                                                                Spring.StiffnessMediumLow
                                                                )
                                                )
                        ) {
                                Text(
                                        text = item.label,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = DarkSurface
                                )
                        }
                }
        }
}
