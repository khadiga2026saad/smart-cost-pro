package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Store
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.core.MoneyUtils
import com.example.ui.theme.BackgroundWhite
import com.example.ui.theme.BlackPrimary
import com.example.ui.theme.BlackSecondary
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CoralRed
import com.example.ui.theme.DeepSlate
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.FinancialGreenContainer
import com.example.ui.theme.FinancialRedContainer
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldGradientEnd
import com.example.ui.theme.GoldGradientStart
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SlateMedium
import com.example.ui.theme.SmartCostProTheme
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCrispWhite
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import com.example.ui.viewmodel.FinanceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * بيانات بوابات الوصول السريع الثمانية
 */
data class GatewayActionItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val badge: String? = null,
    val isPrimary: Boolean = false
)

/**
 * الشاشة الرئيسية التنفيذية المتكاملة لـ SMART COST PRO
 * - Top App Bar تنفيذي (User Profile, Live Clock, Search, Voice, Settings)
 * - شريط التنبيهات والأخبار المالي المتحرك (basicMarquee)
 * - بطاقات المؤشرات المالية التنفيذية والـ KPI
 * - شبكة بوابات الوصول السريع الثمانية (8-Gateway Action Grid)
 * - زر العمليات السريعة FAB وشريط التنقل السفلي الفاخر (Bottom Navigation)
 */
@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: FinanceViewModel? = null,
    onNavigateToGateway: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Current live time & date state
    var currentTimeString by remember { mutableStateOf("") }
    var currentDateString by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val timeFormat = SimpleDateFormat("hh:mm a", Locale("ar"))
        val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ar"))
        while (true) {
            val now = Date()
            currentTimeString = timeFormat.format(now)
            currentDateString = dateFormat.format(now)
            delay(1000L)
        }
    }

    // Interactive Dialogs & Sheets states
    var showSearchDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var showVoiceDialog by remember { mutableStateOf(false) }
    var showOverflowMenu by remember { mutableStateOf(false) }
    var showQuickCostSheet by remember { mutableStateOf(false) }
    var showAdvisorDialog by remember { mutableStateOf(false) }
    var selectedBottomNavIndex by remember { mutableIntStateOf(0) }
    val gridState = rememberLazyGridState()
    val coroutineScope = rememberCoroutineScope()

    // Financial calculations from ViewModel or executive defaults
    val summary = viewModel?.currentMonthSummary?.collectAsStateWithLifecycle()?.value ?: com.example.domain.model.FinancialSummary()
    val formattedBalance = if (summary.currentLiquidBalancePiastres > 0L) {
        MoneyUtils.formatPiastres(summary.currentLiquidBalancePiastres)
    } else {
        "485,250.00 ج.م"
    }
    val formattedExpenses = if (summary.totalExpensesPiastres > 0L) {
        MoneyUtils.formatPiastres(summary.totalExpensesPiastres)
    } else {
        "135,800.00 ج.م"
    }
    val formattedRevenue = if (summary.totalRevenuesPiastres > 0L) {
        MoneyUtils.formatPiastres(summary.totalRevenuesPiastres)
    } else {
        "621,050.00 ج.م"
    }

    // 8-Gateway Action Grid Items
    val gatewayItems = remember {
        listOf(
            GatewayActionItem(
                id = "income_accounts",
                title = "الدخل والحسابات",
                subtitle = "الودائع والتدفقات النقدية",
                icon = Icons.Default.AttachMoney,
                badge = "+14%",
                isPrimary = true
            ),
            GatewayActionItem(
                id = "daily_expenses",
                title = "المصروفات اليومية",
                subtitle = "تسجيل الفواتير اللحظية",
                icon = Icons.Default.CreditCard,
                badge = "مباشر"
            ),
            GatewayActionItem(
                id = "budgets",
                title = "الموازنة التقديرية",
                subtitle = "متابعة الأسقف والانحراف",
                icon = Icons.Default.PieChart,
                badge = "65% استهلاك"
            ),
            GatewayActionItem(
                id = "project_costing",
                title = "تكاليف المشاريع",
                subtitle = "العقود والمراحل والمشتريات",
                icon = Icons.Default.Store
            ),
            GatewayActionItem(
                id = "reports_analytics",
                title = "التقارير والـ PDF",
                subtitle = "تصدير القوائم التنفيذية",
                icon = Icons.Default.Description,
                badge = "جاهز"
            ),
            GatewayActionItem(
                id = "vendors_clients",
                title = "العملاء والموردين",
                subtitle = "الذمم الدائنة والمدينة",
                icon = Icons.Default.Group
            ),
            GatewayActionItem(
                id = "ai_advisor",
                title = "المستشار المالي الذكي",
                subtitle = "توصيات خفض الهدر المالي",
                icon = Icons.Default.AutoAwesome,
                badge = "نشط",
                isPrimary = true
            ),
            GatewayActionItem(
                id = "settings_backup",
                title = "إعدادات النظام",
                subtitle = "النسخ الاحتياطي والصلاحيات",
                icon = Icons.Default.Settings
            )
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundWhite,
        floatingActionButton = {
            // Gold Gradient Floating Action Button for "تسجيل تكلفة سريعة"
            Box(
                modifier = Modifier
                    .shadow(12.dp, RoundedCornerShape(20.dp), spotColor = GoldDark)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(GoldGradientStart, GoldGradientEnd)
                        )
                    )
                    .clickable { showQuickCostSheet = true }
                    .padding(horizontal = 20.dp, vertical = 14.dp)
                    .testTag("fab_quick_cost"),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "تسجيل تكلفة سريعة",
                        tint = DeepSlate,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "تسجيل تكلفة سريعة",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepSlate
                        )
                    )
                }
            }
        },
        bottomBar = {
            // Executive Bottom Navigation Bar
            NavigationBar(
                containerColor = SurfaceCrispWhite,
                contentColor = DeepSlate,
                tonalElevation = 8.dp,
                modifier = Modifier
                    .border(BorderStroke(1.dp, BorderLight))
                    .testTag("home_bottom_nav")
            ) {
                val navItems = listOf(
                    Triple("الرئيسية", Icons.Default.Home, 0),
                    Triple("العمليات", Icons.Default.ReceiptLong, 1),
                    Triple("التحليلات", Icons.Default.Analytics, 2),
                    Triple("المزيد", Icons.Default.MoreHoriz, 3)
                )

                navItems.forEach { (label, icon, index) ->
                    val isSelected = selectedBottomNavIndex == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            selectedBottomNavIndex = index
                            when (index) {
                                1 -> onNavigateToGateway("daily_expenses")
                                2 -> onNavigateToGateway("reports_analytics")
                                3 -> onNavigateToGateway("settings_backup")
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = icon,
                                contentDescription = label,
                                tint = if (isSelected) DeepSlate else TextSecondaryDark
                            )
                        },
                        label = {
                            Text(
                                text = label,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) DeepSlate else TextSecondaryDark,
                                fontSize = 12.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = GoldPrimary,
                            selectedIconColor = DeepSlate,
                            selectedTextColor = DeepSlate,
                            unselectedIconColor = TextSecondaryDark,
                            unselectedTextColor = TextSecondaryDark
                        ),
                        modifier = Modifier.testTag("nav_item_$index")
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundWhite)
        ) {
            // الشريط العلوي (ثابت لا يتحرك عند التمرير)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                ExecutiveHeader(
                    timeString = currentTimeString,
                    dateString = currentDateString,
                    onSearchClick = { showSearchDialog = true },
                    onVoiceClick = { showVoiceDialog = true },
                    onSettingsClick = { showOverflowMenu = true },
                    onLogout = onLogout,
                    onHomeClick = {
                        coroutineScope.launch {
                            gridState.animateScrollToItem(0)
                        }
                    }
                )
            }


            // شريط التنبيهات والأخبار المالي المتحرك (ثابت لا يتحرك عند التمرير)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                LiveFinancialTickerBar()
            }

            Spacer(modifier = Modifier.height(4.dp))

            // بقية محتويات الشاشة القابلة للتمرير (البطاقات والشبكة)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .testTag("home_lazy_grid"),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // 3. Financial Summary / KPI Cards (بطاقات المؤشرات التنفيذية)
                item(span = { GridItemSpan(2) }) {
                    FinancialSummaryKpiSection(
                        operationalBalance = formattedBalance,
                        totalIncome = formattedRevenue,
                        totalExpenses = formattedExpenses
                    )
                }

                // 4. Section Title for 8-Gateway Grid
                item(span = { GridItemSpan(2) }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(4.dp, 18.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(GoldPrimary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "بوابات الوصول السريع (8-Gateways)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = DeepSlate
                                )
                            )
                        }

                        Text(
                            text = "نظام التكلفة الشامل",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextSecondaryDark,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }

                // 5. 8-Gateway Action Grid items
                items(gatewayItems) { item ->
                    GatewayCard(
                        item = item,
                        onClick = {
                            if (item.id == "ai_advisor") {
                                showAdvisorDialog = true
                            } else {
                                onNavigateToGateway(item.id)
                            }
                        }
                    )
                }
            }
        }
    }

    // Search Dialog
    if (showSearchDialog) {
        SearchDialog(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            onDismiss = { showSearchDialog = false }
        )
    }

    // Voice Command Dialog Simulation
    if (showVoiceDialog) {
        VoiceSearchDialog(onDismiss = { showVoiceDialog = false })
    }

    // AI Advisor Recommendations Dialog
    if (showAdvisorDialog) {
        AiAdvisorDialog(onDismiss = { showAdvisorDialog = false })
    }

    // Quick Cost Registration Bottom Sheet
    if (showQuickCostSheet) {
        QuickCostBottomSheet(
            onDismiss = { showQuickCostSheet = false },
            onCostSaved = { amount, category, desc ->
                // Save through ViewModel or notify user
                viewModel?.addExpense(
                    amountEgp = amount,
                    department = category,
                    subcategory = "تسجيل سريع",
                    title = desc.ifEmpty { "مصروف سريع من الشاشة الرئيسية" },
                    notes = "سُجل من بوابة FAB السريعة"
                )
                showQuickCostSheet = false
            }
        )
    }
}

/**
 * 1. Top App Bar (Executive Header)
 * مرتب بالتوالي من اليسار إلى اليمين:
 * 1. صورة المستخدم بأيقونة، وتحته اسمه ومهنته
 * 2. نفس الأيقونات السابقة (الساعة الحية، أيقونة البحث، أيقونة البحث الصوتي)
 * 3. علامة الترس (الإعدادات)
 * 4. علامة الهوم (الرئيسية)
 */
@Composable
fun ExecutiveHeader(
    timeString: String,
    dateString: String,
    userName: String = "م. ممدوح سعد",
    userRole: String = "مهندس ومستشار تنفيذي",
    onSearchClick: () -> Unit,
    onVoiceClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onLogout: () -> Unit,
    onHomeClick: () -> Unit = {}
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("executive_header_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceCrispWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, BorderLight)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            // ترتيب صارم من اليسار إلى اليمين (LTR Sequence)
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // 1. أقصى اليسار: صورة المستخدم بأيقونة، وتحته اسمه ومهنته
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .testTag("header_user_profile")
                            .widthIn(max = 120.dp)
                    ) {
                        // صورة المستخدم بأيقونة (حجم 46.dp / 44.dp)
                        Box(modifier = Modifier.size(46.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        brush = Brush.linearGradient(
                                            listOf(DeepSlate, SlateMedium)
                                        )
                                    )
                                    .border(1.5.dp, GoldPrimary, RoundedCornerShape(14.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "صورة المستخدم",
                                    tint = GoldLight,
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            // شارة الاتصال والحالة النشطة
                            Box(
                                modifier = Modifier
                                    .size(12.dp)
                                    .align(Alignment.BottomEnd)
                                    .background(EmeraldGreen, CircleShape)
                                    .border(2.dp, SurfaceCrispWhite, CircleShape)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // تحته اسمه
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepSlate,
                                fontSize = 12.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // ومهنته
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 1.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = userRole,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldDark,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 9.5.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // 2. بعد ذلك نفس الأيقونات + علامة الترس + شعار البرنامج (بنفس حجم ومستوى أيقونة المستخدم)
                    Row(
                        modifier = Modifier.height(46.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        // شارة الساعة والتاريخ الحية
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .background(GoldLight.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                .padding(horizontal = 5.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = if (timeString.isNotEmpty()) timeString else "12:00 م",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepSlate,
                                    fontSize = 10.5.sp
                                )
                            )
                            Text(
                                text = if (dateString.isNotEmpty()) dateString else "اليوم",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextSecondaryDark,
                                    fontSize = 8.sp
                                )
                            )
                        }

                        // أيقونة البحث
                        IconButton(
                            onClick = onSearchClick,
                            modifier = Modifier
                                .size(34.dp)
                                .background(SurfaceCard, CircleShape)
                                .border(1.dp, BorderLight, CircleShape)
                                .testTag("btn_header_search")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "بحث",
                                tint = DeepSlate,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        // أيقونة البحث الصوتي
                        IconButton(
                            onClick = onVoiceClick,
                            modifier = Modifier
                                .size(34.dp)
                                .background(GoldLight, CircleShape)
                                .border(1.dp, GoldPrimary.copy(alpha = 0.4f), CircleShape)
                                .testTag("btn_header_voice")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "بحث صوتي",
                                tint = GoldDark,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        // علامة الترس (Settings)
                        Box {
                            IconButton(
                                onClick = { expandedMenu = true },
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(SurfaceCard, CircleShape)
                                    .border(1.dp, BorderLight, CircleShape)
                                    .testTag("btn_header_settings")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "الإعدادات",
                                    tint = DeepSlate,
                                    modifier = Modifier.size(17.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = expandedMenu,
                                onDismissRequest = { expandedMenu = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("إعدادات النظام") },
                                    onClick = {
                                        expandedMenu = false
                                        onSettingsClick()
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Settings, contentDescription = null, tint = DeepSlate)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("قفل مساحة العمل") },
                                    onClick = {
                                        expandedMenu = false
                                        onLogout()
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Shield, contentDescription = null, tint = GoldDark)
                                    }
                                )
                            }
                        }

                        // شعار البرنامج (أيقونة الرئيسية) بحجم أكبر وأكثر بروزاً في الشريط العلوى
                        Box(modifier = Modifier.size(54.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(
                                        brush = Brush.linearGradient(
                                            listOf(SurfaceCrispWhite, GoldLight.copy(alpha = 0.45f))
                                        )
                                    )
                                    .border(2.dp, GoldPrimary, RoundedCornerShape(16.dp))
                                    .clickable(onClick = onHomeClick)
                                    .testTag("btn_header_home"),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_smart_cost_logo),
                                    contentDescription = "شعار البرنامج - الرئيسية",
                                    modifier = Modifier
                                        .size(42.dp)
                                        .padding(1.dp),
                                    contentScale = ContentScale.Fit
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 2. Live Financial News & Ticker Bar (شريط التنبيهات والأخبار المالي المتحرك)
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LiveFinancialTickerBar() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ticker_bar_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = GoldLight.copy(alpha = 0.45f)
        ),
        border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Brand Logo Emblem (شعار البرنامج في رأس الشريط المتحرك)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, GoldPrimary, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_smart_cost_logo),
                    contentDescription = "شعار سمارت كوست برو",
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Brand Label Tag
            Text(
                text = "نبض التكلفة:",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepSlate,
                    fontSize = 11.sp
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Scrolling Ticker with basicMarquee
            Text(
                text = "💡 تنبيه الميزانية: تم استهلاك 65% من مخصصات التشغيل لشهر مارس   •   📈 مؤشر التوفير: ارتفاع معدل خفض التكاليف بنسبة +8.4%   •   🔔 فاتورة تشغيل مستحقة خلال 48 ساعة   •   💰 التدفق النقدي: تم استلام إيرادات جديدة بقيمة 45,000 ج.م",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = DeepSlate,
                    fontWeight = FontWeight.SemiBold
                ),
                maxLines = 1,
                modifier = Modifier
                    .weight(1f)
                    .basicMarquee(
                        iterations = Int.MAX_VALUE,
                        velocity = 45.dp
                    )
            )
        }
    }
}

/**
 * 3. Financial Summary / KPI Cards (بطاقات المؤشرات التنفيذية)
 */
@Composable
fun FinancialSummaryKpiSection(
    operationalBalance: String,
    totalIncome: String,
    totalExpenses: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Main Wallet Balance Card (Deep luxury dark slate with gold accents)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("kpi_main_balance_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DeepSlate),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Background subtle gold decorative glow
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(GoldPrimary.copy(alpha = 0.25f), Color.Transparent)
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountBalance,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "إجمالي الرصيد التشغيلي والسيولة",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = GoldLight,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }

                        // Growth Badge (+12.5%)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(EmeraldGreen.copy(alpha = 0.2f))
                                .border(1.dp, EmeraldGreen.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "+12.5% نمو",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = EmeraldGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = operationalBalance,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 32.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    // Sub-summary breakdown (Income vs Expenses split)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(SlateMedium.copy(alpha = 0.7f))
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Quick Income Metric
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(EmeraldGreen.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDownward,
                                    contentDescription = null,
                                    tint = EmeraldGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "إجمالي الإيرادات",
                                    style = MaterialTheme.typography.labelSmall.copy(color = GoldLight)
                                )
                                Text(
                                    text = totalIncome,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            }
                        }

                        // Divider
                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(36.dp)
                                .background(SlateLight)
                        )

                        // Quick Expenses Metric
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(CoralRed.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = CoralRed,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "إجمالي التكاليف",
                                    style = MaterialTheme.typography.labelSmall.copy(color = GoldLight)
                                )
                                Text(
                                    text = totalExpenses,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 4. 8-Gateway Action Grid Item Card
 */
@Composable
fun GatewayCard(
    item: GatewayActionItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(138.dp)
            .clickable(onClick = onClick)
            .testTag("gateway_card_${item.id}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isPrimary) SurfaceCrispWhite else SurfaceCrispWhite
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (item.isPrimary) 3.dp else 1.5.dp),
        border = BorderStroke(
            1.dp,
            if (item.isPrimary) GoldPrimary.copy(alpha = 0.5f) else BorderLight
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Gold-tinted icon container
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (item.isPrimary) GoldLight else SurfaceCard
                        )
                        .border(
                            1.dp,
                            if (item.isPrimary) GoldPrimary.copy(alpha = 0.6f) else Color.Transparent,
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.title,
                        tint = if (item.isPrimary) GoldDark else DeepSlate,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Optional Badge
                if (item.badge != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (item.isPrimary) GoldLight else SurfaceCard
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.badge,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (item.isPrimary) GoldDark else DeepSlate,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp
                            )
                        )
                    }
                }
            }

            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepSlate,
                        fontSize = 14.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondaryDark,
                        fontSize = 11.sp
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

/**
 * Search Dialog
 */
@Composable
fun SearchDialog(
    query: String,
    onQueryChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "البحث المالي الشامل",
                fontWeight = FontWeight.Bold,
                color = DeepSlate
            )
        },
        text = {
            Column {
                Text(
                    "ابحث عن أي فاتورة، مشروع، عميل، أو تدفق نقدي...",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChange,
                    placeholder = { Text("اكتب كلمة البحث...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = GoldDark)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = DeepSlate)
            ) {
                Text("إغلاق")
            }
        }
    )
}

/**
 * Voice Command Simulation Dialog
 */
@Composable
fun VoiceSearchDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(GoldLight, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = null,
                    tint = GoldDark,
                    modifier = Modifier.size(34.dp)
                )
            }
        },
        title = {
            Text(
                "المساعد الصوتي الذكي",
                fontWeight = FontWeight.Bold,
                color = DeepSlate,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "جارٍ الاستماع لأمرك المالي...",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "أمثلة: «سجل وقود 500 جنيه»، «كم متبقي في ميزانية هذا الشهر؟»",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondaryDark,
                        fontSize = 11.sp
                    ),
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
            ) {
                Text("تم", color = DeepSlate, fontWeight = FontWeight.Bold)
            }
        }
    )
}

/**
 * AI Financial Advisor Dialog
 */
@Composable
fun AiAdvisorDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = GoldDark,
                modifier = Modifier.size(36.dp)
            )
        },
        title = {
            Text(
                "توصيات المستشار المالي الذكي",
                fontWeight = FontWeight.Bold,
                color = DeepSlate,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "• 💡 مراجعة نفقات الوقود: تم تسجيل زيادة بنسبة 11% هذا الشهر عن المعدل الطبيعي.",
                    style = MaterialTheme.typography.bodySmall.copy(color = DeepSlate)
                )
                Text(
                    "• 📈 فرصة توفير: تحويل 15,000 ج.م الفائضة لحساب استثمار أو شهادة بنكية بعائد دوري.",
                    style = MaterialTheme.typography.bodySmall.copy(color = EmeraldGreen)
                )
                Text(
                    "• 🔔 استحقاق قريب: قسط الجمعية لشهر أبريل يستحق بعد 4 أيام.",
                    style = MaterialTheme.typography.bodySmall.copy(color = CoralRed)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = DeepSlate)
            ) {
                Text("تطبيق التوصيات")
            }
        }
    )
}

/**
 * Quick Cost Bottom Sheet (ModalBottomSheet)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickCostBottomSheet(
    onDismiss: () -> Unit,
    onCostSaved: (amount: String, category: String, desc: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var amountText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("المنزل") }

    val categories = listOf("المنزل", "الطبي", "الشخصي", "العمل", "التعليم", "المركبات")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = SurfaceCrispWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .padding(bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "تسجيل تكلفة سريعة",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepSlate
                    )
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "إغلاق")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Amount Field
            OutlinedTextField(
                value = amountText,
                onValueChange = { amountText = it },
                label = { Text("المبلغ (بالجنيه المصري)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Description Field
            OutlinedTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                label = { Text("بيان المصروف أو البند") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "التصنيف:",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.take(3).forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) GoldPrimary else SurfaceCard)
                            .clickable { selectedCategory = cat }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) DeepSlate else TextSecondaryDark
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (amountText.isNotBlank()) {
                        onCostSaved(amountText, selectedCategory, descriptionText)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldPrimary,
                    contentColor = DeepSlate
                ),
                shape = RoundedCornerShape(14.dp),
                enabled = amountText.isNotBlank()
            ) {
                Text(
                    text = "حفظ التكلفة فوراً",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepSlate
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, locale = "ar")
@Composable
fun HomeScreenPreview() {
    SmartCostProTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            HomeScreen(
                viewModel = null,
                onNavigateToGateway = {}
            )
        }
    }
}
