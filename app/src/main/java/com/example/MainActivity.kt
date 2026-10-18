package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.navigation.Screen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.ExpensesScreen
import com.example.ui.screens.ReportsScreen
import com.example.ui.screens.RevenuesScreen
import com.example.ui.screens.VehiclesScreen
import com.example.ui.theme.BlackPrimary
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SmartCostProTheme
import com.example.ui.viewmodel.FinanceViewModel

class MainActivity : ComponentActivity() {

    private val financeViewModel: FinanceViewModel by viewModels { FinanceViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SmartCostProTheme {
                // Ensure complete Right-to-Left (RTL) Arabic layout
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    SmartCostProApp(viewModel = financeViewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartCostProApp(
    viewModel: FinanceViewModel,
    modifier: Modifier = Modifier
) {
    var isInWorkspace by remember { mutableStateOf(true) } // Initially in workspace for convenience
    var currentScreen by remember { mutableStateOf(Screen.DASHBOARD) }

    // Dialog trigger flags when navigating from quick actions
    var triggerAddExpense by remember { mutableStateOf(false) }
    var triggerAddRevenue by remember { mutableStateOf(false) }

    if (!isInWorkspace) {
        AuthScreen(
            onEnterWorkspace = { isInWorkspace = true },
            modifier = modifier
        )
    } else {
        // Handle Android system Back button: if not on DASHBOARD, return to DASHBOARD
        BackHandler(enabled = currentScreen != Screen.DASHBOARD) {
            currentScreen = Screen.DASHBOARD
        }

        Scaffold(
            modifier = modifier.fillMaxSize(),
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "SMART COST PRO",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    color = BlackPrimary
                                )
                            )
                        }
                    },
                    navigationIcon = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "محلي",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldDark,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { isInWorkspace = false },
                            modifier = Modifier.testTag("btn_logout")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "قفل مساحة العمل",
                                tint = Color.Gray
                            )
                        }
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
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
                        Screen.DASHBOARD -> DashboardScreen(
                            viewModel = viewModel,
                            onNavigateToExpenses = { currentScreen = Screen.EXPENSES },
                            onNavigateToRevenues = { currentScreen = Screen.REVENUES },
                            onNavigateToVehicles = { currentScreen = Screen.VEHICLES },
                            onOpenAddExpense = {
                                triggerAddExpense = true
                                currentScreen = Screen.EXPENSES
                            },
                            onOpenAddRevenue = {
                                triggerAddRevenue = true
                                currentScreen = Screen.REVENUES
                            },
                            onOpenAddVehicleLog = {
                                currentScreen = Screen.VEHICLES
                            }
                        )
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
