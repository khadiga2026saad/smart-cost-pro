package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

/** UI-only actions; only the remember-device choice and username are persisted. */
class LoginViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = application.getSharedPreferences("smart_cost_auth_prefs", Context.MODE_PRIVATE)
    private val savedRememberDevice = preferences.getBoolean("remember_device", true)
    var username by mutableStateOf(if (savedRememberDevice) preferences.getString("saved_user", "") ?: "" else "")
        private set
    var password by mutableStateOf("")
        private set
    var showPassword by mutableStateOf(false)
        private set
    var rememberDevice by mutableStateOf(savedRememberDevice)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var notice by mutableStateOf<String?>(null)
        private set

    fun onUsernameChange(value: String) {
        username = value
        errorMessage = null
        if (rememberDevice) preferences.edit().putString("saved_user", value).apply()
    }
    fun onPasswordChange(value: String) { password = value; errorMessage = null }
    fun onPasswordVisibilityClick() { showPassword = !showPassword }
    fun onRememberDeviceChange(value: Boolean) {
        rememberDevice = value
        preferences.edit()
            .putBoolean("remember_device", value)
            .apply {
                if (value) putString("saved_user", username) else remove("saved_user")
            }
            .apply()
    }
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
        // TODO: TEMPORARY prototype behavior: non-empty fields grant entry; implement real authentication before release.
        onValid()
    }
    fun onGuestClick(onEnter: () -> Unit) {
        // TODO: Define guest access permissions.
        onEnter()
    }
    fun clearNotice() { notice = null }
}
