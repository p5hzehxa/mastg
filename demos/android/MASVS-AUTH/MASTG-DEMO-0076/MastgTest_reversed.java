package org.owasp.mastestapp;

import android.content.Context;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

public final class MastgTest {
    private final Context context;

    public MastgTest(Context context) {
        this.context = context;
    }

    public final String mastgTest() {
        StringBuilder result = new StringBuilder();
        BiometricManager biometricManager = BiometricManager.from(this.context);

        // FAIL: Using DEVICE_CREDENTIAL allows fallback to PIN/pattern/password
        int canAuthenticateWithFallback = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL
        );

        // PASS: Using BIOMETRIC_STRONG only requires biometric authentication
        int canAuthenticateBiometricOnly = biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG
        );

        result.append("Biometric with fallback: " + getStatusString(canAuthenticateWithFallback) + "\n");
        result.append("Biometric only: " + getStatusString(canAuthenticateBiometricOnly) + "\n\n");
        result.append("Demonstrating BiometricPrompt configurations:\n\n");

        // FAIL: This configuration allows fallback to device credentials
        BiometricPrompt.PromptInfo insecurePromptInfo = new BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setSubtitle("Verify your identity")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG | BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build();
        result.append("❌ Insecure: setAllowedAuthenticators(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)\n");

        // PASS: This configuration requires biometric authentication only
        BiometricPrompt.PromptInfo securePromptInfo = new BiometricPrompt.PromptInfo.Builder()
            .setTitle("Authenticate")
            .setSubtitle("Verify your identity")
            .setNegativeButtonText("Cancel")
            .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG)
            .build();
        result.append("✅ Secure: setAllowedAuthenticators(BIOMETRIC_STRONG) with setNegativeButtonText()\n\n");

        result.append("Note: BiometricPrompt requires a secure lock screen.\n");
        result.append("When using biometrics, the user must have a PIN, pattern, or password set.\n");
        result.append("See: https://developer.android.com/identity/sign-in/biometric-auth#declare-supported-authentication-types\n");

        return result.toString();
    }

    private final String getStatusString(int status) {
        switch (status) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                return "BIOMETRIC_SUCCESS";
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                return "BIOMETRIC_ERROR_NO_HARDWARE";
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                return "BIOMETRIC_ERROR_HW_UNAVAILABLE";
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                return "BIOMETRIC_ERROR_NONE_ENROLLED";
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                return "BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED";
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                return "BIOMETRIC_ERROR_UNSUPPORTED";
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                return "BIOMETRIC_STATUS_UNKNOWN";
            default:
                return "Unknown status: " + status;
        }
    }
}
