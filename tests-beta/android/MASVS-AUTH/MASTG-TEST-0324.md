---
platform: android
title: References to APIs for Keys used in Biometric Authentication with Extended Validity Duration
id: MASTG-TEST-0324
apis: [KeyGenParameterSpec.Builder, setUserAuthenticationParameters, setUserAuthenticationValidityDurationSeconds]
type: [static]
weakness: MASWE-0044
profiles: [L2]
---

## Overview

This test checks if the app configures cryptographic keys with an extended authentication validity duration that allows keys to remain unlocked beyond the immediate operation. When using crypto-bound biometric authentication, the authentication validity duration determines how long a key remains usable after successful authentication.

On Android, developers can configure this behavior using [`setUserAuthenticationParameters(int timeout, int type)`](https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.Builder#setUserAuthenticationParameters(int,%20int)) or the deprecated [`setUserAuthenticationValidityDurationSeconds(int)`](https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.Builder#setUserAuthenticationValidityDurationSeconds(int)) when generating keys with [`KeyGenParameterSpec.Builder`](https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.Builder):

- **Duration = 0**: The key requires authentication for every cryptographic operation. This is the most secure configuration as each use of the key requires fresh biometric verification.

- **Duration > 0**: The key remains unlocked for the specified duration (in seconds) after successful authentication. During this window, the key can be used without requiring additional authentication, even if the user is no longer present.

When the duration is set to a high value (e.g., 300+ seconds), an attacker with runtime access (e.g., using @MASTG-TOOL-0001) can exploit the unlocked key window to perform unauthorized cryptographic operations without biometric verification.

## Steps

1. Run @MASTG-TECH-0014 with a tool such as @MASTG-TOOL-0110 on the app binary to look for uses of `KeyGenParameterSpec.Builder` with `setUserAuthenticationParameters` or `setUserAuthenticationValidityDurationSeconds`.
2. Identify the timeout/duration values configured for keys used with biometric authentication.

## Observation

The output should contain a list of locations where cryptographic keys are generated with authentication validity duration settings, showing the configured timeout values.

## Evaluation

The test fails if the app configures keys used for sensitive operations with:

- `setUserAuthenticationParameters(duration, type)` where duration > 0
- `setUserAuthenticationValidityDurationSeconds(duration)` where duration > 0

The test passes if the app uses `setUserAuthenticationParameters(0, type)` to require authentication for every cryptographic operation when protecting sensitive data resources or sensitive functionality.

> Note: A non-zero authentication validity duration is not inherently a vulnerability. Short durations (e.g., 5-30 seconds) may be acceptable for certain use cases where multiple related operations need to be performed in quick succession. However, for high-security applications and sensitive operations, requiring authentication per use (duration = 0) provides the strongest protection against unauthorized key usage and runtime attacks.
