---
platform: android
title: Uses of BiometricPrompt with Device Credential Fallback with semgrep
id: MASTG-DEMO-0076
code: [kotlin]
test: MASTG-TEST-0313
---

### Sample

This sample demonstrates the use of the `BiometricPrompt` API with different authenticator configurations. It shows both weaker configurations that allow fallback to device credentials (PIN, pattern, password), which are more susceptible to compromise (e.g., through shoulder surfing) and secure configurations that requires a strong biometric authentication only. 

Note that when using biometric authentication on Android, the user must have a PIN, pattern, or password set on the device as stated in the [Android documentation](https://developer.android.com/identity/sign-in/biometric-auth#declare-supported-authentication-types).

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

- Line 74: `setAllowedAuthenticators` is called with `BIOMETRIC_STRONG | DEVICE_CREDENTIAL`, which allows the user to authenticate with either biometrics or their device PIN/pattern/password.

For sensitive operations, the app should use `BIOMETRIC_STRONG` to enforce biometric-only authentication. Note that when using `BIOMETRIC_STRONG` alone, you must also call `setNegativeButtonText()` to provide a cancel option for the user.
