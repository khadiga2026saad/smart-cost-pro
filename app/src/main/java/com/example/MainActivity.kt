package com.example

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.navigation.Screen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ExpensesScreen
import com.example.ui.screens.ExecutiveHeader
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.LiveFinancialTickerBar
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.RevenuesScreen
import com.example.ui.screens.SearchDialog
import com.example.ui.screens.VehiclesScreen
import com.example.ui.screens.VoiceSearchDialog
import com.example.ui.theme.BlackPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SmartCostProTheme
import com.example.ui.viewmodel.FinanceViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val financeViewModel: FinanceViewModel by viewModels { FinanceViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var isEnglish by remember { mutableStateOf(false) }

            val currentContext = LocalContext.current
            val currentConfig = LocalConfiguration.current

            val localizedContext = remember(isEnglish) {
                val locale = if (isEnglish) Locale("en") else Locale("ar")
                val config = Configuration(currentConfig).apply {
                    setLocale(locale)
                    setLayoutDirection(locale)
                }
                currentContext.createConfigurationContext(config)
            }

            val layoutDirection = if (isEnglish) LayoutDirection.Ltr else LayoutDirection.Rtl

            SmartCostProTheme {
                CompositionLocalProvider(
                    LocalContext provides localizedContext,
                    LocalLayoutDirection provides layoutDirection
                ) {
                    SmartCostProApp(
                        viewModel = financeViewModel,
                        isEnglish = isEnglish,
                        onToggleLanguage = { isEnglish = !isEnglish }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartCostProApp(
    viewModel: FinanceViewModel,
    isEnglish: Boolean = false,
    onToggleLanguage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Starts at the single LoginScreen
    var isInWorkspace by remember { mutableStateOf(FirebaseAuth.getInstance().currentUser != null) }
    var currentScreen by remember { mutableStateOf(Screen.DASHBOARD) }

    // Dialog trigger flags when navigating from quick actions
    var triggerAddExpense by remember { mutableStateOf(false) }
    var triggerAddRevenue by remember { mutableStateOf(false) }

    if (!isInWorkspace) {
        // شاشة الدخول المعتمدة
        LoginScreen(
            onLoginClick = {
                isInWorkspace = true
                currentScreen = Screen.DASHBOARD
            },
            onGuestClick = {
                isInWorkspace = true
                currentScreen = Screen.DASHBOARD
            },
            onLanguageClick = onToggleLanguage,
            modifier = modifier
        )
    } else {
        // Handle Android system Back button: if not on DASHBOARD, return to DASHBOARD
        BackHandler(enabled = currentScreen != Screen.DASHBOARD) {
            currentScreen = Screen.DASHBOARD
        }

        // When on DASHBOARD, HomeScreen provides its full dedicated Executive Header & Navigation
        if (currentScreen == Screen.DASHBOARD) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToGateway = { gatewayId ->
                    when (gatewayId) {
                        "income_accounts" -> {
                            currentScreen = Screen.REVENUES
                            triggerAddRevenue = false
                        }
                        "daily_expenses", "expenses" -> {
                            currentScreen = Screen.EXPENSES
                            triggerAddExpense = false
                        }
                        "vehicles" -> {
                            currentScreen = Screen.VEHICLES
                        }
                        "reports", "reports_analytics" -> {
                            currentScreen = Screen.REPORTS
                        }
                        "project_costing", "vendors_clients" -> {
                            currentScreen = Screen.EXPENSES
                        }
                        "budgets" -> {
                            currentScreen = Screen.REPORTS
                        }
                        "settings_backup" -> {
                            isInWorkspace = false
                        }
                    }
                },
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    isInWorkspace = false
                },
                modifier = modifier
            )
        } else {
            // Secondary Screen Scaffold with TopAppBar and standard NavigationBar
            Scaffold(
                modifier = modifier.fillMaxSize(),
                topBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        // Current live time state for header
                        var currentTimeString by remember { mutableStateOf("") }
                        var currentDateString by remember { mutableStateOf("") }
                        var showSearchDialog by remember { mutableStateOf(false) }
                        var searchQuery by remember { mutableStateOf("") }
                        var showVoiceDialog by remember { mutableStateOf(false) }

                        LaunchedEffect(Unit) {
                            val timeFormat = java.text.SimpleDateFormat("hh:mm a", java.util.Locale("ar"))
                            val dateFormat = java.text.SimpleDateFormat("d MMMM yyyy", java.util.Locale("ar"))
                            while (true) {
                                val now = java.util.Date()
                                currentTimeString = timeFormat.format(now)
                                currentDateString = dateFormat.format(now)
                                kotlinx.coroutines.delay(1000L)
                            }
                        }

                        ExecutiveHeader(
                            timeString = currentTimeString,
                            dateString = currentDateString,
                            onSearchClick = { showSearchDialog = true },
                            onVoiceClick = { showVoiceDialog = true },
                            onSettingsClick = { },
                            onLogout = {
                                FirebaseAuth.getInstance().signOut()
                                isInWorkspace = false
                            },
                            onHomeClick = { currentScreen = Screen.DASHBOARD }
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        LiveFinancialTickerBar()

                        if (showSearchDialog) {
                            SearchDialog(
                                query = searchQuery,
                                onQueryChange = { searchQuery = it },
                                onDismiss = { showSearchDialog = false }
                            )
                        }

                        if (showVoiceDialog) {
                            VoiceSearchDialog(onDismiss = { showVoiceDialog = false })
                        }
                    }
                },
                bottomBar = {
                    NavigationBar(
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                            .testTag("bottom_nav_bar"),
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = BlackPrimary
                    ) {
                        Screen.entries.forEach { screen ->
                            val isSelected = currentScreen == screen
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = {
                                    currentScreen = screen
                                    triggerAddExpense = false
                                    triggerAddRevenue = false
                                },
                                icon = {
                                    Icon(
                                        imageVector = screen.icon,
                                        contentDescription = screen.titleAr
                                    )
                                },
                                label = {
                                    Text(
                                        text = screen.titleAr,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = BlackPrimary,
                                    selectedTextColor = BlackPrimary,
                                    indicatorColor = GoldPrimary,
                                    unselectedIconColor = Color.Gray,
                                    unselectedTextColor = Color.Gray
                                ),
                                modifier = Modifier.testTag("nav_item_${screen.route}")
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    Crossfade(targetState = currentScreen, label = "screen_crossfade") { screen ->
                        when (screen) {
                            Screen.DASHBOARD -> {
                                // Handled in top branch
                            }
                            Screen.EXPENSES -> ExpensesScreen(
                                viewModel = viewModel,
                                showAddDialogInitially = triggerAddExpense
                            )
                            Screen.VEHICLES -> VehiclesScreen(
                                viewModel = viewModel
                            )
                            Screen.REVENUES -> RevenuesScreen(
                                viewModel = viewModel,
                                showAddRevenueInitially = triggerAddRevenue
                            )
                            Screen.REPORTS -> ReportsScreen(
                                viewModel = viewModel
                            )
                        }
                    }
                }
            }
        }
    }
}
