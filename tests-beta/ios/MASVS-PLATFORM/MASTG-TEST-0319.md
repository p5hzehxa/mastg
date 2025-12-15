---
platform: ios
title: Runtime Use of File Access APIs in WebViews
id: MASTG-TEST-0319
apis: [WKWebView, WKPreferences, WKWebViewConfiguration, allowFileAccessFromFileURLs, allowUniversalAccessFromFileURLs, javaScriptEnabled, UIWebView]
type: [dynamic]
weakness: MASWE-0069
best-practices: [MASTG-BEST-0028]
profiles: [L1, L2]
knowledge: [MASTG-KNOW-0076]
---

## Overview

This test is the dynamic counterpart to @MASTG-TEST-0318. It verifies at runtime whether WebViews in the app have file access enabled, which can introduce security risks such as unauthorized file access and data exfiltration.

## Steps

1. Run a dynamic analysis tool like @MASTG-TOOL-0031 (Frida) to:
    - Enumerate instances of `UIWebView` in the app (if any).
    - Enumerate instances of `WKWebView` in the app and inspect their configuration values.
    - Check the values of `allowFileAccessFromFileURLs` and `allowUniversalAccessFromFileURLs` using `valueForKey:`.
    - Verify if JavaScript is enabled.

Example Frida script:

```javascript
ObjC.choose(ObjC.classes['WKWebView'], {
  onMatch: function (wk) {
    console.log('WKWebView instance: ', wk);
    console.log('URL: ', wk.URL().toString());
    console.log('javaScriptEnabled: ', wk.configuration().preferences().javaScriptEnabled());
    console.log('allowFileAccessFromFileURLs: ',
            wk.configuration().preferences().valueForKey_('allowFileAccessFromFileURLs').toString());
    console.log('allowUniversalAccessFromFileURLs: ',
            wk.configuration().valueForKey_('allowUniversalAccessFromFileURLs').toString());
  },
  onComplete: function () {
    console.log('done for WKWebView!');
  }
});

ObjC.choose(ObjC.classes['UIWebView'], {
  onMatch: function (uiWebView) {
    console.log('UIWebView instance found (deprecated): ', uiWebView);
  },
  onComplete: function () {
    console.log('done for UIWebView!');
  }
});
```

## Observation

The output should contain a list of WebView instances and their corresponding configuration settings, including:

- `javaScriptEnabled`
- `allowFileAccessFromFileURLs`
- `allowUniversalAccessFromFileURLs`

## Evaluation

**Fail:**

The test fails if any of the following are true:

- Any `UIWebView` instances are found (deprecated and always allows file access).
- For `WKWebView`, both `javaScriptEnabled` is `true` and either `allowFileAccessFromFileURLs` or `allowUniversalAccessFromFileURLs` is set to `1` (true).

**Note:** `allowFileAccessFromFileURLs` or `allowUniversalAccessFromFileURLs` being enabled does not represent a security vulnerability by itself if JavaScript is disabled, but it is recommended to keep them disabled when not necessary.

**Pass:**

The test passes if:

- No `UIWebView` instances are found.
- For all `WKWebView` instances, either `javaScriptEnabled` is `false`, or both `allowFileAccessFromFileURLs` and `allowUniversalAccessFromFileURLs` are set to `0` (false).
