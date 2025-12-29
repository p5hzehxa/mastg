package org.owasp.mastestapp;

import android.content.Context;
import androidx.biometric.BiometricPrompt;
import androidx.biometric.BiometricManager;

public final class MastgTest {
    private final Context context;

    public MastgTest(Context context) {
        this.context = context;
    }

    public final String mastgTest() {
        StringBuilder result = new StringBuilder();

        result.append("Demonstrating BiometricPrompt confirmation configurations:\n\n");

        // FAIL: This configuration allows implicit authentication without explicit user action
        BiometricPrompt.PromptInfo implicitPromptInfo = new BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authorize Payment")
            .setSubtitle("Complete your transaction")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setConfirmationRequired(false)
            .build();
        result.append("❌ Insecure for sensitive operations: setConfirmationRequired(false)\n");
        result.append("   Authentication can succeed without explicit user interaction\n\n");

        // PASS: This configuration requires explicit user confirmation
        BiometricPrompt.PromptInfo explicitPromptInfo = new BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authorize Payment")
            .setSubtitle("Complete your transaction")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .setConfirmationRequired(true)
            .build();
        result.append("✅ Secure: setConfirmationRequired(true)\n");
        result.append("   User must explicitly confirm the authentication\n\n");

        // PASS: Default behavior requires confirmation
        BiometricPrompt.PromptInfo defaultPromptInfo = new BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authorize Payment")
            .setSubtitle("Complete your transaction")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build();
        result.append("✅ Secure: Default behavior (confirmation required)\n\n");

        result.append("Note: setConfirmationRequired(false) may be appropriate for low-risk operations\n");
        result.append("like password autofill, but for sensitive operations (payments, data access),\n");
        result.append("explicit confirmation ensures the user consciously authorizes the action.\n");
        result.append("\nSee: https://developer.android.com/identity/sign-in/biometric-auth#no-explicit-user-action\n");

        return result.toString();
    }
}
