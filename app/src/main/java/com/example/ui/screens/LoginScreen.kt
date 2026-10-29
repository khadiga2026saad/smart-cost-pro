package com.example.ui.screens

import android.content.Context
import android.content.ContextWrapper
import androidx.fragment.app.FragmentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.viewmodel.LoginViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.R

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onBiometricClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onGuestClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    selectedLanguage: String = "ar",
    onSelectLanguage: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel()
) {
    var languageMenuExpanded by remember { mutableStateOf(false) }
    val username = loginViewModel.username
    val password = loginViewModel.password
    val showPassword = loginViewModel.showPassword
    val rememberDevice = loginViewModel.rememberDevice
    val context = androidx.compose.ui.platform.LocalContext.current
    val activity = remember(context) {
        var current: Context? = context
        var found: FragmentActivity? = null
        while (current != null) {
            if (current is FragmentActivity) {
                found = current
                break
            }
            current = (current as? ContextWrapper)?.baseContext
        }
        found
    }
    val notice = loginViewModel.notice
    val error = loginViewModel.errorMessage
    val isLoading = loginViewModel.isLoading
    val showRegisterDialog = loginViewModel.showRegisterDialog
    val showForgotPasswordDialog = loginViewModel.showForgotPasswordDialog
    var confirmPassword by remember { mutableStateOf("") }
    var googlePassword by remember { mutableStateOf("") }
    var confirmGooglePassword by remember { mutableStateOf("") }
    var resetEmail by remember(username) { mutableStateOf(username) }
    LaunchedEffect(notice, error) {
        (error ?: notice)?.let { android.widget.Toast.makeText(context, it, android.widget.Toast.LENGTH_SHORT).show() }
        if (notice != null && !loginViewModel.showRegisterDialog && !loginViewModel.showForgotPasswordDialog) loginViewModel.clearNotice()
    }
    val fieldShape = RoundedCornerShape(12.dp)
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = modifier.fillMaxSize().background(Color(0xFFF9F9FF))
            .windowInsetsPadding(WindowInsets.safeDrawing)
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.fillMaxWidth().widthIn(max = 448.dp).height(64.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { loginViewModel.onBackClick(); onBackClick() }, modifier = Modifier.size(34.dp)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "رجوع", tint = SlateLight)
            }
            Row(
                Modifier.background(FinancialGreenContainer, CircleShape)
                    .border(1.dp, EmeraldGreen.copy(alpha = .25f), CircleShape)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(6.dp).background(EmeraldGreen, CircleShape))
                Spacer(Modifier.width(6.dp))

            }
            Box {
                Surface(onClick = { languageMenuExpanded = true }, shape = CircleShape, color = GoldLight, border = BorderStroke(1.dp, GoldPrimary.copy(alpha = .4f))) {
                    Text(
                        when (selectedLanguage) { "en" -> "◎ English"; "fr" -> "◎ Français"; else -> "◎ العربية" },
                        Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                        fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldDark
                    )
                }
                DropdownMenu(expanded = languageMenuExpanded, onDismissRequest = { languageMenuExpanded = false }) {
                    listOf("ar" to "العربية", "en" to "English", "fr" to "Français").forEach { (code, label) ->
                        DropdownMenuItem(
                            text = { Text(label, fontWeight = if (selectedLanguage == code) FontWeight.Bold else FontWeight.Normal) },
                            onClick = { languageMenuExpanded = false; onSelectLanguage(code) },
                            trailingIcon = { if (selectedLanguage == code) Icon(Icons.Default.Check, contentDescription = null) }
                        )
                    }
                }
            }
        }
        Image(
            painter = painterResource(id = R.drawable.ic_smart_cost_logo),
            contentDescription = "الشعار الرسمي لتطبيق سمارت كوست برو",
            modifier = Modifier.fillMaxWidth().widthIn(max = 448.dp).height(230.dp),
            contentScale = ContentScale.Fit
        )
        Text(stringResource(R.string.auth_app_tagline), fontSize = 11.sp, color = TextSecondaryDark, textAlign = TextAlign.Center)
        Spacer(Modifier.height(6.dp))
        Surface(shape = CircleShape, color = SurfaceWhite, border = BorderStroke(1.dp, BorderLight), shadowElevation = 2.dp) {
            Row(Modifier.padding(horizontal = 10.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, null, Modifier.size(12.dp), tint = GoldPrimary)
                Spacer(Modifier.width(6.dp))
                Text("eng.mamdouh saad", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SlateLight)
            }
        }
        Spacer(Modifier.height(16.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().widthIn(max = 448.dp),
            shape = RoundedCornerShape(16.dp),
            color = SurfaceWhite,
            border = BorderStroke(1.dp, BorderLight.copy(alpha = .6f)),
            shadowElevation = 12.dp
        ) {
            Column(Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, null, Modifier.size(15.dp), tint = GoldDark)
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.auth_input_user_label), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateLight)
                }
                Spacer(Modifier.height(3.dp))
                OutlinedTextField(
                    value = username, onValueChange = loginViewModel::onUsernameChange,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp), singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    placeholder = { Text(stringResource(R.string.auth_input_user_placeholder), fontSize = 11.sp) },
                    leadingIcon = { Icon(Icons.Default.Person, null, Modifier.size(18.dp), tint = TextTertiaryDark) },
                    textStyle = MaterialTheme.typography.bodySmall.copy(color = DeepSlate, textDirection = TextDirection.Ltr),
                    shape = fieldShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary, unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard
                    )
                )
                Spacer(Modifier.height(7.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, null, Modifier.size(15.dp), tint = GoldDark)
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(R.string.auth_input_password_label), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateLight)
                }
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = password, onValueChange = loginViewModel::onPasswordChange,
                    modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp), singleLine = true,
                    placeholder = { Text("••••••••••••", fontSize = 12.sp) },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
                    leadingIcon = { Icon(Icons.Default.Lock, null, Modifier.size(18.dp), tint = TextTertiaryDark) },
                    trailingIcon = {
                        IconButton(onClick = { loginViewModel.onPasswordVisibilityClick() }) {
                            Icon(if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (showPassword) "إخفاء كلمة المرور" else "إظهار كلمة المرور",
                                tint = TextSecondaryDark, modifier = Modifier.size(18.dp))
                        }
                    },
                    textStyle = MaterialTheme.typography.bodySmall.copy(color = DeepSlate),
                    shape = fieldShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary, unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard
                    )
                )
                Spacer(Modifier.height(3.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { loginViewModel.onRememberDeviceChange(!rememberDevice) }) {
                        Checkbox(checked = rememberDevice, onCheckedChange = loginViewModel::onRememberDeviceChange,
                            modifier = Modifier.size(32.dp),
                            colors = CheckboxDefaults.colors(checkedColor = GoldDark))
                        Text(stringResource(R.string.auth_remember_secure_device), fontSize = 11.sp, color = TextSecondaryDark)
                    }
                    Text(stringResource(R.string.auth_forgot_password), Modifier.clickable(onClick = { loginViewModel.onForgotPasswordClick() }), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldDark)
                }
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = { activity?.let { loginViewModel.onLoginClick(it, onLoginClick) } ?: run { android.widget.Toast.makeText(context, "تعذر فتح نافذة المصادقة", android.widget.Toast.LENGTH_SHORT).show() } }, enabled = !isLoading, modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = fieldShape, colors = ButtonDefaults.buttonColors(containerColor = DeepSlate),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = GoldPrimary)
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(if (isLoading) "جارٍ التحقق..." else "تسجيل الدخول", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowBack, null, Modifier.size(17.dp), tint = GoldPrimary)
                }
                Spacer(Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HorizontalDivider(Modifier.weight(1f), color = BorderLight)
                    Text(stringResource(R.string.auth_or_quick_login), Modifier.padding(horizontal = 10.dp), fontSize = 10.sp, color = TextTertiaryDark)
                    HorizontalDivider(Modifier.weight(1f), color = BorderLight)
                }
                Spacer(Modifier.height(16.dp))
                Surface(
                    onClick = { activity?.let { loginViewModel.onBiometricClick(it, onLoginClick) } ?: run { android.widget.Toast.makeText(context, "تعذر فتح نافذة البصمة", android.widget.Toast.LENGTH_SHORT).show() } }, modifier = Modifier.fillMaxWidth(),
                    shape = fieldShape, color = GoldLight.copy(alpha = .7f),
                    border = BorderStroke(1.dp, GoldPrimary.copy(alpha = .45f))
                ) {
                    Row(Modifier.padding(horizontal = 14.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(44.dp).background(SurfaceWhite, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Shield, null, Modifier.size(24.dp), tint = GoldDark)
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text(stringResource(R.string.auth_card_biometric_title), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepSlate)
                            Text(stringResource(R.string.auth_card_biometric_desc), fontSize = 10.sp, color = TextSecondaryDark)
                        }
                        Icon(Icons.Default.ArrowBack, null, Modifier.size(15.dp), tint = GoldDark)
                    }
                }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { loginViewModel.onGoogleClick(context, onLoginClick) }, enabled = !isLoading, modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = fieldShape, border = BorderStroke(1.dp, BorderLight),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceCard)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = "شعار Google",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(stringResource(R.string.auth_btn_google), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SlateLight)
                }
            }
        }
        Spacer(Modifier.height(14.dp))
        Row(Modifier.fillMaxWidth().widthIn(max = 448.dp), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.auth_dont_have_account), fontSize = 11.sp, color = TextSecondaryDark)
            Spacer(Modifier.width(5.dp))
            Text(stringResource(R.string.auth_register_now), Modifier.clickable(onClick = { loginViewModel.onRegisterClick() }), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldDark)
        }
        Spacer(Modifier.height(10.dp))
        // Guest Login Tab with circular logo badge (كورة بها شعار البرنامج)
        Surface(
            onClick = { loginViewModel.onGuestClick(onGuestClick) },
            modifier = Modifier.fillMaxWidth().widthIn(max = 448.dp),
            shape = RoundedCornerShape(14.dp),
            color = SurfaceWhite,
            border = BorderStroke(1.dp, BorderLight),
            shadowElevation = 2.dp
        ) {
            Row(
                Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(GoldLight.copy(alpha = 0.6f), CircleShape)
                        .border(1.dp, GoldPrimary.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_smart_cost_logo),
                        contentDescription = "شعار التطبيق",
                        modifier = Modifier.size(28.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(stringResource(R.string.auth_login_as_guest), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DeepSlate)
                    Text("استعراض النظام دون الحاجة لحساب", fontSize = 10.sp, color = TextSecondaryDark)
                }
                Icon(Icons.Default.ArrowBack, null, Modifier.size(16.dp), tint = GoldDark)
            }
        }
        Spacer(Modifier.height(16.dp))
    }

    if (showRegisterDialog) {
        AlertDialog(
            onDismissRequest = { if (!isLoading) { loginViewModel.dismissDialogs(); confirmPassword = "" } },
            title = { Text(if (loginViewModel.awaitingEmailVerification) "تفعيل البريد الإلكتروني" else stringResource(R.string.create_account_title), fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (loginViewModel.awaitingEmailVerification) {
                        Text("افتح رابط التفعيل المرسل إلى " + username + " ثم اضغط تحقق.")
                        TextButton(enabled = !isLoading, onClick = loginViewModel::resendVerification) { Text("إعادة إرسال رابط التفعيل") }
                    } else {
                        Text("سيتم إنشاء الحساب باستخدام البريد الإلكتروني وكلمة المرور.")
                        OutlinedTextField(value = username, onValueChange = loginViewModel::onUsernameChange,
                            label = { Text("البريد الإلكتروني") }, singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email), modifier = Modifier.fillMaxWidth(),
                            isError = username.isNotBlank() && !android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches(),
                            trailingIcon = { if (username.isNotBlank()) Icon(
                                if (android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = "صحة صيغة البريد",
                                tint = if (android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) Color(0xFF075E52) else MaterialTheme.colorScheme.error
                            ) })
                        OutlinedTextField(value = password, onValueChange = loginViewModel::onPasswordChange,
                            label = { Text("كلمة المرور (6 أحرف على الأقل)") }, singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = confirmPassword, onValueChange = { confirmPassword = it },
                            label = { Text("تأكيد كلمة المرور") }, singleLine = true,
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password), modifier = Modifier.fillMaxWidth(),
                            isError = confirmPassword.isNotEmpty() && confirmPassword != password,
                            trailingIcon = { if (confirmPassword.isNotEmpty()) Icon(
                                if (confirmPassword == password) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = if (confirmPassword == password) "كلمتا المرور متطابقتان" else "كلمتا المرور غير متطابقتين",
                                tint = if (confirmPassword == password) Color(0xFF075E52) else MaterialTheme.colorScheme.error
                            ) })
                    }
                    if (error != null) Text(error, color = MaterialTheme.colorScheme.error)
                    if (notice != null) Text(notice, color = Color(0xFF075E52))
                }
            },
            confirmButton = {
                TextButton(enabled = !isLoading, onClick = {
                    if (loginViewModel.awaitingEmailVerification) loginViewModel.checkEmailVerification(onLoginClick)
                    else activity?.let { loginViewModel.registerAccount(confirmPassword, it, onLoginClick) }
                }) { Text(if (isLoading) "جارٍ التحقق..." else if (loginViewModel.awaitingEmailVerification) "تحقق من التفعيل" else "إنشاء الحساب") }
            },
            dismissButton = { TextButton(onClick = { loginViewModel.dismissDialogs(); confirmPassword = "" }) { Text(stringResource(R.string.action_cancel)) } }
        )
    }

    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { loginViewModel.dismissDialogs() },
            title = { Text("استعادة كلمة المرور", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("أدخل البريد المسجل لإرسال رابط استعادة من Firebase.")
                    OutlinedTextField(
                        value = resetEmail,
                        onValueChange = { resetEmail = it },
                        label = { Text(stringResource(R.string.email_label)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(enabled = !isLoading, onClick = {
                    loginViewModel.onUsernameChange(resetEmail)
                    loginViewModel.sendPasswordReset()
                }) { Text(if (isLoading) "جارٍ الإرسال..." else "إرسال رابط الاستعادة") }
            },
            dismissButton = {
                TextButton(onClick = { loginViewModel.dismissDialogs() }) { Text(stringResource(R.string.action_cancel)) }
            }
        )
    }

    if (loginViewModel.showGooglePasswordDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("تعيين كلمة مرور للحساب", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("حساب Google دخل بنجاح. يمكنك إضافة كلمة مرور لتسجيل الدخول بالبريد الإلكتروني أيضًا، وسيظل الحساب نفسه دون إنشاء حساب جديد.")
                    OutlinedTextField(
                        value = googlePassword,
                        onValueChange = { googlePassword = it },
                        label = { Text("كلمة المرور (6 أحرف على الأقل)") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = confirmGooglePassword,
                        onValueChange = { confirmGooglePassword = it },
                        label = { Text(stringResource(R.string.confirm_password_label)) },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !isLoading,
                    onClick = {
                        loginViewModel.setPasswordForGoogleAccount(googlePassword, confirmGooglePassword)
                        if (googlePassword.length >= 6 && googlePassword == confirmGooglePassword) {
                            googlePassword = ""
                            confirmGooglePassword = ""
                        }
                    }
                ) { Text(if (isLoading) "جارٍ الحفظ..." else "تعيين كلمة المرور") }
            },
        )
    }

}

@Preview(showBackground = true, locale = "ar")
@Composable
fun LoginScreenPreview() {
    SmartCostProTheme { LoginScreen() }
}
