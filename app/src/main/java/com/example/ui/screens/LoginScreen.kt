package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
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
    onLanguageClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = viewModel()
) {
    val username = loginViewModel.username
    val password = loginViewModel.password
    val showPassword = loginViewModel.showPassword
    val rememberDevice = loginViewModel.rememberDevice
    val context = androidx.compose.ui.platform.LocalContext.current
    val notice = loginViewModel.notice
    val error = loginViewModel.errorMessage
    LaunchedEffect(notice, error) {
        (error ?: notice)?.let { android.widget.Toast.makeText(context, it, android.widget.Toast.LENGTH_SHORT).show() }
        if (notice != null) loginViewModel.clearNotice()
    }
    val fieldShape = RoundedCornerShape(12.dp)
    Column(
        modifier = modifier.fillMaxSize().background(Color(0xFFF9F9FF))
            .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { loginViewModel.onBackClick(); onBackClick() }, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.ArrowForward, contentDescription = "رجوع", tint = SlateLight)
            }
            Row(
                Modifier.background(FinancialGreenContainer, CircleShape)
                    .border(1.dp, EmeraldGreen.copy(alpha = .25f), CircleShape)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(Modifier.size(8.dp).background(EmeraldGreen, CircleShape))
                Spacer(Modifier.width(6.dp))
                Text("نظام مشفر آمن", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF047857))
            }
            Surface(onClick = { loginViewModel.onLanguageClick() }, shape = CircleShape, color = GoldLight, border = BorderStroke(1.dp, GoldPrimary.copy(alpha = .4f))) {
                Text("◎ EN", Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldDark)
            }
        }
        Image(
            painter = painterResource(id = R.drawable.ic_smart_cost_logo),
            contentDescription = "الشعار الرسمي لتطبيق سمارت كوست برو",
            modifier = Modifier.fillMaxWidth().heightIn(max = 208.dp).height(150.dp),
            contentScale = ContentScale.Fit
        )
        Text("منظومة الإدارة والتكاليف التنفيذية الذكية", fontSize = 11.sp, color = TextSecondaryDark, textAlign = TextAlign.Center)
        Spacer(Modifier.height(8.dp))
        Surface(shape = CircleShape, color = SurfaceWhite, border = BorderStroke(1.dp, BorderLight), shadowElevation = 2.dp) {
            Row(Modifier.padding(horizontal = 12.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, null, Modifier.size(12.dp), tint = GoldPrimary)
                Spacer(Modifier.width(6.dp))
                Text("eng.mamdouh saad", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SlateLight)
            }
        }
        Spacer(Modifier.height(20.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().widthIn(max = 448.dp),
            shape = RoundedCornerShape(24.dp),
            color = SurfaceWhite,
            border = BorderStroke(1.dp, BorderLight.copy(alpha = .6f)),
            shadowElevation = 12.dp
        ) {
            Column(Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, null, Modifier.size(15.dp), tint = GoldDark)
                    Spacer(Modifier.width(6.dp))
                    Text("اسم المستخدم أو البريد الإلكتروني", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateLight)
                }
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = username, onValueChange = loginViewModel::onUsernameChange,
                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                    placeholder = { Text("مثال: CEO@smartcost.pro أو 29401", fontSize = 11.sp) },
                    leadingIcon = { Icon(Icons.Default.Person, null, Modifier.size(18.dp), tint = TextTertiaryDark) },
                    textStyle = MaterialTheme.typography.bodySmall.copy(color = DeepSlate),
                    shape = fieldShape,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GoldPrimary, unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard
                    )
                )
                Spacer(Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Lock, null, Modifier.size(15.dp), tint = GoldDark)
                    Spacer(Modifier.width(6.dp))
                    Text("كلمة المرور المشفرة", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SlateLight)
                }
                Spacer(Modifier.height(6.dp))
                OutlinedTextField(
                    value = password, onValueChange = loginViewModel::onPasswordChange,
                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                    placeholder = { Text("••••••••••••", fontSize = 12.sp) },
                    visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                Spacer(Modifier.height(10.dp))
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.clickable { loginViewModel.onRememberDeviceChange(!rememberDevice) }) {
                        Checkbox(checked = rememberDevice, onCheckedChange = loginViewModel::onRememberDeviceChange,
                            modifier = Modifier.size(32.dp),
                            colors = CheckboxDefaults.colors(checkedColor = GoldDark))
                        Text("تذكر هذا الجهاز الآمن", fontSize = 11.sp, color = TextSecondaryDark)
                    }
                    Text("استعادة الرمز؟", Modifier.clickable(onClick = { loginViewModel.onForgotPasswordClick() }), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldDark)
                }
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = { loginViewModel.onLoginClick(onLoginClick) }, modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = fieldShape, colors = ButtonDefaults.buttonColors(containerColor = DeepSlate),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Text("تسجيل الدخول", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(Modifier.width(8.dp))
                    Icon(Icons.Default.ArrowBack, null, Modifier.size(17.dp), tint = GoldPrimary)
                }
                Spacer(Modifier.height(20.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    HorizontalDivider(Modifier.weight(1f), color = BorderLight)
                    Text("أو تسجيل الدخول السريع", Modifier.padding(horizontal = 10.dp), fontSize = 10.sp, color = TextTertiaryDark)
                    HorizontalDivider(Modifier.weight(1f), color = BorderLight)
                }
                Spacer(Modifier.height(16.dp))
                Surface(
                    onClick = { loginViewModel.onBiometricClick() }, modifier = Modifier.fillMaxWidth(),
                    shape = fieldShape, color = GoldLight.copy(alpha = .7f),
                    border = BorderStroke(1.dp, GoldPrimary.copy(alpha = .45f))
                ) {
                    Row(Modifier.padding(horizontal = 14.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(32.dp).background(SurfaceWhite, RoundedCornerShape(8.dp)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Shield, null, Modifier.size(19.dp), tint = GoldDark)
                        }
                        Spacer(Modifier.width(10.dp))
                        Column(Modifier.weight(1f)) {
                            Text("الدخول السريع عبر Face ID / البصمة", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = DeepSlate)
                            Text("التحقق البيومتري المعتمد", fontSize = 10.sp, color = TextSecondaryDark)
                        }
                        Icon(Icons.Default.ArrowBack, null, Modifier.size(15.dp), tint = GoldDark)
                    }
                }
                Spacer(Modifier.height(10.dp))
                OutlinedButton(
                    onClick = { loginViewModel.onGoogleClick() }, modifier = Modifier.fillMaxWidth().height(48.dp),
                    shape = fieldShape, border = BorderStroke(1.dp, BorderLight),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = SurfaceCard)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google_logo),
                        contentDescription = "شعار Google",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text("متابعة الدخول بحساب Google", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SlateLight)
                }
                Spacer(Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text("ليس لديك حساب؟", fontSize = 11.sp, color = TextSecondaryDark)
                    Spacer(Modifier.width(5.dp))
                    Text("سجل الآن", Modifier.clickable(onClick = { loginViewModel.onRegisterClick() }), fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GoldDark)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("الدخول كزائر لتصفح النظام", Modifier.clickable(onClick = { loginViewModel.onGuestClick(onGuestClick) }).padding(8.dp),
            fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSecondaryDark, textAlign = TextAlign.Center)
        Spacer(Modifier.height(16.dp))
    }
}

@Preview(showBackground = true, locale = "ar")
@Composable
fun LoginScreenPreview() {
    SmartCostProTheme { LoginScreen() }
}
