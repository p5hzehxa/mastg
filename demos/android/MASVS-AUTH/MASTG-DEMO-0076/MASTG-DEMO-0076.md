---
platform: android
title: Uses of BiometricPrompt with Device Credential Fallback with semgrep
id: MASTG-DEMO-0076
code: [kotlin]
test: MASTG-TEST-0313
---

### Sample

This sample demonstrates the use of `BiometricPrompt` and `BiometricManager` APIs with different authenticator configurations. It shows both insecure configurations that allow fallback to device credentials (PIN, pattern, password) and secure configurations that require biometric authentication only.

Note that when using biometric authentication on Android, the user must have a PIN, pattern, or password set on the device. As stated in the [Android documentation](https://developer.android.com/identity/sign-in/biometric-auth#declare-supported-authentication-types): "To begin using an authenticator, the user needs to create a PIN, pattern, or password. If the user doesn't already have one, the biometric enrollment flow prompts them to create one."

{{ MastgTest.kt # MastgTest_reversed.java }}

### Steps

Let's run @MASTG-TOOL-0110 rules against the sample code.

{{ ../../../../rules/mastg-android-biometric-device-credential-fallback.yml }}

{{ run.sh }}

### Observation

The output shows all usages of APIs that configure biometric authentication with fallback to device credentials.

{{ output.txt }}

### Evaluation

The test fails because the output shows references to biometric authentication configurations that allow fallback to device credentials:

- Line 26-28: `canAuthenticate` is called with `BIOMETRIC_STRONG | DEVICE_CREDENTIAL`, which checks if either biometric or device credential authentication is available.
- Line 40-46: `setAllowedAuthenticators` is called with `BIOMETRIC_STRONG | DEVICE_CREDENTIAL`, which allows the user to authenticate with either biometrics or their device PIN/pattern/password.

For sensitive operations, the app should use `BIOMETRIC_STRONG` only (line 49) to enforce biometric-only authentication. Note that when using `BIOMETRIC_STRONG` alone, you must also call `setNegativeButtonText()` to provide a cancel option for the user.
