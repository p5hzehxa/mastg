package org.owasp.mastestapp

import android.content.Context
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricManager

// SUMMARY: This sample demonstrates biometric authentication with and without explicit user confirmation.

class MastgTest(private val context: Context) {

    fun mastgTest(): String {
        val result = StringBuilder()

        result.append("Demonstrating BiometricPrompt confirmation configurations:\n\n")

        // FAIL: [MASTG-TEST-0316] This configuration allows implicit authentication without explicit user action
        val implicitPromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authorize Payment")
            .setSubtitle("Complete your transaction")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setConfirmationRequired(false)
            .build()
        result.append("❌ Insecure for sensitive operations: setConfirmationRequired(false)\n")
        result.append("   Authentication can succeed without explicit user interaction\n\n")

        // PASS: [MASTG-TEST-0316] This configuration requires explicit user confirmation
        val explicitPromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authorize Payment")
            .setSubtitle("Complete your transaction")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setConfirmationRequired(true)
            .build()
        result.append("✅ Secure: setConfirmationRequired(true)\n")
        result.append("   User must explicitly confirm the authentication\n\n")

        // PASS: [MASTG-TEST-0316] Default behavior requires confirmation
        val defaultPromptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authorize Payment")
            .setSubtitle("Complete your transaction")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build()
        result.append("✅ Secure: Default behavior (confirmation required)\n\n")

        result.append("Note: setConfirmationRequired(false) may be appropriate for low-risk operations\n")
        result.append("like password autofill, but for sensitive operations (payments, data access),\n")
        result.append("explicit confirmation ensures the user consciously authorizes the action.\n")
        result.append("\nSee: https://developer.android.com/identity/sign-in/biometric-auth#no-explicit-user-action\n")

        return result.toString()
    }
}
