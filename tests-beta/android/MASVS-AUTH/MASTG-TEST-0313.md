---
platform: android
title: References to APIs Allowing Fallback to Non-Biometric Authentication
id: MASTG-TEST-0313
apis: [BiometricPrompt, BiometricManager.Authenticators, setAllowedAuthenticators]
type: [static]
weakness: MASWE-0045
profiles: [L2]
---

## Overview

This test checks if the app uses biometric authentication mechanisms that allow fallback to device credentials (PIN, pattern, or password) for sensitive operations. On Android, the [`BiometricPrompt`](https://developer.android.com/reference/androidx/biometric/BiometricPrompt) API can be configured to accept different types of authenticators via [`setAllowedAuthenticators`](https://developer.android.com/reference/androidx/biometric/BiometricPrompt.PromptInfo.Builder#setAllowedAuthenticators(int)) in [`BiometricPrompt.PromptInfo.Builder`](https://developer.android.com/reference/androidx/biometric/BiometricPrompt.PromptInfo.Builder).

The following authenticator types are available in [`BiometricManager.Authenticators`](https://developer.android.com/reference/androidx/biometric/BiometricManager.Authenticators):

- `BIOMETRIC_STRONG`: Class 3 biometric authentication (e.g., fingerprint, face)
- `BIOMETRIC_WEAK`: Class 2 biometric authentication
- `DEVICE_CREDENTIAL`: Device credentials (PIN, pattern, password)

When `DEVICE_CREDENTIAL` is included (either alone or combined with biometric authenticators using the bitwise OR operator `|`), the authentication allows fallback to device credentials, which is considered weaker than requiring biometrics alone because passcodes are more susceptible to compromise (e.g., through shoulder surfing).

Similarly, using [`setDeviceCredentialAllowed(true)`](https://developer.android.com/reference/androidx/biometric/BiometricPrompt.PromptInfo.Builder#setDeviceCredentialAllowed(boolean)) (deprecated since API 30) also enables fallback to device credentials.

## Steps

1. Run @MASTG-TECH-0014 with a tool such as @MASTG-TOOL-0110 on the app binary to look for uses of `BiometricPrompt.PromptInfo.Builder` with `setAllowedAuthenticators` including `DEVICE_CREDENTIAL` or `setDeviceCredentialAllowed(true)`.

## Observation

The output should contain a list of locations where biometric authentication is configured with fallback to device credentials.

## Evaluation

The test fails if the app uses `BiometricPrompt` with authenticators that include `DEVICE_CREDENTIAL` for any sensitive data resource that needs protection.

The test passes only if the app uses `BiometricPrompt` with `BIOMETRIC_STRONG` only (without `DEVICE_CREDENTIAL`) to enforce biometric-only access for any sensitive data resource that needs protection.

**Note:** Using `DEVICE_CREDENTIAL` is not inherently a vulnerability, but in high-security applications (e.g., finance, government, health), its use can represent a weakness that reduces the intended security posture. This issue is better categorized as a security weakness or hardening issue, not a critical vulnerability.
