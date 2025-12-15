---
platform: ios
title: References to File Access in WebViews with radare2
id: MASTG-DEMO-0081
code: [swift]
test: MASTG-TEST-0318
---

## Sample

This sample demonstrates a WKWebView with file access enabled via the undocumented properties `allowFileAccessFromFileURLs` and `allowUniversalAccessFromFileURLs`.

{{ MastgTest.swift }}

The sample:

- Creates a `WKWebView` instance with custom configuration.
- Uses Key-Value Coding (KVC) to set the undocumented properties:
  - `allowFileAccessFromFileURLs` is set to `true`, allowing JavaScript to access other local files.
  - `allowUniversalAccessFromFileURLs` is set to `true`, allowing JavaScript to access content from any origin.
- Loads a local HTML file that could potentially access and exfiltrate sensitive data.

## Steps

1. Extract the app binary from the IPA (@MASTG-TECH-0054).
2. Run @MASTG-TOOL-0129 (rabin2) to search for references to the relevant WebView methods.

{{ run.sh }}

The script searches for:

- References to `WKWebView` class usage.
- The `setValue:forKey:` method which is used to set undocumented properties.
- String references to `allowFileAccessFromFileURLs` and `allowUniversalAccessFromFileURLs`.
- The `loadFileURL:allowingReadAccessToURL:` method.

## Observation

The output shows references to WebView-related methods and strings in the binary.

{{ output.txt }}

## Evaluation

The test **fails** because:

- The binary contains references to `setValue:forKey:` which is used to set the undocumented WebView properties.
- The strings `allowFileAccessFromFileURLs` and `allowUniversalAccessFromFileURLs` are present in the binary, indicating these properties are being configured.
- The presence of `loadFileURL:allowingReadAccessToURL:` indicates the app loads local files into the WebView.

These findings suggest the app enables file access in WebViews, which could allow malicious JavaScript to access and exfiltrate sensitive local files.
