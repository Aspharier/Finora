package com.aspharier.finora.ui.screens.exchange

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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SwapVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aspharier.finora.ui.theme.NeonGreen
import com.aspharier.finora.ui.theme.NumpadBackgroundDark
import com.aspharier.finora.ui.theme.NumpadBackgroundLight
import com.aspharier.finora.ui.theme.NumpadBorderDark
import com.aspharier.finora.ui.theme.NumpadBorderLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeScreen(viewModel: ExchangeViewModel = hiltViewModel(), isDarkTheme: Boolean = true) {
        val state by viewModel.state.collectAsState()

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        var showSheet by remember { mutableStateOf(false) }
        var selectingFrom by remember { mutableStateOf(true) }

        LaunchedEffect(Unit) { viewModel.refreshRatesSafely() }

        Column(modifier = Modifier.fillMaxSize().padding(start = 16.dp, end = 16.dp, top = 24.dp)) {
                Text(
                        text = "Currency Exchange",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedContent(
                        targetState = state.fromCurrency,
                        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                        label = "fromCardAnimation"
                ) { currency ->
                        CurrencyInputCard(
                                label = "From",
                                currency = currency,
                                amount = state.fromAmount,
                                onCurrencyClick = {
                                        selectingFrom = true
                                        showSheet = true
                                }
                        )
                }

                Spacer(modifier = Modifier.height(12.dp))

                SwapButton { viewModel.onSwap() }

                Spacer(modifier = Modifier.height(12.dp))

                AnimatedContent(
                        targetState = state.toCurrency,
                        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(300)) },
                        label = "toCardAnimation"
                ) { currency ->
                        CurrencyInputCard(
                                label = "To",
                                currency = currency,
                                amount = state.toAmount,
                                isReadOnly = true,
                                onCurrencyClick = {
                                        selectingFrom = false
                                        showSheet = true
                                }
                        )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                        text = state.rateInfo,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )

                ExchangeStatusIndicator(state)

                Text(
                        text = state.lastUpdated,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                NumericKeypad(onKeyPress = viewModel::onKeyPress, isDarkTheme = isDarkTheme)
        }

        if (showSheet) {
                ModalBottomSheet(
                        onDismissRequest = { showSheet = false },
                        sheetState = sheetState,
                        containerColor = MaterialTheme.colorScheme.surface
                ) {
                        CurrencySelectorSheet(
                                currencies = SupportedCurrencies,
                                onSelect = {
                                        if (selectingFrom) {
                                                viewModel.onFromCurrencySelected(it)
                                        } else {
                                                viewModel.onToCurrencySelected(it)
                                        }
                                        showSheet = false
                                }
                        )
                }
        }
}

@Composable
private fun ExchangeStatusIndicator(state: ExchangeState) {
        val color =
                when (state.status) {
                        ExchangeStatus.LOADING -> MaterialTheme.colorScheme.primary
                        ExchangeStatus.SUCCESS -> MaterialTheme.colorScheme.primary
                        ExchangeStatus.ERROR -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                }

        val text =
                when (state.status) {
                        ExchangeStatus.LOADING -> "Updating rates…"
                        ExchangeStatus.SUCCESS -> state.lastUpdated
                        ExchangeStatus.ERROR -> state.errorMessage ?: "Rate unavailable"
                        else -> state.lastUpdated
                }

        Row(verticalAlignment = Alignment.CenterVertically) {
                if (state.status == ExchangeStatus.LOADING) {
                        CircularProgressIndicator(
                                modifier = Modifier.size(14.dp),
                                strokeWidth = 2.dp,
                                color = color
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                }

                Text(text = text, style = MaterialTheme.typography.labelMedium, color = color)
        }
}

@Composable
private fun CurrencySelectorSheet(
        currencies: List<CurrencyUiModel>,
        onSelect: (CurrencyUiModel) -> Unit
) {
        var query by remember { mutableStateOf("") }

        val filtered =
                currencies.filter { it.code.contains(query, true) || it.name.contains(query, true) }

        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {

                // Search bar
                OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = { Text("Search currency") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn { items(filtered) { currency -> CurrencyRow(currency, onSelect) } }
        }
}

@Composable
private fun CurrencyRow(currency: CurrencyUiModel, onSelect: (CurrencyUiModel) -> Unit) {
        Card(
                modifier =
                        Modifier.fillMaxWidth().padding(vertical = 6.dp).clickable {
                                onSelect(currency)
                        },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
                Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        Text(text = currency.flag, style = MaterialTheme.typography.headlineMedium)

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                                Text(
                                        text = currency.code,
                                        style = MaterialTheme.typography.bodyLarge
                                )
                                Text(
                                        text = currency.name,
                                        style = MaterialTheme.typography.labelMedium,
                                        color =
                                                MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.6f
                                                )
                                )
                        }
                }
        }
}

@Composable
private fun CurrencyInputCard(
        label: String,
        currency: CurrencyUiModel,
        amount: String,
        isReadOnly: Boolean = false,
        onCurrencyClick: () -> Unit
) {
        Row(
                modifier = Modifier.fillMaxWidth().clickable { onCurrencyClick() },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
                Card(
                        shape = RoundedCornerShape(20.dp),
                        colors =
                                CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                ),
                        elevation = CardDefaults.cardElevation(8.dp)
                ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelMedium,
                                        color =
                                                MaterialTheme.colorScheme.onSurface.copy(
                                                        alpha = 0.6f
                                                )
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
                                                        if (isReadOnly)
                                                                MaterialTheme.colorScheme.onSurface
                                                        else MaterialTheme.colorScheme.primary
                                        )
                                }
                        }
                }
        }
}

@Composable
private fun SwapButton(onClick: () -> Unit) {
        var rotated by remember { mutableStateOf(false) }

        val rotation by
                animateFloatAsState(
                        targetValue = if (rotated) 180f else 0f,
                        animationSpec =
                                spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                ),
                        label = "swapRotation"
                )
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                IconButton(
                        onClick = {
                                rotated = !rotated
                                onClick()
                        },
                        colors =
                                IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                )
                ) {
                        Icon(
                                imageVector = Icons.Outlined.SwapVert,
                                contentDescription = "Swap currencies",
                                tint = NeonGreen,
                                modifier = Modifier.graphicsLayer { rotationZ = rotation }
                        )
                }
        }
}

@Composable
private fun NumericKeypad(onKeyPress: (String) -> Unit, isDarkTheme: Boolean) {
        val keys =
                listOf(
                        listOf("1", "2", "3"),
                        listOf("4", "5", "6"),
                        listOf("7", "8", "9"),
                        listOf(".", "0", "⌫")
                )

        Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
                keys.forEach { row ->
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                                row.forEach { key ->
                                        KeypadKey(
                                                label = key,
                                                onClick = { onKeyPress(key) },
                                                modifier = Modifier.weight(1f),
                                                isDarkTheme = isDarkTheme
                                        )
                                }
                        }
                }
        }
}

@Composable
private fun KeypadKey(
        label: String,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        isDarkTheme: Boolean
) {
        val interactionSource = remember { MutableInteractionSource() }
        val pressed = remember { mutableStateOf(false) }

        // Theme-aware colors for better visibility
        val backgroundColor = if (isDarkTheme) NumpadBackgroundDark else NumpadBackgroundLight
        val borderColor = if (isDarkTheme) NumpadBorderDark else NumpadBorderLight
        val textColor = MaterialTheme.colorScheme.onBackground

        LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect { interaction ->
                        when (interaction) {
                                is PressInteraction.Press -> pressed.value = true
                                is PressInteraction.Release, is PressInteraction.Cancel ->
                                        pressed.value = false
                        }
                }
        }

        val scale by
                animateFloatAsState(
                        targetValue = if (pressed.value) 0.92f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium),
                        label = "keyScale"
                )

        Box(
                modifier =
                        modifier.height(56.dp)
                                .graphicsLayer {
                                        scaleX = scale
                                        scaleY = scale
                                }
                                .background(
                                        color = backgroundColor,
                                        shape = RoundedCornerShape(16.dp)
                                )
                                .border(
                                        width = 1.dp,
                                        color = borderColor,
                                        shape = RoundedCornerShape(16.dp)
                                )
                                .clickable(
                                        interactionSource = interactionSource,
                                        indication = ripple(bounded = true),
                                        onClick = onClick
                                ),
                contentAlignment = Alignment.Center
        ) { Text(text = label, style = MaterialTheme.typography.headlineMedium, color = textColor) }
}

@Composable
private fun ExchangeCTA(enabled: Boolean) {
        var pressed by remember { mutableStateOf(false) }
        val scale by
                animateFloatAsState(
                        targetValue = if (pressed) 0.96f else 1f,
                        animationSpec = spring(stiffness = Spring.StiffnessLow),
                        label = "ctaScale"
                )

        val interactionSource = remember { MutableInteractionSource() }

        LaunchedEffect(interactionSource) {
                interactionSource.interactions.collect { interaction ->
                        when (interaction) {
                                is PressInteraction.Press -> pressed = true
                                is PressInteraction.Release, is PressInteraction.Cancel ->
                                        pressed = false
                        }
                }
        }

        Button(
                onClick = {},
                enabled = enabled,
                modifier =
                        Modifier.fillMaxWidth().height(56.dp).graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                        },
                shape = RoundedCornerShape(16.dp),
                colors =
                        ButtonDefaults.buttonColors(
                                containerColor = NeonGreen,
                                disabledContainerColor = NeonGreen.copy(alpha = 0.4f),
                                contentColor = MaterialTheme.colorScheme.background
                        ),
                interactionSource = interactionSource
        ) { Text(text = "Exchange Money", style = MaterialTheme.typography.bodyLarge) }
}
