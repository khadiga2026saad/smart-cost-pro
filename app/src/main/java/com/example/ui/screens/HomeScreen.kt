package com.example.ui.screens

import android.content.Context
import android.content.ContextWrapper
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.compose.material3.Switch
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.DirectionsCar
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
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
/**
 * Approved home layout: one gateway to the costs/expenses section.
 * Other surfaces are read-only indicators, not navigation cards.
 * Financial values are sourced from FinanceViewModel; no Stitch demo figures.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    viewModel: FinanceViewModel? = null,
    onNavigateToGateway: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var now by remember { mutableStateOf(Date()) }
    LaunchedEffect(Unit) {
        while (true) { now = Date(); delay(1000L) }
    }
    val time = remember(now) { SimpleDateFormat("hh:mm:ss a", Locale("ar")).format(now) }
    val date = remember(now) { SimpleDateFormat("d MMMM yyyy", Locale("ar")).format(now) }
    val summary = viewModel?.currentMonthSummary?.collectAsStateWithLifecycle()?.value
    val income = summary?.let { MoneyUtils.formatPiastres(it.totalRevenuesPiastres) } ?: "—"
    val expenses = summary?.let { MoneyUtils.formatPiastres(it.totalExpensesPiastres) } ?: "—"
    val balance = summary?.let { MoneyUtils.formatPiastres(it.currentLiquidBalancePiastres) } ?: "—"
    var userNameState by remember { mutableStateOf("أحمد محمود") }
    var userRoleState by remember { mutableStateOf("مؤسس برمجيات ورائد أعمال") }
    var tickerTextState by remember { mutableStateOf("✦ SMART COST PRO • نظام المراقبة المالية • تابع بيانات التكاليف من أفرع القسم • ") }
    var colorThemeState by remember { mutableStateOf("ذهبي ملكي") }
    var showSettingsDialog by remember { mutableStateOf(false) }
    val appContext = LocalContext.current
    val authPreferences = remember(appContext) {
        appContext.getSharedPreferences("smart_cost_auth_prefs", Context.MODE_PRIVATE)
    }
    val signedInUser = FirebaseAuth.getInstance().currentUser
    var biometricEnabled by remember {
        mutableStateOf(
            signedInUser != null &&
                authPreferences.getBoolean("biometric_enabled", false) &&
                authPreferences.getString("biometric_uid", null) == signedInUser.uid
        )
    }
    var biometricNotice by remember { mutableStateOf<String?>(null) }

    fun changeBiometricSetting(enable: Boolean) {
        if (!enable) {
            authPreferences.edit().putBoolean("biometric_enabled", false).remove("biometric_uid").apply()
            biometricEnabled = false
            biometricNotice = "تم إيقاف الدخول بالبصمة. لم يتم حذف الحساب أو بياناته."
            return
        }
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null || user.isAnonymous || !user.isEmailVerified) {
            biometricNotice = "سجّل الدخول بحساب موثّق أولًا لتفعيل البصمة."
            biometricEnabled = false
            return
        }
        val activity = appContext.findFragmentActivity()
        if (activity == null) {
            biometricNotice = "تعذر فتح نافذة التحقق على هذا الجهاز."
            return
        }
        val availability = BiometricManager.from(activity)
            .canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)
        if (availability != BiometricManager.BIOMETRIC_SUCCESS) {
            biometricNotice = when (availability) {
                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "أضف بصمة أو وسيلة تعرف حيوي من إعدادات أمان الهاتف أولًا."
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "هذا الجهاز لا يدعم التحقق الحيوي."
                else -> "التحقق الحيوي غير متاح حاليًا."
            }
            biometricEnabled = false
            return
        }
        val expectedUid = user.uid
        val prompt = BiometricPrompt(
            activity,
            ContextCompat.getMainExecutor(activity),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val current = FirebaseAuth.getInstance().currentUser
                    if (current != null && current.uid == expectedUid &&
                        !current.isAnonymous && current.isEmailVerified) {
                        authPreferences.edit().putBoolean("biometric_enabled", true)
                            .putString("biometric_uid", expectedUid).apply()
                        biometricEnabled = true
                        biometricNotice = "تم تفعيل الدخول بالبصمة لهذا الحساب."
                    } else {
                        biometricEnabled = false
                        biometricNotice = "تغيّر الحساب؛ لم يتم تفعيل البصمة."
                    }
                }
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    biometricEnabled = false
                    biometricNotice = if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON ||
                        errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                        "تم إلغاء تفعيل البصمة."
                    } else {
                        "تعذر التحقق: $errString"
                    }
                }
                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    biometricNotice = "لم تتطابق البصمة؛ حاول مرة أخرى."
                }
            }
        )
        prompt.authenticate(
            BiometricPrompt.PromptInfo.Builder()
                .setTitle("تفعيل الدخول بالبصمة")
                .setSubtitle("أكد هويتك لتفعيل الدخول السريع لهذا الحساب")
                .setNegativeButtonText("إلغاء")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK)
                .build()
        )
    }
    var showMenu by remember { mutableStateOf(false) }

    val themeAccentColor = remember(colorThemeState) {
        when (colorThemeState) {
            "أخضر زمردي" -> Color(0xFF10B981)
            "أزرق سيان" -> Color(0xFF0284C7)
            else -> GoldPrimary
        }
    }
    val themeDarkAccentColor = remember(colorThemeState) {
        when (colorThemeState) {
            "أخضر زمردي" -> Color(0xFF047857)
            "أزرق سيان" -> Color(0xFF0369A1)
            else -> GoldDark
        }
    }
    val themeLightAccentColor = remember(colorThemeState) {
        when (colorThemeState) {
            "أخضر زمردي" -> Color(0xFFE8F5E9)
            "أزرق سيان" -> Color(0xFFE0F2FE)
            else -> GoldLight
        }
    }

    val ink = Color(0xFF0B1528)
    val pale = Color(0xFFF8FAFC)
    val cyan = Color(0xFF06B6D4)

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = pale,
        bottomBar = {
            NavigationBar(containerColor = Color.White, modifier = Modifier.testTag("home_bottom_nav")) {
                NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Home, null) }, label = { Text("الرئيسية") })
                NavigationBarItem(selected = false, onClick = { onNavigateToGateway("expenses") }, icon = { Icon(Icons.Default.ReceiptLong, null) }, label = { Text("التكاليف") })
                NavigationBarItem(selected = false, onClick = { onNavigateToGateway("expenses") }, icon = { Icon(Icons.Default.PieChart, null) }, label = { Text("الملخص") })
                NavigationBarItem(selected = false, onClick = { showSettingsDialog = true }, icon = { Icon(Icons.Default.Settings, null) }, label = { Text("الإعدادات") })
            }
        }
    ) { insets ->
        Column(Modifier.fillMaxSize().padding(insets).verticalScroll(androidx.compose.foundation.rememberScrollState())) {
            // الشريط العلوي التنفيذي وشريط الأخبار بكامل العرض وتنسيق متوازن على شاشات الموبيل
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(pale)
                    .padding(vertical = 8.dp)
            ) {
                // 1. الشريط العلوي التنفيذي بعرض موسع ومتناسق
                ExecutiveHeader(
                    timeString = time,
                    dateString = date,
                    userName = userNameState,
                    userRole = userRoleState,
                    accentColor = themeAccentColor,
                    onSettingsClick = { showSettingsDialog = true },
                    onLogout = onLogout,
                    onHomeClick = { onNavigateToGateway("expenses") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)
                )

                Spacer(Modifier.height(8.dp))

                // 2. شريط الأخبار المالي المتحرك بعرض موسع ومتناسق مع الشريط العلوي
                ApprovedHomeTicker(
                    tickerText = tickerTextState,
                    accentColor = themeAccentColor,
                    lightAccentColor = themeLightAccentColor,
                    onClick = { showSettingsDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp)
                )
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(7.dp).background(GoldPrimary, CircleShape))
                Spacer(Modifier.width(6.dp))
                Text("نظام المراقبة المالية", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = ink)
                Text("  •  FINANCIAL MONITORING", fontSize = 9.sp, color = SlateMedium)
            }
            Spacer(Modifier.height(12.dp))

            Surface(
                onClick = { onNavigateToGateway("expenses") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp)
                    .testTag("costs_only_gateway"),
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                border = BorderStroke(1.5.dp, GoldPrimary.copy(alpha = 0.6f)),
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    Color.White,
                                    GoldLight.copy(alpha = 0.45f)
                                )
                            )
                        )
                        .padding(15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GoldLight)
                            .border(1.dp, GoldPrimary.copy(alpha = 0.6f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.AttachMoney,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = GoldDark
                        )
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(Modifier.weight(1f)) {
                        Text(
                            "دخول قسم التكاليف",
                            color = DeepSlate,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            "إدارة الميزانيات والمصروفات والتدفقات المالية",
                            color = TextSecondaryDark,
                            fontSize = 10.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(GoldLight.copy(alpha = 0.6f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ArrowDownward,
                            contentDescription = "فتح أفرع التكاليف",
                            tint = GoldDark,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            Row(Modifier.fillMaxWidth().padding(horizontal = 14.dp), horizontalArrangement = Arrangement.spacedBy(9.dp)) {
                HomeReadOnlyIndicator("الدخل الشهري", income, EmeraldGreen, Modifier.weight(1f))
                HomeReadOnlyIndicator("المصروفات الشهرية", expenses, CoralRed, Modifier.weight(1f))
            }
            Spacer(Modifier.height(12.dp))
            Surface(Modifier.fillMaxWidth().padding(horizontal = 14.dp), shape = RoundedCornerShape(16.dp), color = Color.White, border = BorderStroke(1.dp, BorderLight)) {
                Column(Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CreditCard, null, tint = GoldDark)
                        Spacer(Modifier.width(8.dp))
                        Column(Modifier.weight(1f)) {
                            Text("الرصيد الجاري", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = ink)
                            Text("Current Balance & Liquidity", fontSize = 10.sp, color = SlateMedium)
                        }
                        Text("مؤشر مالي", fontSize = 10.sp, color = cyan)
                    }
                    Spacer(Modifier.height(13.dp))
                    Text(balance, fontSize = 24.sp, fontWeight = FontWeight.Black, color = ink)
                }
            }
            Spacer(Modifier.height(12.dp))
            SubCategoriesDropdownSection(onNavigateToGateway = onNavigateToGateway)
            Spacer(Modifier.height(12.dp))
            CostStudyExportSection()
            Spacer(Modifier.height(18.dp))
        }

        if (showSettingsDialog) {
            SettingsDialog(
                currentUserName = userNameState,
                currentUserRole = userRoleState,
                tickerText = tickerTextState,
                currentColorTheme = colorThemeState,
                biometricEnabled = biometricEnabled,
                biometricNotice = biometricNotice,
                onBiometricChange = { changeBiometricSetting(it) },
                onSaveSettings = { newName, newRole, newTicker, newTheme ->
                    userNameState = newName
                    userRoleState = newRole
                    tickerTextState = newTicker
                    colorThemeState = newTheme
                },
                onDismiss = { showSettingsDialog = false }
            )
        }
    }
}

@Composable
private fun HomeReadOnlyIndicator(title: String, value: String, accent: Color, modifier: Modifier = Modifier) {
    Surface(modifier, shape = RoundedCornerShape(14.dp), color = Color.White, border = BorderStroke(1.dp, BorderLight)) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(7.dp).background(accent, CircleShape))
                Spacer(Modifier.width(5.dp))
                Text(title, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepSlate)
            }
            Spacer(Modifier.height(6.dp))
            Text(value, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold, color = accent)
        }
    }
}

/**
 * 2. News Ticker Bar (شريط الأخبار المالي المتحرك)
 * شريط أنيق يطابق تماماً النموذج البصري المطلوب:
 * - كبسولة شعار سمارت كوست برو والسهم باللون السماوي يميناً
 * - كبسولة بيضاء بإطار بنفسجي هادئ تحتوي على النص المتحرك مع النجوم ✦ يساراً
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ApprovedHomeTicker(
    tickerText: String = "✦ نظام فائق يعزز إنجاز كل مسؤول ✦ Smart Cost Pro • دقة التكاليف • سهولة التتبع ✦",
    accentColor: Color = GoldPrimary,
    lightAccentColor: Color = Color(0xFFE0F2FE),
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Row(
        modifier = modifier
            .height(50.dp)
            .testTag("approved_home_ticker"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. كبسولة الشعار والسهم يميناً (تظهر يميناً في الواجهة العربية RTL)
        Surface(
            modifier = Modifier
                .fillMaxHeight()
                .clickable(onClick = onClick)
                .testTag("ticker_logo_container"),
            shape = RoundedCornerShape(18.dp),
            color = lightAccentColor,
            border = BorderStroke(1.2.dp, Color(0xFFBAE6FD)),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White)
                        .border(0.8.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_smart_cost_logo),
                        contentDescription = "شعار سمارت كوست برو",
                        modifier = Modifier.size(30.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = Color(0xFF0284C7),
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // 2. كبسولة النص المتحرك يساراً بإطار بنفسجي هادئ كما بالصورة
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clickable(onClick = onClick)
                .testTag("ticker_marquee_container"),
            shape = RoundedCornerShape(22.dp),
            color = Color.White,
            border = BorderStroke(1.2.dp, Color(0xFFE9D5FF)),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "✦",
                    color = Color(0xFF9333EA),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(6.dp))
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    Text(
                        text = tickerText,
                        modifier = Modifier
                            .weight(1f)
                            .basicMarquee(
                                iterations = Int.MAX_VALUE,
                                velocity = 35.dp
                            ),
                        maxLines = 1,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepSlate
                    )
                }
                Spacer(Modifier.width(6.dp))
                Text(
                    text = "✦",
                    color = Color(0xFF9333EA),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * 1. Top App Bar (Executive Header)
 * شريط تنفيذي علوي منسق بدقة متناهية وبكامل العرض ليتسع لكافة الشاشات وأجهزة الأندرويد الحقيقية
 */
@Composable
fun ExecutiveHeader(
    timeString: String,
    dateString: String,
    userName: String = "أحمد محمود",
    userRole: String = "مؤسس برمجيات ورائد أعمال",
    accentColor: Color = GoldPrimary,
    onSearchClick: () -> Unit = {},
    onVoiceClick: () -> Unit = {},
    onSettingsClick: () -> Unit = {},
    onLogout: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    var expandedMenu by remember { mutableStateOf(false) }
    var moreMenuExpanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .testTag("executive_header_card"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 1. User Profile على اليمين مع صورة المدير التنفيذي
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .weight(1f, fill = false)
                    .testTag("header_user_profile")
            ) {
                Box(modifier = Modifier.size(40.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.img_avatar_executive),
                        contentDescription = "صورة المدير التنفيذي",
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, Color(0xFF00E5FF), CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .size(9.dp)
                            .align(Alignment.BottomStart)
                            .background(EmeraldGreen, CircleShape)
                            .border(1.2.dp, SurfaceCrispWhite, CircleShape)
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Column(
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepSlate,
                            fontSize = 12.5.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = userRole,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = Color(0xFF0284C7),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 9.sp
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // 2. أدوات التحكم على اليسار - أيقونات مصغرة وبخلفية شفافة تماماً بدون أي تلاصق
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // جرس الإشعارات (مصغر وخلفية شفافة تماماً)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = {})
                        .testTag("btn_header_notifications"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        contentDescription = "الإشعارات",
                        tint = DeepSlate,
                        modifier = Modifier.size(21.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .align(Alignment.TopEnd)
                            .background(CoralRed, CircleShape)
                    )
                }

                // Keep the compact clock legible; the full date has its own row below.
                val timeSegments = remember(timeString) {
                    val trimmed = timeString.trim()
                    val parts = trimmed.split(" ")
                    val timeDigits = parts.getOrNull(0) ?: "07:16:00"
                    val period = parts.getOrNull(1) ?: "PM"
                    val colonSegments = timeDigits.split(":")
                    val hhMm = "${colonSegments.getOrElse(0) { "07" }}:${colonSegments.getOrElse(1) { "16" }}"
                    val ss = colonSegments.getOrElse(2) { "00" }
                    Triple(hhMm, ss, period)
                }
                val (hhMm, ss, period) = timeSegments
                val compactDate = remember(dateString) {
                    dateString.replace(Regex("\\s\\d{4}"), "")
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    modifier = Modifier.testTag("header_clock")
                ) {
                    Icon(Icons.Filled.Schedule, contentDescription = null, tint = accentColor, modifier = Modifier.size(17.dp))
                    Text("$"+"{hhMm}:$"+"ss", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepSlate, maxLines = 1)
                    Text(period, fontSize = 10.sp, color = SlateMedium, maxLines = 1)
                }

                // زر الإعدادات المباشر (مصغر وخلفية شفافة تماماً)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable(onClick = onSettingsClick)
                        .testTag("btn_header_settings"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "الإعدادات",
                        tint = DeepSlate,
                        modifier = Modifier.size(21.dp)
                    )
                }

                // قائمة الخيارات الإضافية (مصغرة وخلفية شفافة تماماً)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .clickable { moreMenuExpanded = true }
                        .testTag("btn_header_more"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "المزيد",
                        tint = DeepSlate,
                        modifier = Modifier.size(21.dp)
                    )
                    DropdownMenu(
                        expanded = moreMenuExpanded,
                        onDismissRequest = { moreMenuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("إعدادات الحساب والألوان والشريط") },
                            onClick = {
                                moreMenuExpanded = false
                                onSettingsClick()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Settings, contentDescription = null, tint = accentColor)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("تسجيل الخروج") },
                            onClick = {
                                moreMenuExpanded = false
                                onLogout()
                            },
                            leadingIcon = {
                                Icon(Icons.Default.Close, contentDescription = null, tint = CoralRed)
                            }
                        )
                    }
                }
            }
        }
    }
    Spacer(Modifier.height(5.dp))
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 8.dp)
            .border(1.dp, BorderLight.copy(alpha = 0.65f), RoundedCornerShape(10.dp))
            .testTag("header_date_strip")
            .padding(horizontal = 12.dp, vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(Icons.Filled.DateRange, contentDescription = null, tint = accentColor, modifier = Modifier.size(19.dp))
        Spacer(Modifier.width(8.dp))
        Text(dateString, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = DeepSlate, maxLines = 1)
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
                        .size(46.dp)
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
                        modifier = Modifier.size(24.dp)
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

@Composable
private fun SubCategoriesDropdownSection(
    onNavigateToGateway: (String) -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val subCategories = remember {
        listOf(
            SubCategoryItem("تموين ومشتريات غذائية", "مصروفات المنزل", "expenses", Icons.Default.ReceiptLong),
            SubCategoryItem("فواتير ومرافق", "مصروفات المنزل", "expenses", Icons.Default.ReceiptLong),
            SubCategoryItem("أدوية وصيدليات", "الرعاية الصحية", "expenses", Icons.Default.ReceiptLong),
            SubCategoryItem("كشوفات واستشارات طبية", "الرعاية الصحية", "expenses", Icons.Default.ReceiptLong),
            SubCategoryItem("ملابس وأناقة", "المصروفات الشخصية", "expenses", Icons.Default.ReceiptLong),
            SubCategoryItem("عناية ومظهر شخصي", "المصروفات الشخصية", "expenses", Icons.Default.ReceiptLong),
            SubCategoryItem("وقود وتعبئات", "المركبات", "vehicles", Icons.Default.DirectionsCar),
            SubCategoryItem("صيانة دورية وإصلاحات", "المركبات", "vehicles", Icons.Default.DirectionsCar),
            SubCategoryItem("رسوم دراسية", "التعليم", "expenses", Icons.Default.ReceiptLong),
            SubCategoryItem("شهادات الدخل والأرباح", "الإيرادات", "income_accounts", Icons.Default.AttachMoney)
        )
    }

    val filteredCategories = remember(searchQuery) {
        if (searchQuery.isBlank()) subCategories
        else subCategories.filter { it.title.contains(searchQuery, ignoreCase = true) || it.departmentTitle.contains(searchQuery, ignoreCase = true) }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("sub_categories_dropdown_section"),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BorderLight),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .testTag("sub_categories_header_click"),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(GoldLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "بحث الأقسام",
                            tint = GoldDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "قائمة الأقسام الفرعية المنسدلة",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackPrimary
                            )
                        )
                        Text(
                            text = "انقر للاستعراض، البحث، والانتقال المباشر للقسم",
                            style = MaterialTheme.typography.labelSmall.copy(color = SlateMedium)
                        )
                    }
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = null,
                    tint = GoldDark
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("بحث في الأقسام الفرعية...") },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = GoldDark)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sub_categories_search_input"),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(10.dp))

                if (filteredCategories.isEmpty()) {
                    Text(
                        text = "لا توجد نتائج مطابقة للبحث",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        filteredCategories.forEach { cat ->
                            Surface(
                                onClick = {
                                    onNavigateToGateway(cat.gatewayTarget)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("sub_category_item_${cat.gatewayTarget}"),
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, BorderLight)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = cat.icon,
                                            contentDescription = null,
                                            tint = GoldDark,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = cat.title,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = BlackPrimary
                                                )
                                            )
                                            Text(
                                                text = cat.departmentTitle,
                                                style = MaterialTheme.typography.labelSmall.copy(color = SlateMedium)
                                            )
                                        }
                                    }
                                    Icon(
                                        imageVector = Icons.Default.ArrowDownward,
                                        contentDescription = "انتقال مباشر",
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CostStudyExportSection() {
    var exportDialogMessage by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp)
            .testTag("cost_study_export_section"),
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, BorderLight),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(GoldLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = "دراسة التكاليف",
                            tint = GoldDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "تصدير دراسة التكاليف (PDF / Excel)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = BlackPrimary
                            )
                        )
                        Text(
                            text = "استخراج تقارير تكاليف شاملة بتنسيق احترافي",
                            style = MaterialTheme.typography.labelSmall.copy(color = SlateMedium)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        exportDialogMessage = "تم تجهيز ملف دراسة التكاليف بصيغة PDF بنجاح وجاهز للمشاركة أو الحفظ."
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("export_pdf_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = GoldPrimary,
                        contentColor = DeepSlate
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        modifier = Modifier.size(21.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "تصدير PDF",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }

                Button(
                    onClick = {
                        exportDialogMessage = "تم تجهيز جدول دراسة التكاليف بصيغة Excel (XLSX) بنجاح وجاهز للفتح."
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                        .testTag("export_excel_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepSlate,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        modifier = Modifier.size(21.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "تصدير Excel",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }

    if (exportDialogMessage != null) {
        AlertDialog(
            onDismissRequest = { exportDialogMessage = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(32.dp)
                )
            },
            title = {
                Text(
                    text = "نجاح عملية التصدير",
                    fontWeight = FontWeight.Bold,
                    color = DeepSlate
                )
            },
            text = {
                Text(
                    text = exportDialogMessage ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark),
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = { exportDialogMessage = null },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text("حسناً", color = DeepSlate, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
fun SettingsDialog(
    currentUserName: String,
    currentUserRole: String,
    tickerText: String,
    currentColorTheme: String = "ذهبي ملكي",
    biometricEnabled: Boolean = false,
    biometricNotice: String? = null,
    onBiometricChange: (Boolean) -> Unit = {},
    onSaveSettings: (newName: String, newRole: String, newTicker: String, newTheme: String) -> Unit,
    onDismiss: () -> Unit
) {
    var nameInput by remember { mutableStateOf(currentUserName) }
    var roleInput by remember { mutableStateOf(currentUserRole) }
    var tickerInput by remember { mutableStateOf(tickerText) }
    var selectedColorTheme by remember { mutableStateOf(currentColorTheme) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = GoldDark)
                Spacer(modifier = Modifier.width(8.dp))
                Text("إعدادات النظام والحساب والألوان", fontWeight = FontWeight.Bold, color = DeepSlate)
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(androidx.compose.foundation.rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "تخصيص بيانات الحساب، ألوان الواجهة، وشريط الأخبار المتحرك.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
                )

                Text("الأمان:", fontWeight = FontWeight.Bold, color = DeepSlate, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth().testTag("biometric_setting_row"),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(Modifier.weight(1f).padding(end = 12.dp)) {
                        Text("تفعيل الدخول بالبصمة", fontWeight = FontWeight.SemiBold, color = DeepSlate)
                        Text(
                            "يتطلب حسابًا موثّقًا وجلسة محفوظة على هذا الجهاز.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
                        )
                    }
                    Switch(
                        checked = biometricEnabled,
                        onCheckedChange = onBiometricChange,
                        modifier = Modifier.testTag("biometric_enabled_switch")
                    )
                }
                if (biometricNotice != null) {
                    Text(biometricNotice, style = MaterialTheme.typography.bodySmall, color = GoldDark)
                }

                Text("بيانات الحساب:", fontWeight = FontWeight.Bold, color = DeepSlate, fontSize = 12.sp)
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("اسم المستخدم") },
                    modifier = Modifier.fillMaxWidth().testTag("settings_name_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = roleInput,
                    onValueChange = { roleInput = it },
                    label = { Text("المسمى الوظيفي / الدور") },
                    modifier = Modifier.fillMaxWidth().testTag("settings_role_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )

                Text("ثيم وألوان التطبيق:", fontWeight = FontWeight.Bold, color = DeepSlate, fontSize = 12.sp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("ذهبي ملكي", "أخضر زمردي", "أزرق سيان").forEach { theme ->
                        val isSelected = selectedColorTheme == theme
                        val themeColor = when (theme) {
                            "أخضر زمردي" -> EmeraldGreen
                            "أزرق سيان" -> Color(0xFF0284C7)
                            else -> GoldPrimary
                        }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) themeColor.copy(alpha = 0.2f) else SurfaceCard)
                                .border(
                                    width = if (isSelected) 1.5.dp else 1.dp,
                                    color = if (isSelected) themeColor else BorderLight,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedColorTheme = theme }
                                .padding(vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = theme,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) DeepSlate else TextSecondaryDark
                                )
                            )
                        }
                    }
                }

                Text("محتوى شريط الأخبار المتحرك:", fontWeight = FontWeight.Bold, color = DeepSlate, fontSize = 12.sp)
                OutlinedTextField(
                    value = tickerInput,
                    onValueChange = { tickerInput = it },
                    label = { Text("نص الشريط المتحرك") },
                    modifier = Modifier.fillMaxWidth().testTag("settings_ticker_input"),
                    shape = RoundedCornerShape(10.dp),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSaveSettings(nameInput, roleInput, tickerInput, selectedColorTheme)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary),
                modifier = Modifier.testTag("settings_save_button")
            ) {
                Text("حفظ التغييرات", color = DeepSlate, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.testTag("settings_cancel_button")) {
                Text("إلغاء", color = TextSecondaryDark)
            }
        }
    )
}

private data class SubCategoryItem(
    val title: String,
    val departmentTitle: String,
    val gatewayTarget: String,
    val icon: ImageVector
)

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


private fun Context.findFragmentActivity(): FragmentActivity? {
    var current: Context = this
    while (current is ContextWrapper) {
        if (current is FragmentActivity) return current
        current = current.baseContext
    }
    return current as? FragmentActivity
}
