---
platform: android
title: Uses of BiometricPrompt with Event-Bound Authentication with semgrep
id: MASTG-DEMO-0083
code: [kotlin]
test: MASTG-TEST-0321
---

### Sample

This sample demonstrates the use of the `BiometricPrompt` API without a `CryptoObject` for event-bound biometric authentication, which is weaker than crypto-bound authentication (with a `CryptoObject`) as it can be bypassed.

The key being generated and used with `CryptoObject` has set [`.setUserAuthenticationRequired(false)`](https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.Builder#setUserAuthenticationRequired(boolean)) which means the key is authorized to be used regardless of whether the user has been authenticated or not.

{{ ../MASTG-DEMO-0082/MastgTest.kt # ../MASTG-DEMO-0082/MastgTest_reversed.java }}

### Steps

Let's run @MASTG-TOOL-0110 rules against the sample code.

{{ ../../../../rules/mastg-android-biometric-event-bound.yml }}

{{ run.sh }}

### Observation

The output shows the usage of `BiometricPrompt.authenticate()` without using `CryptoObject` and `.setUserAuthenticationRequired(false)`.

{{ output.txt }}

### Evaluation

The test fails because the output shows both:

- Line 76: `BiometricPrompt.authenticate(PromptInfo)` is used without a `CryptoObject` and
- Line 192 `setUserAuthenticationRequired(false)` is set for key generation.

For sensitive operations, the app should use `CryptoObject` when doing biometric authentication and the key generated should have `setUserAuthenticationRequired(true)` set.
