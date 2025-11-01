---
masvs_category: MASVS-PLATFORM
platform: android
title: Enforced Updating
---

Starting with Android 5.0 (API level 21), developers can implement enforced updates using the Play In‑App Updates API (Play Core). See the official documentation: [In‑App Updates](https://developer.android.com/guide/playcore/in-app-updates "In‑App Updates"). This mechanism is far more reliable than legacy methods such as scraping Play Store pages or calling undocumented endpoints, which are unstable and unsupported.

Enforced updating can be particularly useful for maintaining security when public key pins need to be rotated or when critical vulnerabilities must be patched quickly. Requiring users to install an updated version ensures that old, insecure builds are no longer active in the field.

Keep in mind that updating the app does not resolve vulnerabilities residing on backend systems. A secure update mechanism should complement proper API and service lifecycle management. Similarly, if users are not forced to update, test older app versions against your backend and apply API versioning and deprecation policies to maintain security and stability across all supported releases.

## Google Play In‑App Updates API

The [Play In‑App Updates API](https://developer.android.com/guide/playcore/in-app-updates) (Play Core) is part of the Google Play ecosystem and exposes the [`AppUpdateManager`](https://developer.android.com/reference/com/google/android/play/core/appupdate/AppUpdateManager "AppUpdateManager") class, which lets apps check for available updates and initiate update flows directly within the app.

The API supports two primary modes:

- **Immediate updates**, which require the user to update before using the app further.
- **Flexible updates**, which allow users to continue using the app while the update downloads in the background.

Use `startUpdateFlowForResult(...)` with [`AppUpdateOptions`](https://developer.android.com/reference/com/google/android/play/core/appupdate/AppUpdateOptions "AppUpdateOptions") or an `ActivityResultLauncher`. Typical code paths evaluate [`UpdateAvailability`](https://developer.android.com/reference/com/google/android/play/core/install/model/UpdateAvailability "UpdateAvailability") and select an [`AppUpdateType`](https://developer.android.com/reference/com/google/android/play/core/install/model/AppUpdateType "AppUpdateType") (for example, `IMMEDIATE` vs. `FLEXIBLE`).

## Custom Backend-Gated Flows

For **apps distributed outside Google Play**, developers must design custom mechanisms to check for updates, such as querying a self‑hosted update API or leveraging distribution frameworks like Firebase Remote Config to enforce minimum version requirements. See this [blog post](https://medium.com/@sembozdemir/force-your-users-to-update-your-app-with-using-firebase-33f1e0bcec5a "Force users to update with Firebase") for an example of using Firebase for forced updates.

Practical guidance for backend‑gated flows:

- Include the app version (for example, `X-App-Version`, `version`, or `build`) in early requests and have the backend return a `minVersion` (or equivalent) policy.
- Enforce the policy on the client by presenting a blocking UI (for example, a non‑dismissible dialog or gating screen) and disabling navigation until the update is completed.
- Consider integrity and tamper resistance: avoid trusting only client‑provided data, sign responses where appropriate, and handle offline scenarios (for example, cache policy with a reasonable TTL and a safe fallback).
