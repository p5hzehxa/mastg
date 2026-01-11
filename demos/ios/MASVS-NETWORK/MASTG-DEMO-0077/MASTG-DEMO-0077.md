---
platform: ios
title: Hardcoded HTTP URLs in iOS Binary
code: [swift]
id: MASTG-DEMO-0077
test: MASTG-TEST-0313
kind: fail
---

### Sample

The code snippet below shows sample code that uses hardcoded HTTP URLs:

{{ MastgTest.swift }}

### Steps

1. Unzip the app package and locate the main binary file (@MASTG-TECH-0058), which in this case is `./Payload/MASTestApp.app/MASTestApp`.
2. Run @MASTG-TOOL-0073 with the script to search for HTTP URLs in the binary.

{{ http_urls.r2 }}

{{ run.sh }}

### Observation

The output contains a list of HTTP URLs found in the binary and the cross reference information including the HTTP URLs offsets (`from`) and containing functions (`fcn_addr`, `fcn_name`, `realname`):

{{ output.txt # xrefs.json }}

### Evaluation

The test fails because hardcoded HTTP URLs were found in the binary. The URLs `http://httpbin.org/get` and `http://example.com/api` indicate potential cleartext communication endpoints.

To determine if these URLs are actually used for network communication:

1. Review the code context where these URLs are referenced. For example, use the `from` offset from the output to locate the surrounding code in the binary (e.g. `s <from>; pd 20` in radare2).
2. Check if ATS exceptions are configured to allow cleartext traffic (see @MASTG-TEST-0314).
3. Perform dynamic analysis to observe actual network traffic (see @MASTG-TEST-0236).
