---
platform: ios
title: Runtime Monitoring of WebView File Access with Frida
id: MASTG-DEMO-0082
code: [swift]
test: MASTG-TEST-0319
---

## Sample

This demo uses the same sample as @MASTG-DEMO-0081.

{{ ../MASTG-DEMO-0081/MastgTest.swift }}

## Steps

1. Install the app on a device (@MASTG-TECH-0056).
2. Make sure you have @MASTG-TOOL-0039 installed on your machine and the frida-server running on the device.
3. Run `run.sh` to spawn the app with Frida.
4. Click the **Start** button to trigger the WebView configuration.
5. Stop the script by pressing `Ctrl+C`.

{{ run.sh # script.js }}

The Frida script performs the following:

1. Enumerates all `WKWebView` instances in the application.
2. For each WebView instance found:
   - Retrieves the current URL being loaded.
   - Checks if JavaScript is enabled.
   - Uses `valueForKey:` to read the undocumented properties `allowFileAccessFromFileURLs` and `allowUniversalAccessFromFileURLs`.
   - Displays the configuration values.

## Observation

The output shows the WebView instances found and their configuration settings.

{{ output.txt }}

## Evaluation

The test **fails** because:

- A `WKWebView` instance was found with `javaScriptEnabled` set to `true`.
- The `allowFileAccessFromFileURLs` property is set to `1` (true), allowing JavaScript running in a `file://` context to access other local files.
- The `allowUniversalAccessFromFileURLs` property is set to `1` (true), allowing JavaScript to bypass same-origin policy restrictions.

These settings combined could allow malicious JavaScript in a local HTML file to access and exfiltrate sensitive data from the device's file system.
