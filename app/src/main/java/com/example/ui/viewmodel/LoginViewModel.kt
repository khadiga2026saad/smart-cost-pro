package com.example.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/** UI-only actions. No credential verification or persistence is performed here. */
class LoginViewModel : ViewModel() {
    var username by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var showPassword by mutableStateOf(false)
        private set
    var rememberDevice by mutableStateOf(true)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var notice by mutableStateOf<String?>(null)
        private set

    fun onUsernameChange(value: String) { username = value; errorMessage = null }
    fun onPasswordChange(value: String) { password = value; errorMessage = null }
    fun onPasswordVisibilityClick() { showPassword = !showPassword }
    fun onRememberDeviceChange(value: Boolean) { rememberDevice = value }
    fun onBackClick() { /* TODO: Define back navigation behavior. */ notice = "تم الضغط على الرجوع" }
    fun onLanguageClick() { /* TODO: Define language-switch behavior. */ notice = "تغيير اللغة قريبًا" }
    fun onForgotPasswordClick() { /* TODO: Implement recovery. */ notice = "استعادة الرمز قريبًا" }
    fun onBiometricClick() { /* TODO: Implement biometric authentication. */ notice = "الدخول بالبصمة قريبًا" }
    fun onGoogleClick() { /* TODO: Implement Google authentication. */ notice = "الدخول بحساب Google قريبًا" }
    fun onRegisterClick() { /* TODO: Implement registration. */ notice = "إنشاء الحساب قريبًا" }
    fun onLoginClick(onValid: () -> Unit) {
        if (username.isBlank() || password.isBlank()) {
            errorMessage = "من فضلك أدخل اسم المستخدم وكلمة المرور"
            return
        }
        errorMessage = null
        // TODO: Replace non-empty validation with the agreed authentication policy.
        onValid()
    }
    fun onGuestClick(onEnter: () -> Unit) {
        // TODO: Define guest access permissions.
        onEnter()
    }
    fun clearNotice() { notice = null }
}
