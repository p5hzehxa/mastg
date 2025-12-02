---
platform: android
title: References to APIs for Event-Bound Biometric Authentication
id: MASTG-TEST-0314
apis: [BiometricPrompt, BiometricPrompt.CryptoObject, authenticate]
type: [static]
weakness: MASWE-0044
profiles: [L2]
---

## Overview

This test checks if the app uses biometric authentication in an event-bound manner, where authentication success relies solely on a callback result rather than being cryptographically bound to sensitive operations.

On Android, [`BiometricPrompt.authenticate()`](https://developer.android.com/reference/androidx/biometric/BiometricPrompt#authenticate(androidx.biometric.BiometricPrompt.PromptInfo)) can be called with or without a [`CryptoObject`](https://developer.android.com/reference/androidx/biometric/BiometricPrompt.CryptoObject):

- **Without CryptoObject** (event-bound): The app relies on the `onAuthenticationSucceeded` callback to determine if authentication was successful. This approach is vulnerable to bypass because an attacker can use runtime hooking (e.g., with @MASTG-TOOL-0001) to invoke the callback directly without actual biometric verification.

- **With CryptoObject** (crypto-bound): The app passes a cryptographic object (e.g., `Cipher`, `Signature`, `Mac`) that requires user authentication. The cryptographic operation can only succeed after genuine biometric authentication, making bypass significantly harder.

The recommended approach is to use `BiometricPrompt.authenticate(PromptInfo, CryptoObject)` with a key stored in the Android KeyStore that has `setUserAuthenticationRequired(true)`. This ensures that the key can only be used after successful biometric authentication, binding the authentication to a cryptographic operation.

## Steps

1. Run @MASTG-TECH-0014 with a tool such as @MASTG-TOOL-0110 on the app binary to look for uses of `BiometricPrompt.authenticate()`.
2. Analyze whether the calls include a `CryptoObject` parameter.

## Observation

The output should contain a list of locations where `BiometricPrompt.authenticate()` is called, indicating whether a `CryptoObject` is passed.

## Evaluation

The test fails if for each sensitive operation worth protecting:

- `BiometricPrompt.authenticate(PromptInfo)` is used without a `CryptoObject`.
- There are no calls to key generation with `setUserAuthenticationRequired(true)` in conjunction with biometric authentication.

The test passes if the app uses `BiometricPrompt.authenticate(PromptInfo, CryptoObject)` with properly configured cryptographic keys from the Android KeyStore for sensitive operations.
