---
platform: android
title: Testing Enforced Updating
id: MASTG-TEST-0x80-1
type: [dynamic]
weakness: MASWE-0075
profiles: [L2]
---

## Overview

This test verifies whether the app enforces an update (@MASTG-KNOW-0023) when directed by the backend. In a backend-gated flow, the app typically sends the current app version (for example, via `BuildConfig.VERSION_NAME`/`BuildConfig.VERSION_CODE`) and receives a response indicating whether the version is supported. Alternatively, the app may use the [Google Play In-App Updates APIs](https://developer.android.com/guide/playcore/in-app-updates) for an immediate update (for example, [`AppUpdateManager`](https://developer.android.com/reference/com/google/android/play/core/appupdate/AppUpdateManager)).

## Steps

1. Apply @MASTG-TECH-0011 (MITM) to capture launch traffic and initial API calls. Filter for headers, parameters, or body fields carrying version information (for example, `X-App-Version`, `version`, `build`, `minVersion`).
2. Use @MASTG-TECH-0033 (dynamic instrumentation) to hook relevant classes or methods that retrieve the app version (such as `BuildConfig.VERSION_NAME`) or that are specifically related to update flows (for example, `AppUpdateManager#getAppUpdateInfo`, `AppUpdateManager#startUpdateFlowForResult`, or the code that evaluates `minVersion`).

## Observation

The output should contain:

- a network traffic trace showing version values in requests and corresponding backend responses for different versions
- a method trace showing which APIs were called

## Evaluation

The test case fails if the app does not implement enforced updating. For example, if it neither uses the Play In-App Updates API nor performs backend-gated version checks or if it implements them incorrectly.

**Additional Verification:**

Validate whether the backend indicates that an update is required but the app still allows you to continue using it (this may require manual testing). For example:

- Try to dismiss any update prompts or navigate around them.
- Modify requests to present an older version (for example, change `version`/`build`), replay the request, and observe whether the backend response changes (for example, an error or a field indicating an update is required).
