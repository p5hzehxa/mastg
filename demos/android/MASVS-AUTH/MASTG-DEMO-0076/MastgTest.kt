package org.owasp.mastestapp

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt

// SUMMARY: This sample demonstrates insecure biometric authentication that allows fallback to device credentials.

class MastgTest(private val context: Context) {

    fun mastgTest(): String {
        val result = StringBuilder()

        // Check biometric availability
        val biometricManager = BiometricManager.from(context)

        // FAIL: [MASTG-TEST-0313] Using DEVICE_CREDENTIAL allows fallback to PIN/pattern/password
        val canAuthenticateWithFallback = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        )

        // PASS: [MASTG-TEST-0313] Using BIOMETRIC_STRONG only requires biometric authentication
        val canAuthenticateBiometricOnly = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        )

        result.append("Biometric with fallback: ${getStatusString(canAuthenticateWithFallback)}\n")
        result.append("Biometric only: ${getStatusString(canAuthenticateBiometricOnly)}\n\n")

        // Demonstrate PromptInfo configurations
        result.append("Demonstrating BiometricPrompt configurations:\n\n")

        // FAIL: [MASTG-TEST-0313] This configuration allows fallback to device credentials
        val insecurePromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setSubtitle("Verify your identity")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()
        result.append("❌ Insecure: setAllowedAuthenticators(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)\n")

        // PASS: [MASTG-TEST-0313] This configuration requires biometric authentication only
        val securePromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setSubtitle("Verify your identity")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()
        result.append("✅ Secure: setAllowedAuthenticators(BIOMETRIC_STRONG) with setNegativeButtonText()\n\n")

        // Note about BiometricPrompt requiring a password to be set
        result.append("Note: BiometricPrompt requires a secure lock screen.\n")
        result.append("When using biometrics, the user must have a PIN, pattern, or password set.\n")
        result.append("See: https://developer.android.com/identity/sign-in/biometric-auth#declare-supported-authentication-types\n")

        return result.toString()
    }

    private fun getStatusString(status: Int): String {
        return when (status) {
            BiometricManager.BIOMETRIC_SUCCESS -> "BIOMETRIC_SUCCESS"
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "BIOMETRIC_ERROR_NO_HARDWARE"
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "BIOMETRIC_ERROR_HW_UNAVAILABLE"
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "BIOMETRIC_ERROR_NONE_ENROLLED"
            BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED -> "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED"
            BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED -> "BIOMETRIC_ERROR_UNSUPPORTED"
            BiometricManager.BIOMETRIC_STATUS_UNKNOWN -> "BIOMETRIC_STATUS_UNKNOWN"
            else -> "Unknown status: $status"
        }
    }
}
