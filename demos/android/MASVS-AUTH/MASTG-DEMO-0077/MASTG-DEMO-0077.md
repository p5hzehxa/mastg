---
platform: android
title: References to APIs for Event-Bound Biometric Authentication
id: MASTG-DEMO-0077
code: [kotlin]
test: MASTG-TEST-0314
---

### Sample

This sample demonstrates the use of the `BiometricPrompt` API in an event-bound manner (without a `CryptoObject`) and a crypto-bound manner (with a `CryptoObject`) to implement biometric authentication.

This demo uses the same sample as @MASTG-DEMO-0076.

{{ ../MASTG-DEMO-0076/MastgTest.kt # ../MASTG-DEMO-0076/MastgTest_reversed.java }}

### Steps

Let's run @MASTG-TOOL-0110 rules against the sample code.

{{ ../../../../rules/mastg-android-biometric-event-bound.yml }}

{{ run.sh }}

### Observation

The output shows all usages of `BiometricPrompt.authenticate()` and indicates whether a `CryptoObject` is used.

{{ output.txt }}

### Evaluation

The test fails if sensitive operations use `BiometricPrompt.authenticate(PromptInfo)` without a `CryptoObject`, or if there is no evidence of keys generated with `setUserAuthenticationRequired(true)` being used in conjunction with biometric authentication.

The test passes if the app uses `BiometricPrompt.authenticate(PromptInfo, CryptoObject)` for sensitive operations with keys stored in the Android KeyStore configured with `setUserAuthenticationRequired(true)`.
