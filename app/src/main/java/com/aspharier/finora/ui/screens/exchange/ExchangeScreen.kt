package com.aspharier.finora.ui.screens.exchange

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aspharier.finora.ui.theme.NeonGreen
import com.aspharier.finora.ui.theme.PureWhite

@Composable
fun ExchangeScreen(viewModel: ExchangeViewModel = hiltViewModel()) {
        val state by viewModel.state.collectAsState()

        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 24.dp)) {
                Text(
                        text = "Currency Exchange",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(20.dp))

                CurrencyInputCard(
                        label = "From",
                        currency = state.fromCurrency,
                        amount = state.fromAmount
                )

                Spacer(modifier = Modifier.height(12.dp))

                SwapButton { viewModel.onSwap() }

                Spacer(modifier = Modifier.height(12.dp))

                CurrencyInputCard(
                        label = "To",
                        currency = state.toCurrency,
                        amount = state.toAmount,
                        isReadOnly = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                        text = state.rateInfo,
                        style = MaterialTheme.typography.labelMedium,
                        color = PureWhite.copy(alpha = 0.7f)
                )

                Text(
                        text = state.lastUpdated,
                        style = MaterialTheme.typography.labelMedium,
                        color = PureWhite.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                NumericKeypad()

                Spacer(modifier = Modifier.height(20.dp))

                ExchangeCTA()
        }
}

@Composable
private fun CurrencyInputCard(
        label: String,
        currency: CurrencyUiModel,
        amount: String,
        isReadOnly: Boolean = false
) {
        Card(
                shape = RoundedCornerShape(20.dp),
                colors =
                        CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(8.dp)
        ) {
                Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                        ) {
                                Text(
                                        text = "${currency.flag} ${currency.code}",
                                        style = MaterialTheme.typography.bodyLarge
                                )

                                Text(
                                        text = "${currency.symbol} $amount",
                                        style = MaterialTheme.typography.displayLarge,
                                        color =
                                                if (isReadOnly) MaterialTheme.colorScheme.onSurface
                                                else MaterialTheme.colorScheme.primary
                                )
                        }
                }
        }
}

@Composable
private fun SwapButton(onClick: () -> Unit) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                IconButton(
                        onClick = onClick,
                        colors =
                                IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                )
                ) {
                        Icon(
                                imageVector = Icons.Outlined.SwapVert,
                                contentDescription = "Swap currencies",
                                tint = NeonGreen,
                                modifier = Modifier.size(28.dp)
                        )
                }
        }
}

@Composable
private fun NumericKeypad() {
        val keys =
                listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9"),
                        listOf(".", "0", "⌫")
                )

        Column {
                keys.forEach { row ->
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                        ) { row.forEach { key -> KeypadKey(key) } }
                        Spacer(modifier = Modifier.height(8.dp))
                }
        }
}

@Composable
private fun KeypadKey(label: String) {
        Card(
                modifier = Modifier.size(72.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
                Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                ) {
                        Text(
                                text = label,
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.onSurface
                        )
                }
        }
}

@Composable
private fun ExchangeCTA() {
        Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors =
                        ButtonDefaults.buttonColors(
                                containerColor = NeonGreen,
                                contentColor = MaterialTheme.colorScheme.background
                        )
        ) { Text(text = "Exchange Money", style = MaterialTheme.typography.bodyLarge) }
}
