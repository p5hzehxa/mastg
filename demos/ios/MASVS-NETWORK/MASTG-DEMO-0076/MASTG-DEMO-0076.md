---
platform: ios
title: Insecure ATS Configuration Allowing Cleartext Traffic
code: [xml]
id: MASTG-DEMO-0076
test: MASTG-TEST-0314
kind: fail
---

### Sample

The code snippet below shows an insecure ATS configuration in an `Info.plist` file that disables App Transport Security globally by setting `NSAllowsArbitraryLoads` to `true`:

{{ Info.plist }}

### Steps

1. Extract the app (@MASTG-TECH-0058) and locate the `Info.plist` file inside the app bundle.
2. Run the following script to extract and display the `NSAppTransportSecurity` configuration:

{{ run.sh }}

### Observation

The output shows the ATS configuration found in the `Info.plist` file:

{{ output.txt # Info.json }}

### Evaluation

The test fails because several ATS settings are set to `true`, which disables ATS globally and allows cleartext HTTP traffic to any domain. Specifically, the following settings are misconfigured:

- `NSAllowsArbitraryLoads = true` disables ATS for all network connections.
- `NSAllowsArbitraryLoadsForLocalNetworking = true` allows cleartext traffic on local networks.
- `NSAllowsArbitraryLoadsForMedia = true` allows cleartext traffic for media resources.
- `NSAllowsArbitraryLoadsInWebContent = true` allows cleartext traffic in WebViews.
- Domain-specific exceptions for `api.example.com` and `cdn.example.net` also allow insecure HTTP loads.
