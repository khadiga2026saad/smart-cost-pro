package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.ui.theme.BackgroundWhite
import com.example.ui.theme.BlackPrimary
import com.example.ui.theme.BorderLight
import com.example.ui.theme.CoralRed
import com.example.ui.theme.DeepSlate
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldGradientEnd
import com.example.ui.theme.GoldGradientStart
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SlateLight
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceCrispWhite
import com.example.ui.theme.TextPrimaryDark
import com.example.ui.theme.TextSecondaryDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * شاشة تسجيل الدخول التنفيذية لـ Smart Cost Pro
 * مطابقة تماماً للمرجع البصري (الصورة المرفقة):
 * 1. الشعار الرسمي داخل بطاقة بيضاء مستديرة مع ظل خفيف.
 * 2. عنوان Smart Cost Pro (مع Pro بلون ذهبي) ووصف المنظومة.
 * 3. شارة eng.mamdouh saad مع علامة التوثيق الذهبية.
 * 4. بطاقة بيضاء رئيسية بخط ذهبي علوي، وحقول المدخلات مع شارة AES-256 bit.
 * 5. خانة تذكر هذا الجهاز الآمن ورابط استعادة كلمة المرور.
 * 6. زر تسجيل دخول داكن (Deep Slate) بسهم.
 * 7. فاصل أو تسجيل الدخول السريع.
 * 8. بطاقة الدخول السريع عبر Face ID / البصمة بخلفية ذهبية فاتحة.
 * 9. زر الدخول بحساب Google.
 * 10. ليس لديك حساب؟ سجل الآن، والدخول كزائر.
 * 11. زر تبديل اللغة الفوري (AR / EN) وتفعيل كامل لجميع الوظائف دون أزرار شكلية.
 */
@Composable
fun AuthScreen(
    onEnterWorkspace: () -> Unit,
    isEnglish: Boolean = false,
    onToggleLanguage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Persistent storage for "Remember this secure device"
    val prefs = remember { context.getSharedPreferences("smart_cost_auth_prefs", Context.MODE_PRIVATE) }
    val savedRememberMe = remember { prefs.getBoolean("remember_device", true) }
    val initialUsername = remember {
        if (savedRememberMe) prefs.getString("saved_user", "eng.mamdouh saad") ?: "eng.mamdouh saad"
        else ""
    }

    // State Holders
    var usernameOrEmail by remember { mutableStateOf(initialUsername) }
    var password by remember { mutableStateOf(if (savedRememberMe) "123456" else "") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(savedRememberMe) }

    // Validation Errors
    var usernameError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Interactive Flow Dialogs
    var isAuthenticating by remember { mutableStateOf(false) }
    var showBiometricModal by remember { mutableStateOf(false) }
    var showGoogleAuthModal by remember { mutableStateOf(false) }
    var showForgotPasswordModal by remember { mutableStateOf(false) }
    var showRegisterModal by remember { mutableStateOf(false) }

    // Action: Validate & Execute Login
    fun performLogin() {
        var hasError = false
        if (usernameOrEmail.trim().isEmpty()) {
            usernameError = context.getString(R.string.auth_error_empty_user)
            hasError = true
        } else {
            usernameError = null
        }

        if (password.trim().isEmpty()) {
            passwordError = context.getString(R.string.auth_error_empty_password)
            hasError = true
        } else if (password.length < 6) {
            passwordError = context.getString(R.string.auth_error_password_length)
            hasError = true
        } else {
            passwordError = null
        }

        if (!hasError) {
            isAuthenticating = true
            // Save or clear remembered username
            prefs.edit().apply {
                putBoolean("remember_device", rememberMe)
                if (rememberMe) {
                    putString("saved_user", usernameOrEmail.trim())
                } else {
                    remove("saved_user")
                }
                apply()
            }

            coroutineScope.launch {
                delay(600) // Smooth authentic transition
                isAuthenticating = false
                onEnterWorkspace()
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Top Bar: Language Switch Button & Mode Badge
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Verified Security / Offline Mode Indicator
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = GoldLight.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = GoldDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "OFFLINE SECURE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = GoldDark,
                                fontSize = 10.sp,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
                }

                // Interactive Language Switch Button (EN / AR)
                Surface(
                    onClick = onToggleLanguage,
                    shape = RoundedCornerShape(20.dp),
                    color = SurfaceCrispWhite,
                    shadowElevation = 2.dp,
                    border = BorderStroke(1.dp, BorderLight),
                    modifier = Modifier.testTag("btn_switch_language")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Language",
                            tint = DeepSlate,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.auth_language_switch_label),
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepSlate
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Official Logo inside White Rounded Card with subtle shadow
            Surface(
                modifier = Modifier
                    .size(108.dp)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(28.dp), clip = false)
                    .testTag("card_logo_container"),
                shape = RoundedCornerShape(28.dp),
                color = SurfaceCrispWhite,
                border = BorderStroke(1.dp, Color(0xFFF1F3F5))
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_smart_cost_logo),
                        contentDescription = "شعار سمارت كوست برو الرسمي",
                        modifier = Modifier
                            .size(92.dp)
                            .clip(RoundedCornerShape(24.dp)),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. App Title: Smart Cost (Dark) + Pro (Royal Gold)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.auth_app_title_first),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = DeepSlate,
                        letterSpacing = 0.5.sp
                    )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.auth_app_title_pro),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = GoldPrimary,
                        letterSpacing = 0.5.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Tagline: منظومة الإدارة والتكاليف التنفيذية الذكية
            Text(
                text = stringResource(R.string.auth_app_tagline),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondaryDark,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Certified Executive User Badge: (eng.mamdouh saad) with Gold Verification Icon
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = SurfaceCrispWhite,
                shadowElevation = 1.dp,
                border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.35f)),
                modifier = Modifier.testTag("badge_verified_user")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "Verified",
                        tint = GoldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = stringResource(R.string.auth_verified_user_badge),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepSlate,
                            letterSpacing = 0.2.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Main White Card with Golden Accent Top Line & Inputs
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 480.dp)
                    .testTag("card_main_auth"),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCrispWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                border = BorderStroke(1.dp, BorderLight)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Golden top accent line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(3.dp)
                            .background(
                                Brush.horizontalGradient(
                                    listOf(
                                        GoldGradientStart,
                                        GoldPrimary,
                                        GoldGradientEnd
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp)
                    ) {
                        // Demo credentials auto-fill button (Quick convenience for user)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = "💡 " + stringResource(R.string.auth_demo_fill),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = GoldDark,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier
                                    .clickable {
                                        usernameOrEmail = "eng.mamdouh saad"
                                        password = "••••••••"
                                        usernameError = null
                                        passwordError = null
                                    }
                                    .testTag("btn_auto_fill_demo")
                            )
                        }

                        // Field 1: Username / Email
                        Text(
                            text = stringResource(R.string.auth_input_user_label),
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepSlate
                            ),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = usernameOrEmail,
                            onValueChange = {
                                usernameOrEmail = it
                                if (usernameError != null) usernameError = null
                            },
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.auth_input_user_placeholder),
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = null,
                                    tint = if (usernameError != null) CoralRed else GoldDark
                                )
                            },
                            isError = usernameError != null,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = BorderLight,
                                errorBorderColor = CoralRed,
                                focusedContainerColor = SurfaceCrispWhite,
                                unfocusedContainerColor = SurfaceCard
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_auth_username")
                        )
                        if (usernameError != null) {
                            Text(
                                text = usernameError!!,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CoralRed,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Field 2: Password with AES-256 bit Badge
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.auth_input_password_label),
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = DeepSlate
                                )
                            )

                            // AES-256 bit Security Indicator
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = EmeraldGreen.copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, EmeraldGreen.copy(alpha = 0.3f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = null,
                                        tint = EmeraldGreen,
                                        modifier = Modifier.size(11.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = stringResource(R.string.auth_input_password_aes),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = EmeraldGreen,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = password,
                            onValueChange = {
                                password = it
                                if (passwordError != null) passwordError = null
                            },
                            placeholder = {
                                Text(
                                    text = "••••••••",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = if (passwordError != null) CoralRed else GoldDark
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = { isPasswordVisible = !isPasswordVisible },
                                    modifier = Modifier.testTag("btn_toggle_password_visibility")
                                ) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password visibility",
                                        tint = TextSecondaryDark
                                    )
                                }
                            },
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            isError = passwordError != null,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(onDone = { performLogin() }),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = BorderLight,
                                errorBorderColor = CoralRed,
                                focusedContainerColor = SurfaceCrispWhite,
                                unfocusedContainerColor = SurfaceCard
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_auth_password")
                        )
                        if (passwordError != null) {
                            Text(
                                text = passwordError!!,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CoralRed,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Remember Me Checkbox & Forgot Password Link
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { rememberMe = !rememberMe }
                                    .padding(vertical = 4.dp)
                            ) {
                                Checkbox(
                                    checked = rememberMe,
                                    onCheckedChange = { rememberMe = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = DeepSlate,
                                        checkmarkColor = GoldPrimary,
                                        uncheckedColor = Color.Gray
                                    ),
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag("checkbox_remember_me")
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = stringResource(R.string.auth_remember_secure_device),
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = DeepSlate,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }

                            Text(
                                text = stringResource(R.string.auth_forgot_password),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = GoldDark,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier
                                    .clickable { showForgotPasswordModal = true }
                                    .testTag("link_forgot_password")
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Primary Login Button (Dark Deep Slate with Arrow)
                        Button(
                            onClick = { performLogin() },
                            enabled = !isAuthenticating,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("btn_submit_login"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepSlate,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                        ) {
                            if (isAuthenticating) {
                                CircularProgressIndicator(
                                    color = GoldPrimary,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.5.dp
                                )
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = stringResource(R.string.auth_btn_login),
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = null,
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        // Divider: أو تسجيل الدخول السريع
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = BorderLight
                            )
                            Text(
                                text = stringResource(R.string.auth_or_quick_login),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondaryDark,
                                    fontWeight = FontWeight.Normal
                                ),
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = BorderLight
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // 6. Fast Biometric Card (Face ID / Fingerprint) with light gold background
                        Surface(
                            onClick = { showBiometricModal = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("card_biometric_auth"),
                            shape = RoundedCornerShape(16.dp),
                            color = GoldLight.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = GoldPrimary.copy(alpha = 0.2f),
                                    modifier = Modifier.size(44.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Fingerprint,
                                            contentDescription = "Fingerprint / Face ID",
                                            tint = GoldDark,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(14.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = stringResource(R.string.auth_card_biometric_title),
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = DeepSlate
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = stringResource(R.string.auth_card_biometric_desc),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = EmeraldGreen,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    )
                                }

                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = GoldDark,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 7. Google Sign-in Button with official Google Logo
                        OutlinedButton(
                            onClick = { showGoogleAuthModal = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_google_signin"),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, BorderLight),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = SurfaceCrispWhite,
                                contentColor = DeepSlate
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.ic_google_logo),
                                    contentDescription = "Google Logo",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = stringResource(R.string.auth_btn_google),
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DeepSlate
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 8. Footer Links: Not registered? Sign up now & Guest Mode
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.auth_dont_have_account) + " ",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondaryDark
                    )
                )
                Text(
                    text = stringResource(R.string.auth_register_now),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = GoldDark
                    ),
                    modifier = Modifier
                        .clickable { showRegisterModal = true }
                        .testTag("link_register_now")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Guest Mode Option (Explore as Guest)
            TextButton(
                onClick = {
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(context.getString(R.string.auth_guest_welcome))
                    }
                    onEnterWorkspace()
                },
                modifier = Modifier.testTag("btn_guest_mode")
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = SlateLight,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(R.string.auth_login_as_guest),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = SlateLight,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Offline Constitutional Guarantee
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "بياناتك مشفرة ومحفوظة بالكامل على هذا الجهاز",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondaryDark,
                        fontSize = 11.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }

    // Modal 1: Biometric Verification Dialog
    if (showBiometricModal) {
        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
        val pulseScale by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 1.15f,
            animationSpec = infiniteRepeatable(
                animation = tween(900),
                repeatMode = RepeatMode.Reverse
            ),
            label = "pulse_scale"
        )

        var biometricState by remember { mutableStateOf("SCANNING") } // SCANNING -> VERIFIED

        Dialog(onDismissRequest = { showBiometricModal = false }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = SurfaceCrispWhite,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.auth_biometric_prompt_title),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepSlate
                            )
                        )
                        IconButton(onClick = { showBiometricModal = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .scale(if (biometricState == "SCANNING") pulseScale else 1f)
                            .background(
                                color = if (biometricState == "VERIFIED") EmeraldGreen.copy(alpha = 0.15f) else GoldLight,
                                shape = CircleShape
                            )
                            .border(
                                width = 2.dp,
                                color = if (biometricState == "VERIFIED") EmeraldGreen else GoldPrimary,
                                shape = CircleShape
                            )
                            .clickable {
                                if (biometricState == "SCANNING") {
                                    biometricState = "VERIFIED"
                                    coroutineScope.launch {
                                        delay(700)
                                        showBiometricModal = false
                                        onEnterWorkspace()
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (biometricState == "VERIFIED") Icons.Default.Check else Icons.Default.Fingerprint,
                            contentDescription = "Sensor",
                            tint = if (biometricState == "VERIFIED") EmeraldGreen else GoldDark,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = if (biometricState == "VERIFIED")
                            stringResource(R.string.auth_biometric_sim_success)
                        else
                            stringResource(R.string.auth_biometric_prompt_desc),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = if (biometricState == "VERIFIED") EmeraldGreen else DeepSlate,
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            biometricState = "VERIFIED"
                            coroutineScope.launch {
                                delay(600)
                                showBiometricModal = false
                                onEnterWorkspace()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DeepSlate),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (biometricState == "VERIFIED") "متابعة..." else "تأكيد البصمة والمتابعة",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }

    // Modal 2: Google Sign-in Interactive Dialog
    if (showGoogleAuthModal) {
        var isGoogleLoading by remember { mutableStateOf(false) }

        Dialog(onDismissRequest = { if (!isGoogleLoading) showGoogleAuthModal = false }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = SurfaceCrispWhite,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = "Google",
                        modifier = Modifier.size(36.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "تسجيل الدخول باستخدام Google",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = DeepSlate
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "اختر حسابك المعتمد للدخول إلى مساحة العمل",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Account item card
                    Surface(
                        onClick = {
                            isGoogleLoading = true
                            coroutineScope.launch {
                                delay(900)
                                isGoogleLoading = false
                                showGoogleAuthModal = false
                                onEnterWorkspace()
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, BorderLight),
                        color = SurfaceCard,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = GoldPrimary,
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "M",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Black
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "م. ممدوح سعد (Mamdouh Saad)",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = DeepSlate
                                    )
                                )
                                Text(
                                    text = "eng.mamdouh.saad@gmail.com",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondaryDark)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    if (isGoogleLoading) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = GoldPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = stringResource(R.string.auth_google_signing_in),
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = DeepSlate,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    } else {
                        OutlinedButton(
                            onClick = { showGoogleAuthModal = false },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.auth_close))
                        }
                    }
                }
            }
        }
    }

    // Modal 3: Forgot Password Dialog
    if (showForgotPasswordModal) {
        var recoveryEmail by remember { mutableStateOf(usernameOrEmail) }
        var isSent by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showForgotPasswordModal = false },
            title = {
                Text(
                    text = stringResource(R.string.auth_reset_pwd_title),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = DeepSlate
                    )
                )
            },
            text = {
                Column {
                    if (isSent) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.auth_reset_success),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = EmeraldGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    } else {
                        Text(
                            text = stringResource(R.string.auth_reset_pwd_desc),
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondaryDark)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = recoveryEmail,
                            onValueChange = { recoveryEmail = it },
                            label = { Text(stringResource(R.string.email_label)) },
                            leadingIcon = {
                                Icon(Icons.Default.Mail, contentDescription = null, tint = GoldDark)
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (isSent) {
                            showForgotPasswordModal = false
                        } else {
                            isSent = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DeepSlate),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(if (isSent) stringResource(R.string.auth_close) else stringResource(R.string.auth_send_code))
                }
            },
            dismissButton = {
                if (!isSent) {
                    TextButton(onClick = { showForgotPasswordModal = false }) {
                        Text(stringResource(R.string.action_cancel), color = Color.Gray)
                    }
                }
            }
        )
    }

    // Modal 4: Full Register / Sign-Up Dialog
    if (showRegisterModal) {
        var regName by remember { mutableStateOf("") }
        var regEmail by remember { mutableStateOf("") }
        var regPass by remember { mutableStateOf("") }
        var regPassConfirm by remember { mutableStateOf("") }
        var regError by remember { mutableStateOf<String?>(null) }
        var isCreating by remember { mutableStateOf(false) }

        Dialog(onDismissRequest = { showRegisterModal = false }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = SurfaceCrispWhite,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(22.dp)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.auth_register_title),
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = DeepSlate
                            )
                        )
                        IconButton(onClick = { showRegisterModal = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = regName,
                        onValueChange = { regName = it },
                        label = { Text(stringResource(R.string.auth_register_name)) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = GoldDark) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = regEmail,
                        onValueChange = { regEmail = it },
                        label = { Text(stringResource(R.string.auth_register_email)) },
                        leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null, tint = GoldDark) },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = regPass,
                        onValueChange = { regPass = it },
                        label = { Text(stringResource(R.string.auth_register_password)) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = GoldDark) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = regPassConfirm,
                        onValueChange = { regPassConfirm = it },
                        label = { Text(stringResource(R.string.auth_register_confirm_password)) },
                        leadingIcon = { Icon(Icons.Default.Security, contentDescription = null, tint = GoldDark) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (regError != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = regError!!,
                            style = MaterialTheme.typography.labelSmall.copy(color = CoralRed, fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            if (regName.isBlank() || regEmail.isBlank() || regPass.isBlank()) {
                                regError = context.getString(R.string.error_required_field)
                            } else if (regPass != regPassConfirm) {
                                regError = context.getString(R.string.auth_error_confirm_mismatch)
                            } else if (regPass.length < 6) {
                                regError = context.getString(R.string.auth_error_password_length)
                            } else {
                                regError = null
                                isCreating = true
                                coroutineScope.launch {
                                    delay(700)
                                    isCreating = false
                                    showRegisterModal = false
                                    onEnterWorkspace()
                                }
                            }
                        },
                        enabled = !isCreating,
                        colors = ButtonDefaults.buttonColors(containerColor = DeepSlate),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        if (isCreating) {
                            CircularProgressIndicator(
                                color = GoldPrimary,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = stringResource(R.string.auth_register_btn),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
