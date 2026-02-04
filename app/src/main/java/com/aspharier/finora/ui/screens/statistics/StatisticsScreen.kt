package com.aspharier.finora.ui.screens.statistics

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aspharier.finora.ui.theme.NeonGreen

@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 24.dp)) {
        ModeToggle(state.mode, viewModel::toggleMode)

        Spacer(modifier = Modifier.height(20.dp))

        TotalAmountHeader(state)

        Spacer(modifier = Modifier.height(24.dp))

        TimeRangeSelector(state.timeRange, viewModel::changeRange)

        Spacer(modifier = Modifier.height(24.dp))

        BarChart(state.bars)

        Spacer(modifier = Modifier.height(32.dp))

        ScheduledPaymentsPreview()
    }
}

@Composable
private fun ModeToggle(selected: StatsMode, onSelect: (StatsMode) -> Unit) {
    Box(
            modifier =
                    Modifier.fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(28.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            StatsMode.values().forEach { mode ->
                val isSelected = selected == mode
                Box(
                        modifier =
                                Modifier.weight(1f)
                                        .fillMaxHeight()
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(
                                                if (isSelected) MaterialTheme.colorScheme.surface
                                                else Color.Transparent
                                        )
                                        .clickable { onSelect(mode) },
                        contentAlignment = Alignment.Center
                ) {
                    Text(
                            text = mode.name.lowercase().replaceFirstChar { it.uppercase() },
                            color =
                                    if (isSelected) MaterialTheme.colorScheme.onSurface
                                    else
                                            MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                                    alpha = 0.6f
                                            ),
                            style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun TotalAmountHeader(state: StatisticsState) {

    AnimatedContent(
            targetState = state.totalAmount,
            transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
            label = "amountChange"
    ) { amount ->
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                    text = amount,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.width(12.dp))

            val scale by
                    animateFloatAsState(
                            targetValue = 1f,
                            animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                            label = "trendScale"
                    )

            Icon(
                    imageVector =
                            if (state.isPositiveTrend) Icons.Default.TrendingUp
                            else Icons.Default.TrendingDown,
                    contentDescription = null,
                    tint =
                            if (state.isPositiveTrend) NeonGreen
                            else MaterialTheme.colorScheme.error,
                    modifier =
                            Modifier.graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                            }
            )
        }
    }
}

@Composable
private fun TimeRangeSelector(selected: TimeRange, onSelect: (TimeRange) -> Unit) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        TimeRange.values().forEach { range ->
            val isSelected = selected == range
            Box(
                    modifier =
                            Modifier.width(60.dp)
                                    .height(60.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(
                                            width = if (isSelected) 0.dp else 1.dp,
                                            color =
                                                    if (isSelected) Color.Transparent
                                                    else MaterialTheme.colorScheme.outlineVariant,
                                            shape = RoundedCornerShape(16.dp)
                                    )
                                    .background(if (isSelected) NeonGreen else Color.Transparent)
                                    .clickable { onSelect(range) },
                    contentAlignment = Alignment.Center
            ) {
                Text(
                        text = range.name.first().toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color =
                                if (isSelected) Color.Black
                                else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun BarChart(bars: List<BarData>) {
    val max = bars.maxOf { it.value }
    val maxIndex = bars.indexOfFirst { it.value == max }

    Row(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
            verticalAlignment = Alignment.Bottom
    ) {
        bars.forEachIndexed { index, bar ->
            val isMaxBar = index == maxIndex
            val animatedHeight by
                    animateFloatAsState(
                            targetValue = if (bar.value > 0) bar.value / max else 0.05f,
                            animationSpec =
                                    spring(
                                            dampingRatio = Spring.DampingRatioMediumBouncy,
                                            stiffness = Spring.StiffnessLow
                                    ),
                            label = "barGrow_$index"
                    )

            Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
            ) {
                Box(
                        modifier =
                                Modifier.width(32.dp)
                                        .fillMaxHeight(animatedHeight.coerceIn(0.05f, 1f))
                                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                                        .background(
                                                if (isMaxBar) NeonGreen
                                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                        )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                        text = bar.label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ScheduledPaymentsPreview() {
    Column {
        Text(text = "Scheduled Payments", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(12.dp))

        Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Netflix", style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.weight(1f))
                Text("₹ 499", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
