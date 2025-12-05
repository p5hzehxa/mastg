---
platform: android
title: Uses of WebViewClient URL Loading Handlers with semgrep
id: MASTG-DEMO-0077
code: [kotlin, java]
test: MASTG-TEST-0313
---

## Sample

The following sample demonstrates how a `WebViewClient` is configured to intercept URL loading in a WebView. The `shouldOverrideUrlLoading` method is implemented to handle navigation requests, which overrides the default behavior of opening links in the default browser.

{{ MastgTest.kt # MastgTest_reversed.java }}

## Steps

Run @MASTG-TOOL-0110 rules against the sample code.

{{ ../../../../rules/mastg-android-webview-url-handlers.yml }}

{{ run.sh }}

## Observation

The output shows references to WebViewClient URL loading handlers.

{{ output.txt }}

## Evaluation

The test case identifies that the app implements a custom `WebViewClient` with URL interception logic. Review the implementation to verify:

1. **Line with `setWebViewClient`**: The WebView is configured with a custom WebViewClient.
2. **Lines with `shouldOverrideUrlLoading`**: The implementation should be reviewed to ensure URLs are properly validated against a trusted allowlist.

In this sample, the implementation does not perform any URL validation and simply logs the URL before loading it, which could allow navigation to untrusted content.
