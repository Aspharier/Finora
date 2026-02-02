package com.aspharier.finora.ui.screens.home

enum class TransactionFilter {
    ALL, SENT, REQUEST, TRANSFER, REMIT
}

data class SpendingCategoryUiModel(
    val name: String,
    val amount: Double,
    val percentage: Int
)

data class HomeState(
    // User info
    val userName: String = "Sajibur",
    
    // Balance
    val totalBalance: Double = 24600.00,
    val currency: String = "$",
    val percentageChange: Double = 22.7,
    val isPositiveChange: Boolean = true,
    
    // Budget
    val leftToSpend: Double = 738.00,
    val monthlyBudget: Double = 22550.00,
    val monthlySpent: Double = 21812.00,
    
    // Recent Transaction Avatars
    val recentContacts: List<ContactAvatar> = sampleContacts(),
    
    // Transaction Filters
    val selectedFilter: TransactionFilter = TransactionFilter.ALL,
    
    // Transactions
    val recentTransactions: List<TransactionUiModel> = sampleTransactions(),
    
    // Spending categories (keeping for compatibility)
    val categories: List<SpendingCategoryUiModel> = sampleCategories()
)

private fun sampleContacts() = listOf(
    ContactAvatar("1", "Noah"),
    ContactAvatar("2", "Mason"),
    ContactAvatar("3", "Mason"),
    ContactAvatar("4", "Lucas"),
    ContactAvatar("5", "Ethan"),
    ContactAvatar("6", "Oliver")
)

private fun sampleCategories() = listOf(
    SpendingCategoryUiModel("Shopping", 6200.0, 34),
    SpendingCategoryUiModel("Food", 4100.0, 22),
    SpendingCategoryUiModel("Transport", 2300.0, 13),
    SpendingCategoryUiModel("Subscriptions", 1200.0, 7)
)

private fun sampleTransactions() = listOf(
    TransactionUiModel(
        id = "1",
        merchant = "Henry James",
        category = "Receive",
        amount = 367.00,
        isCredit = true,
        timestamp = "10:30 AM"
    ),
    TransactionUiModel(
        id = "2",
        merchant = "Sarah Wilson",
        category = "Transfer",
        amount = 908.00,
        isCredit = false,
        timestamp = "09:15 AM"
    ),
    TransactionUiModel(
        id = "3",
        merchant = "Mike Johnson",
        category = "Receive",
        amount = 450.00,
        isCredit = true,
        timestamp = "Yesterday"
    )
)