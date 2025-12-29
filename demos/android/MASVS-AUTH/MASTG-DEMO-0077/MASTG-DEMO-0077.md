---
platform: android
title: Uses of BiometricPrompt without Explicit User Confirmation with semgrep
id: MASTG-DEMO-0077
code: [kotlin]
test: MASTG-TEST-0316
---

### Sample

This sample demonstrates the use of `BiometricPrompt.PromptInfo.Builder` with `setConfirmationRequired()` method. It shows both insecure configurations that allow implicit authentication without explicit user action and secure configurations that require explicit confirmation.

When `setConfirmationRequired(false)` is used, passive biometrics (like face recognition) can authenticate the user as soon as the device detects their biometric data, without requiring them to tap a confirmation button. According to the [Android documentation](https://developer.android.com/identity/sign-in/biometric-auth#no-explicit-user-action): "A false value for `setConfirmationRequired()` is intended to be used in cases where the user's intent is obvious, such as autofilling a password. In cases where security is more important, such as making a purchase, set this value to true."

{{ MastgTest.kt # MastgTest_reversed.java }}

### Steps

Let's run @MASTG-TOOL-0110 rules against the sample code.

{{ ../../../../rules/mastg-android-biometric-no-confirmation-required.yml }}

{{ run.sh }}

### Observation

The output shows the usage of API that configures biometric authentication without requiring explicit user confirmation.

{{ output.txt }}

### Evaluation

The test fails because the output shows a reference to biometric authentication configuration that disables explicit user confirmation:

- Line 25: `setConfirmationRequired(false)` is called, which allows the authentication to succeed implicitly without the user actively confirming the action. This is used in the context of authorizing a payment, which is a sensitive operation.

For sensitive operations like payments or data access, the app should use `setConfirmationRequired(true)` (line 36) or rely on the default behavior (line 46) to ensure the user explicitly confirms the authentication. For low-risk operations like password autofill where the user's intent is clear, using `setConfirmationRequired(false)` may be appropriate.
