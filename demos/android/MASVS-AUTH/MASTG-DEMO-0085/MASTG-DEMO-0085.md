---
platform: android
title: Uses of BiometricPrompt without Explicit User Confirmation with semgrep
id: MASTG-DEMO-0085
code: [kotlin]
test: MASTG-TEST-0316
---

### Sample

This sample demonstrates the use of `BiometricPrompt.PromptInfo.Builder` with `setConfirmationRequired()` method. It shows both insecure configurations that allow implicit authentication without explicit user action and secure configurations that require explicit confirmation.

When `setConfirmationRequired(false)` is used, passive biometrics (like face recognition) can authenticate the user as soon as the device detects their biometric data, without requiring them to tap a confirmation button.

{{ ../MASTG-DEMO-0082/MastgTest.kt # ../MASTG-DEMO-0082/MastgTest_reversed.java }}

### Steps

Let's run @MASTG-TOOL-0110 rules against the sample code.

{{ ../../../../rules/mastg-android-biometric-no-confirmation-required.yml }}

{{ run.sh }}

### Observation

The output shows the usage of API that configures biometric authentication without requiring explicit user confirmation.

{{ output.txt }}

### Evaluation

The test fails because the output shows two references to biometric authentication configuration that disables explicitly user confirmation:

- Line 90 and 181: `setConfirmationRequired(false)` is called, which allows the authentication to succeed implicitly without the user actively confirming the action.

For sensitive operations like payments or data access, the app should use `setConfirmationRequired(true)` or rely on the default behavior to [ensure the user explicitly confirms the authentication](https://developer.android.com/identity/sign-in/biometric-auth#no-explicit-user-action. For low-risk operations like password autofill where the user's intent is clear, using `setConfirmationRequired(false)` may be appropriate.
