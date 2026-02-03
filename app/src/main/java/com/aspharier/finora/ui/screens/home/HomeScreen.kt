package com.aspharier.finora.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.CallMade
import androidx.compose.material.icons.automirrored.outlined.CallReceived
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aspharier.finora.ui.theme.NeonGreen

@Composable
fun HomeScreen(
        viewModel: HomeViewModel = hiltViewModel(),
        onToggleTheme: () -> Unit = {},
        isDarkTheme: Boolean = true
) {
        val state by viewModel.state.collectAsState()

        LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
                // Greeting Header
                item {
                        GreetingHeader(
                                userName = state.userName,
                                onToggleTheme = onToggleTheme,
                                isDarkTheme = isDarkTheme
                        )
                }

                // Balance Card
                item { BalanceCard(state) }

                // Quick Actions
                item { QuickActions() }

                // Budget Progress
                item { BudgetProgress(state) }

                // Recent Transactions (Avatars)
                item { RecentTransactionAvatars(state.recentContacts) }

                // Transactions Section
                item {
                        TransactionsSection(
                                transactions = state.recentTransactions,
                                selectedFilter = state.selectedFilter
                        )
                }
        }
}

@Composable
private fun GreetingHeader(userName: String, onToggleTheme: () -> Unit, isDarkTheme: Boolean) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                        // Profile Avatar
                        Box(
                                modifier =
                                        Modifier.size(48.dp)
                                                .clip(CircleShape)
                                                .background(
                                                        Brush.linearGradient(
                                                                colors =
                                                                        listOf(
                                                                                NeonGreen.copy(
                                                                                        alpha = 0.6f
                                                                                ),
                                                                                NeonGreen.copy(
                                                                                        alpha = 0.3f
                                                                                )
                                                                        )
                                                        )
                                                ),
                                contentAlignment = Alignment.Center
                        ) {
                                Text(
                                        text = userName.first().toString(),
                                        style =
                                                MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 20.sp
                                                ),
                                        color = MaterialTheme.colorScheme.onPrimary
                                )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                                Text(
                                        text = "Hello, $userName 👋",
                                        style =
                                                MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.SemiBold
                                                ),
                                        color = MaterialTheme.colorScheme.onBackground
                                )
                                Text(
                                        text = "Welcome Back",
                                        style = MaterialTheme.typography.labelMedium,
                                        color =
                                                MaterialTheme.colorScheme.onBackground.copy(
                                                        alpha = 0.6f
                                                )
                                )
                        }
                }

                // Theme Toggle Button
                Box(
                        modifier =
                                Modifier.size(44.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .clickable { onToggleTheme() },
                        contentAlignment = Alignment.Center
                ) {
                        Icon(
                                imageVector =
                                        if (isDarkTheme) Icons.Outlined.LightMode
                                        else Icons.Outlined.DarkMode,
                                contentDescription = "Toggle Theme",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                        )
                }
        }
}

@Composable
private fun BalanceCard(state: HomeState) {
        var visible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) { visible = true }

        val alpha by
                animateFloatAsState(
                        targetValue = if (visible) 1f else 0f,
                        animationSpec = tween(400),
                        label = "balanceAlpha"
                )

        val translateY by
                animateFloatAsState(
                        targetValue = if (visible) 0f else 24f,
                        animationSpec = tween(400),
                        label = "balanceOffset"
                )

        Card(
                modifier =
                        Modifier.fillMaxWidth()
                                .graphicsLayer(alpha = alpha, translationY = translateY),
                shape = RoundedCornerShape(24.dp),
                colors =
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(8.dp)
        ) {
                Column(modifier = Modifier.padding(20.dp)) {
                        // Top Row: My Balance + Add Card
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Text(
                                        text = "My Balance",
                                        style = MaterialTheme.typography.labelMedium,
                                        color =
                                                MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.7f
                                                )
                                )

                                Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier =
                                                Modifier.clip(RoundedCornerShape(8.dp))
                                                        .clickable {}
                                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                        Text(
                                                text = "Add Card",
                                                style = MaterialTheme.typography.labelMedium,
                                                color =
                                                        MaterialTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.7f
                                                        )
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                                imageVector = Icons.Outlined.Add,
                                                contentDescription = "Add Card",
                                                tint =
                                                        MaterialTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.7f
                                                        ),
                                                modifier = Modifier.size(16.dp)
                                        )
                                }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Balance Amount
                        Text(
                                text =
                                        "${state.currency}${"%, .2f".format(state.totalBalance).replace(" ", "")}",
                                style =
                                        MaterialTheme.typography.displayLarge.copy(
                                                fontSize = 36.sp,
                                                fontWeight = FontWeight.Bold
                                        ),
                                color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Percentage Change
                        Text(
                                text = "+${state.percentageChange}%",
                                style = MaterialTheme.typography.bodyLarge,
                                color = NeonGreen
                        )
                }
        }
}

@Composable
private fun QuickActions() {
        val actions =
                listOf(
                        QuickActionItem("Withdraw", Icons.AutoMirrored.Outlined.CallMade),
                        QuickActionItem("Deposit", Icons.AutoMirrored.Outlined.CallReceived),
                        QuickActionItem("Pay", Icons.Outlined.Payment),
                        QuickActionItem("Scan", Icons.Outlined.QrCodeScanner)
                )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                actions.forEach { action -> ActionItem(label = action.label, icon = action.icon) }
        }
}

private data class QuickActionItem(val label: String, val icon: ImageVector)

@Composable
private fun ActionItem(label: String, icon: ImageVector) {
        val interactionSource = remember { MutableInteractionSource() }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                        modifier =
                                Modifier.size(56.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(MaterialTheme.colorScheme.surface)
                                        .border(
                                                width = 1.dp,
                                                color =
                                                        MaterialTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.1f
                                                        ),
                                                shape = RoundedCornerShape(16.dp)
                                        )
                                        .clickable(
                                                interactionSource = interactionSource,
                                                indication = ripple(bounded = true)
                                        ) {},
                        contentAlignment = Alignment.Center
                ) {
                        Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = NeonGreen,
                                modifier = Modifier.size(24.dp)
                        )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
        }
}

@Composable
private fun BudgetProgress(state: HomeState) {
        val progress = (state.monthlySpent / state.monthlyBudget).coerceIn(0.0, 1.0)

        val animatedProgress by
                animateFloatAsState(
                        targetValue = progress.toFloat(),
                        animationSpec = tween(800),
                        label = "budgetProgress"
                )

        Column {
                // Budget Labels Row
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                ) {
                        Column {
                                Text(
                                        text = "Left to spend",
                                        style = MaterialTheme.typography.labelMedium,
                                        color =
                                                MaterialTheme.colorScheme.onBackground.copy(
                                                        alpha = 0.6f
                                                )
                                )
                                Text(
                                        text =
                                                "${state.currency}${"%, .0f".format(state.leftToSpend).replace(" ", "")}",
                                        style =
                                                MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.SemiBold,
                                                        fontSize = 18.sp
                                                ),
                                        color = MaterialTheme.colorScheme.onBackground
                                )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                                Text(
                                        text = "Monthly budget",
                                        style = MaterialTheme.typography.labelMedium,
                                        color =
                                                MaterialTheme.colorScheme.onBackground.copy(
                                                        alpha = 0.6f
                                                )
                                )
                                Text(
                                        text =
                                                "${state.currency}${"%, .2f".format(state.monthlyBudget).replace(" ", "")}",
                                        style =
                                                MaterialTheme.typography.bodyLarge.copy(
                                                        fontWeight = FontWeight.SemiBold,
                                                        fontSize = 18.sp
                                                ),
                                        color = MaterialTheme.colorScheme.onBackground
                                )
                        }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Bar
                LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier =
                                Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = NeonGreen,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
        }
}

@Composable
private fun RecentTransactionAvatars(contacts: List<ContactAvatar>) {
        Column {
                // Header Row
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        Text(
                                text = "Recent Transaction",
                                style =
                                        MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.SemiBold
                                        ),
                                color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                                text = "See all",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                modifier = Modifier.clickable {}
                        )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Avatars Row
                LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                ) { items(contacts) { contact -> ContactAvatarItem(contact) } }
        }
}

@Composable
private fun ContactAvatarItem(contact: ContactAvatar) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                        modifier =
                                Modifier.size(52.dp)
                                        .clip(CircleShape)
                                        .background(
                                                Brush.linearGradient(
                                                        colors =
                                                                listOf(
                                                                        Color(0xFF4A5568),
                                                                        Color(0xFF2D3748)
                                                                )
                                                )
                                        )
                                        .border(
                                                width = 2.dp,
                                                color =
                                                        MaterialTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.2f
                                                        ),
                                                shape = CircleShape
                                        ),
                        contentAlignment = Alignment.Center
                ) {
                        Text(
                                text = contact.name.first().toString(),
                                style =
                                        MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.SemiBold
                                        ),
                                color = MaterialTheme.colorScheme.onSurface
                        )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                        text = contact.name,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
                )
        }
}

@Composable
private fun TransactionsSection(
        transactions: List<TransactionUiModel>,
        selectedFilter: TransactionFilter
) {
        Column {
                // Header Row
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        Text(
                                text = "Transactions",
                                style =
                                        MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.SemiBold
                                        ),
                                color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                                text = "See All",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                                modifier = Modifier.clickable {}
                        )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Filter Tabs
                TransactionFilterTabs(selectedFilter)

                Spacer(modifier = Modifier.height(16.dp))

                // Transaction List
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        transactions.forEach { transaction -> TransactionListItem(transaction) }
                }
        }
}

@Composable
private fun TransactionFilterTabs(selectedFilter: TransactionFilter) {
        val filters =
                listOf(
                        TransactionFilter.ALL to "All",
                        TransactionFilter.SENT to "Sent",
                        TransactionFilter.REQUEST to "Request",
                        TransactionFilter.TRANSFER to "Transfer",
                        TransactionFilter.REMIT to "Remit"
                )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filters) { (filter, label) ->
                        FilterChip(
                                selected = filter == selectedFilter,
                                onClick = { /* Handle filter change */},
                                label = {
                                        Text(
                                                text = label,
                                                style = MaterialTheme.typography.labelMedium
                                        )
                                },
                                colors =
                                        FilterChipDefaults.filterChipColors(
                                                containerColor = MaterialTheme.colorScheme.surface,
                                                labelColor =
                                                        MaterialTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.7f
                                                        ),
                                                selectedContainerColor = NeonGreen,
                                                selectedLabelColor = Color.Black
                                        ),
                                border =
                                        FilterChipDefaults.filterChipBorder(
                                                borderColor =
                                                        MaterialTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.1f
                                                        ),
                                                selectedBorderColor = NeonGreen,
                                                enabled = true,
                                                selected = filter == selectedFilter
                                        )
                        )
                }
        }
}

@Composable
private fun TransactionListItem(transaction: TransactionUiModel) {
        Row(
                modifier =
                        Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
                // Avatar
                Box(
                        modifier =
                                Modifier.size(44.dp)
                                        .clip(CircleShape)
                                        .background(
                                                Brush.linearGradient(
                                                        colors =
                                                                listOf(
                                                                        Color(0xFF4A5568),
                                                                        Color(0xFF2D3748)
                                                                )
                                                )
                                        ),
                        contentAlignment = Alignment.Center
                ) {
                        Text(
                                text = transaction.merchant.first().toString(),
                                style =
                                        MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.SemiBold
                                        ),
                                color = MaterialTheme.colorScheme.onSurface
                        )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Name and Time
                Column(modifier = Modifier.weight(1f)) {
                        Text(
                                text = transaction.merchant,
                                style =
                                        MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.Medium
                                        ),
                                color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                                text = transaction.timestamp,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                }

                // Amount and Type
                Column(horizontalAlignment = Alignment.End) {
                        Text(
                                text =
                                        (if (transaction.isCredit) "+" else "-") +
                                                "${if (transaction.isCredit) "$" else "$"}${"%,.2f".format(transaction.amount)}",
                                style =
                                        MaterialTheme.typography.bodyLarge.copy(
                                                fontWeight = FontWeight.SemiBold
                                        ),
                                color =
                                        if (transaction.isCredit) NeonGreen
                                        else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                                text = transaction.category,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                }
        }
}
