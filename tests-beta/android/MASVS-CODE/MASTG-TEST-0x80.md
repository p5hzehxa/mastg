---
platform: android
title: Testing Enforced Updating
id: MASTG-TEST-0x80
type: [static]
weakness: MASWE-0075
profiles: [L2]
---

## Overview

This test verifies whether the app enforces an update (@MASTG-KNOW-0023) when directed by the backend. The app should either send its current version to the backend or retrieve the minimum supported version and prevent usage until the app is updated.

On Android, enforced updates are commonly implemented using the Google Play In-App Updates API or a custom backend-gated flow that evaluates the app version retrieved via `BuildConfig.VERSION_NAME`/`BuildConfig.VERSION_CODE` (or `PackageInfo` via `PackageManager`).

Specifically, look for:

- Google Play In-App Updates classes and methods: `AppUpdateManagerFactory.create`, `AppUpdateManager#getAppUpdateInfo`, `UpdateAvailability.UPDATE_AVAILABLE`, `AppUpdateType.IMMEDIATE`/`FLEXIBLE`, `startUpdateFlowForResult`/`requestUpdateFlow`.
- Version retrieval points: `BuildConfig.VERSION_NAME`, `BuildConfig.VERSION_CODE` (or `PackageInfo` via `PackageManager`).
- Strings like `X-App-Version`, `version`, `minVersion` that may indicate version checks in network requests or other parts of the code.

## Steps

1. Apply @MASTG-TECH-0014 (static analysis) and search for Android update/version APIs used before authentication (for example, in `Application.onCreate`, splash/bootstrap flows, or initial `Activity.onCreate`).

## Observation

The output should contain a list of code locations where the app retrieves or sends its version (for example, `BuildConfig.VERSION_NAME` or `PackageInfo`) and uses the Google Play In-App Updates APIs (for example, `AppUpdateManager`, `startUpdateFlowForResult`), or evaluates a backend `minVersion` response, along with a call graph snippet showing these checks execute before authentication.

## Evaluation

The test case fails if no code paths implement an enforced update before authentication, if the identified logic is not reachable prior to authentication, or if the app displays a mandatory update message but still allows you to continue using the app (for example, dismissing the dialog or navigating around it).

Note that this evaluation requires manual review of the identified code paths in the reverse engineered code to confirm whether they implement enforced updating correctly.

For example, you should try to trace the control flow to confirm that version evaluation leads to an enforced update path, such as:
    - Immediate update flow (`AppUpdateType.IMMEDIATE`), or
    - A custom blocking UI (for example, a full-screen dialog/`Activity` that disables navigation) when backend `minVersion` > current version.

Alternatively, you can use dynamic analysis (see @MASTG-TEST-0x80-1) to confirm the identified code paths execute before authentication and enforce updating as expected.
