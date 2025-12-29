---
platform: android
title: References to APIs Enforcing Authentication without Explicit User Action
id: MASTG-TEST-0316
apis: [BiometricPrompt.PromptInfo.Builder, setConfirmationRequired]
type: [static]
weakness: MASWE-0044
profiles: [L2]
---

## Overview

This test checks if the app enforces biometric authentication without requiring explicit user action. When using [`BiometricPrompt`](https://developer.android.com/reference/androidx/biometric/BiometricPrompt), the [`setConfirmationRequired()`](https://developer.android.com/reference/androidx/biometric/BiometricPrompt.PromptInfo.Builder#setConfirmationRequired(boolean)) method in [`BiometricPrompt.PromptInfo.Builder`](https://developer.android.com/reference/androidx/biometric/BiometricPrompt.PromptInfo.Builder) controls whether the user must explicitly confirm their authentication.

According to the [Android documentation](https://developer.android.com/identity/sign-in/biometric-auth#no-explicit-user-action):

- **setConfirmationRequired(true)** (default): The user must explicitly confirm their authentication by interacting with the prompt (e.g., tapping a button). This ensures the user consciously authorizes the action.

- **setConfirmationRequired(false)**: The system may authenticate the user implicitly without requiring explicit interaction. For example, with passive biometrics like face recognition, the authentication can succeed as soon as the device detects the user's face, without the user needing to tap a confirmation button.

Using `setConfirmationRequired(false)` can be appropriate for low-risk operations (e.g., auto-filling passwords), but for sensitive operations (e.g., payments, data access), explicit confirmation provides an additional layer of security by ensuring the user intentionally authorizes the action.

The Android documentation states: "A false value for `setConfirmationRequired()` is intended to be used in cases where the user's intent is obvious, such as autofilling a password. In cases where security is more important, such as making a purchase, set this value to true."

## Steps

1. Run @MASTG-TECH-0014 with a tool such as @MASTG-TOOL-0110 on the app binary to look for uses of `BiometricPrompt.PromptInfo.Builder` with `setConfirmationRequired(false)`.

## Observation

The output should contain a list of locations where biometric authentication is configured without explicit user confirmation.

## Evaluation

The test fails if the app uses `BiometricPrompt.PromptInfo.Builder` with `setConfirmationRequired(false)` for sensitive operations that require explicit user authorization.

The test passes if the app either:

- Uses `setConfirmationRequired(true)` explicitly for sensitive operations, or
- Relies on the default behavior (which requires confirmation).

**Note:** Using `setConfirmationRequired(false)` is not inherently a vulnerability. It may be appropriate for low-risk operations like password autofill where the user's intent is clear. However, for high-security operations (e.g., financial transactions, sensitive data access), requiring explicit confirmation helps ensure the user consciously authorizes the action and reduces the risk of unauthorized access through passive authentication.
