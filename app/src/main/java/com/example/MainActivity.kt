package com.example

import android.content.res.Configuration
import android.content.Context
import androidx.compose.runtime.saveable.rememberSaveable
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
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
import com.example.ui.screens.MainCategoriesScreen
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

class MainActivity : FragmentActivity() {

    private val financeViewModel: FinanceViewModel by viewModels { FinanceViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val baseContext = LocalContext.current
            val preferences = remember(baseContext) {
                baseContext.getSharedPreferences("smart_cost_language", Context.MODE_PRIVATE)
            }
            var selectedLanguage by rememberSaveable {
                mutableStateOf(preferences.getString("language", "ar")?.takeIf { it in listOf("ar", "en", "fr") } ?: "ar")
            }
            val currentConfig = LocalConfiguration.current
            val localizedContext = remember(baseContext, currentConfig, selectedLanguage) {
                val config = Configuration(currentConfig).apply {
                    setLocale(Locale(selectedLanguage))
                    setLayoutDirection(Locale("ar"))
                }
                baseContext.createConfigurationContext(config)
            }

            SmartCostProTheme {
                CompositionLocalProvider(
                    LocalContext provides localizedContext,
                    LocalLayoutDirection provides LayoutDirection.Rtl
                ) {
                    SmartCostProApp(
                        viewModel = financeViewModel,
                        selectedLanguage = selectedLanguage,
                        onSelectLanguage = { language ->
                            if (language in listOf("ar", "en", "fr")) {
                                selectedLanguage = language
                                preferences.edit().putString("language", language).apply()
                            }
                        }
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
    selectedLanguage: String = "ar",
    onSelectLanguage: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Keep the Firebase session, but require biometric verification on a fresh app launch
    // when the user opted in to biometric unlock on this device.
    val appContext = LocalContext.current
    val authPrefs = remember {
        appContext.getSharedPreferences("smart_cost_auth_prefs", android.content.Context.MODE_PRIVATE)
    }
    fun biometricEnabledFor(user: com.google.firebase.auth.FirebaseUser?): Boolean =
        user != null &&
            authPrefs.getBoolean("biometric_enabled", false) &&
            authPrefs.getString("biometric_uid", null) == user.uid
    var isInWorkspace by remember {
        mutableStateOf(
            FirebaseAuth.getInstance().currentUser?.let { user ->
                !user.isAnonymous && user.isEmailVerified && !biometricEnabledFor(user)
            } == true ||
                (authPrefs.getBoolean("guest_mode", false) && FirebaseAuth.getInstance().currentUser == null)
        )
    }

    // Re-lock the workspace when the app returns from the background, if biometrics are enabled.
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        var hasGoneToBackground = false
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                hasGoneToBackground = true
            } else if (event == Lifecycle.Event.ON_START && hasGoneToBackground) {
                if (biometricEnabledFor(FirebaseAuth.getInstance().currentUser)) isInWorkspace = false
                hasGoneToBackground = false
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
    var currentScreen by remember { mutableStateOf(Screen.DASHBOARD) }
    var showingCostCategories by remember { mutableStateOf(false) }

    // Dialog trigger flags when navigating from quick actions
    var triggerAddExpense by remember { mutableStateOf(false) }
    var triggerAddRevenue by remember { mutableStateOf(false) }

    if (!isInWorkspace) {
        // شاشة الدخول المعتمدة
        LoginScreen(
            onLoginClick = {
                val user = FirebaseAuth.getInstance().currentUser
                val canEnter = user != null && !user.isAnonymous && user.isEmailVerified
                authPrefs.edit().putBoolean("guest_mode", false).apply()
                isInWorkspace = canEnter && !biometricEnabledFor(user)
                if (isInWorkspace) {
                    currentScreen = Screen.DASHBOARD
                    showingCostCategories = false
                }
            },
            onGuestClick = {
                FirebaseAuth.getInstance().signOut()
                authPrefs.edit().putBoolean("guest_mode", true).apply()
                isInWorkspace = true
                currentScreen = Screen.DASHBOARD
                showingCostCategories = false
            },
            selectedLanguage = selectedLanguage,
            onSelectLanguage = onSelectLanguage,
            modifier = modifier
        )
    } else {
        // Handle Android system Back button: if not on DASHBOARD, return to DASHBOARD
        BackHandler(enabled = showingCostCategories || currentScreen != Screen.DASHBOARD) {
            if (showingCostCategories) showingCostCategories = false
            else currentScreen = Screen.DASHBOARD
        }

        // When on DASHBOARD, HomeScreen provides its full dedicated Executive Header & Navigation
        if (showingCostCategories) {
            MainCategoriesScreen(
                viewModel = viewModel,
                onNavigateBack = { showingCostCategories = false; currentScreen = Screen.DASHBOARD },
                onCategoryClick = { id ->
                    // Financial submodules will be connected after their logic is audited.
                    // Existing implemented screens remain accessible where applicable.
                    when (id) {
                        "my_car" -> { showingCostCategories = false; currentScreen = Screen.VEHICLES }
                        "income_certificates" -> { showingCostCategories = false; currentScreen = Screen.REVENUES }
                        "reports" -> { showingCostCategories = false; currentScreen = Screen.REPORTS }
                        "personal_expenses", "home_expenses", "education" -> {
                            showingCostCategories = false; currentScreen = Screen.EXPENSES
                        }
                    }
                },
                onAddNewCategory = { /* Requires approved category persistence before activation. */ },
                onLogout = { FirebaseAuth.getInstance().signOut(); isInWorkspace = false; showingCostCategories = false }
            )
        } else if (currentScreen == Screen.DASHBOARD) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToGateway = { gatewayId ->
                    when (gatewayId) {
                        "income_accounts" -> {
                            currentScreen = Screen.REVENUES
                            triggerAddRevenue = false
                        }
                        "daily_expenses", "expenses" -> {
                            showingCostCategories = true
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
