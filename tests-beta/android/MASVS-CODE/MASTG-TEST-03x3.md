---
platform: android
title: Runtime Use of WebViewClient URL Loading Handlers
id: MASTG-TEST-03x3
apis: [WebView, WebViewClient, shouldOverrideUrlLoading, shouldInterceptRequest, Uri, getHost, getScheme, getPath]
type: [dynamic]
weakness: MASWE-0071
best-practices: []
profiles: [L1, L2]
---

## Overview

This test dynamically analyzes the runtime behavior of `WebViewClient` URL interception methods to understand how the app handles URL loading in WebViews. By hooking relevant methods at runtime, you can observe:

- Which URLs are being loaded and intercepted.
- How the app validates or filters URLs.
- Whether the app implements allowlist or denylist patterns.
- What decisions the app makes when encountering different URL schemes or domains.

This complements static analysis (@MASTG-TEST-03x1) by providing actual runtime evidence of URL handling behavior.

## Steps

1. Use @MASTG-TECH-0109 to hook the following methods while using the app and clicking on links within WebViews:
    - `shouldOverrideUrlLoading` on classes extending `WebViewClient`
    - `shouldInterceptRequest` on classes extending `WebViewClient`
    - Related [`Uri`](https://developer.android.com/reference/android/net/Uri) methods such as `getHost`, `getScheme`, or `getPath` which are typically used to inspect and validate URLs

2. Interact with the app to trigger WebView navigation, including clicking on links within WebViews.

## Observation

The output should contain:

- A list of URLs that were intercepted by `shouldOverrideUrlLoading` and `shouldInterceptRequest`.
- The return values of these methods (indicating whether the URL was allowed or blocked).
- Any URL parsing operations performed using `Uri` methods.

## Evaluation

The test case fails if the runtime analysis reveals that:

- URLs from untrusted sources are allowed to load without proper validation.
- The app does not implement URL validation or uses weak validation logic.
- Navigation to external domains is allowed without user consent or awareness.
- The implementation allows potential open redirect vulnerabilities.

Note that intercepting URL loading is not inherently insecure. The test fails only when the implementation does not properly restrict navigation to trusted content.
