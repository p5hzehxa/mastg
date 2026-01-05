---
platform: android
title: References to APIs Detecting Biometric Enrollment Changes
id: MASTG-TEST-0315
apis: [KeyGenParameterSpec.Builder, setInvalidatedByBiometricEnrollment]
type: [static]
weakness: MASWE-0046
profiles: [L2]
---

## Overview

This test checks whether the app fails to protect sensitive operations against unauthorized access following biometric enrollment changes. An attacker who obtains the device passcode could add a new fingerprint via system settings and use it to authenticate in the app if keys are not properly invalidated.

On Android, when generating cryptographic keys for use with biometric authentication, developers can use [`KeyGenParameterSpec.Builder.setInvalidatedByBiometricEnrollment(boolean)`](https://developer.android.com/reference/android/security/keystore/KeyGenParameterSpec.Builder#setInvalidatedByBiometricEnrollment(boolean)) to control whether the key should be invalidated when new biometrics are enrolled. By default, a key becomes permanently invalidated if a new biometric is enrolled.

The test identifies if `setInvalidatedByBiometricEnrollment(false)` is set when keys are generated. This allows newly enrolled biometrics to authenticate with existing keys, which may be a security risk if an attacker gains access to enroll their biometrics.

## Steps

1. Run @MASTG-TECH-0014 with a tool such as @MASTG-TOOL-0110 on the app binary to look for uses of `KeyGenParameterSpec.Builder` and check if `setInvalidatedByBiometricEnrollment(false)` is called.

## Observation

The output should contain a list of locations where cryptographic key generation is configured, indicating the value of `setInvalidatedByBiometricEnrollment`.

## Evaluation

The test fails if the app uses `setInvalidatedByBiometricEnrollment(false)` for keys used to protect sensitive data resources. 

The test passes if the app either:

- Uses `setInvalidatedByBiometricEnrollment(true)` explicitly, or
- Relies on the default behavior, which invalidates keys on new biometric enrollment when `setUserAuthenticationRequired(true)` is set.
