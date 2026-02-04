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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aspharier.finora.domain.model.Category
import com.aspharier.finora.domain.model.CategorySummary
import com.aspharier.finora.ui.theme.NeonGreen

@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Statistics",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        TimeRangeSelector(state.selectedRange, viewModel::onTimeRangeSelected)

        Spacer(modifier = Modifier.height(24.dp))

        // Total Spent Header
        TotalAmountHeader(state)

        Spacer(modifier = Modifier.height(24.dp))

        // Chart
        if (state.chartData.isNotEmpty()) {
            BarChart(state.chartData)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No data available",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Category Breakdown
        Text(
            text = "Category Breakdown",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(state.categoryBreakdown) { summary ->
                CategoryBreakdownItem(summary)
            }
        }
    }
}

@Composable
private fun TotalAmountHeader(state: StatisticsState) {
    Column {
        Text(
            text = "Total Spent",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        AnimatedContent(
            targetState = state.totalSpent,
            transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
            label = "amountChange"
        ) { amount ->
            Text(
                text = "₹ ${String.format("%.2f", amount)}",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        }
        
        if (state.budgetStatus != null) {
            val budget = state.budgetStatus
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { budget.percentageUsed / 100f },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = when {
                    budget.percentageUsed > 100 -> MaterialTheme.colorScheme.error
                    budget.percentageUsed > 80 -> Color(0xFFFFB74D) // Warning Orange
                    else -> NeonGreen
                },
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${String.format("%.1f", budget.percentageUsed)}% of Budget",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "₹ ${String.format("%.2f", budget.remaining)} remaining",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TimeRangeSelector(selected: TimeRange, onSelect: (TimeRange) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TimeRange.entries.forEach { range ->
            val isSelected = selected == range
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) NeonGreen else Color.Transparent)
                    .border(
                        width = 1.dp,
                        color = if (isSelected) Color.Transparent else MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelect(range) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = range.label,
                    style = MaterialTheme.typography.labelLarge,
                    color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun BarChart(data: List<Pair<String, Double>>) {
    val max = data.maxOfOrNull { it.second } ?: 1.0
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        data.forEach { (label, value) ->
            val animatedHeight by animateFloatAsState(
                targetValue = (value / max).toFloat().coerceIn(0.05f, 1f),
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "barHeight"
            )
            
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(animatedHeight)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(NeonGreen)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CategoryBreakdownItem(summary: CategorySummary) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(12.dp)
    ) {
        Icon(
            imageVector = Category.getIconForName(summary.category.iconName),
            contentDescription = null,
            tint = Color(android.graphics.Color.parseColor(summary.category.colorHex)),
            modifier = Modifier
                .size(40.dp)
                .background(
                    Color(android.graphics.Color.parseColor(summary.category.colorHex)).copy(alpha = 0.2f),
                    CircleShape
                )
                .padding(8.dp)
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = summary.category.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "${String.format("%.1f", summary.percentage)}%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Text(
            text = "₹ ${String.format("%.2f", summary.amount)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}
