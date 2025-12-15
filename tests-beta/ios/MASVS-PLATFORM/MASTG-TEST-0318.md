---
platform: ios
title: References to File Access in WebViews
id: MASTG-TEST-0318
apis: [WKWebView, WKPreferences, WKWebViewConfiguration, allowFileAccessFromFileURLs, allowUniversalAccessFromFileURLs, loadFileURL, UIWebView]
type: [static]
weakness: MASWE-0069
best-practices: [MASTG-BEST-0028]
profiles: [L1, L2]
knowledge: [MASTG-KNOW-0076]
---

## Overview

This test checks for references to file access configuration in iOS WebViews which can introduce security risks such as unauthorized file access and data exfiltration if improperly configured.

For `UIWebView` (deprecated since iOS 12):

- The `file://` scheme is always enabled.
- File access from `file://` URLs is always enabled.
- Universal access from `file://` URLs is always enabled.

For `WKWebView`:

- The `file://` scheme is always enabled and cannot be disabled.
- File access from `file://` URLs is disabled by default but can be enabled.

The following WebView properties can be used to configure file access in `WKWebView`:

- `allowFileAccessFromFileURLs` (`WKPreferences`, `false` by default): enables JavaScript running in the context of a `file://` scheme URL to access content from other `file://` scheme URLs.
- `allowUniversalAccessFromFileURLs` (`WKWebViewConfiguration`, `false` by default): enables JavaScript running in the context of a `file://` scheme URL to access content from any origin.

These properties are **undocumented** and can be set using Key-Value Coding (KVC) by calling `setValue:forKey:`. When these settings are combined with JavaScript enabled, they can enable an attack in which a malicious HTML file gains elevated privileges, accesses local resources, and exfiltrates data over the network, effectively bypassing the security boundaries typically enforced by the same-origin policy.

Additionally, the method `loadFileURL:allowingReadAccessToURL:` should be carefully reviewed. If the `allowingReadAccessToURL` parameter points to a directory instead of a single file, all files within that directory will be accessible to the WebView, which may expose sensitive data.

## Steps

1. Run a static analysis tool such as @MASTG-TOOL-0073 (radare2) or @MASTG-TOOL-0129 (rabin2) to search for:
    - Usage of the deprecated `UIWebView` class.
    - Usage of `WKWebView` and `WKWebViewConfiguration`.
    - References to `allowFileAccessFromFileURLs` or `allowUniversalAccessFromFileURLs`.
    - Usage of the `loadFileURL:allowingReadAccessToURL:` method.
    - Usage of `setValue:forKey:` method which may be setting undocumented WebView properties.

## Observation

The output should contain a list of locations where the relevant APIs and methods are used.

## Evaluation

**Fail:**

The test fails if:

- The app uses the deprecated `UIWebView` class, which always allows file access and universal access from `file://` URLs.
- `WKWebView` is configured with `allowFileAccessFromFileURLs` or `allowUniversalAccessFromFileURLs` explicitly set to `true` via `setValue:forKey:`.
- `loadFileURL:allowingReadAccessToURL:` is called with the `allowingReadAccessToURL` parameter pointing to a directory that may contain sensitive data.

**Pass:**

The test passes if:

- The app uses `WKWebView` without enabling `allowFileAccessFromFileURLs` or `allowUniversalAccessFromFileURLs` (keeping their default value of `false`).
- `loadFileURL:allowingReadAccessToURL:` is called with the `allowingReadAccessToURL` parameter pointing to a single file rather than a directory, or to a directory that does not contain sensitive data.
