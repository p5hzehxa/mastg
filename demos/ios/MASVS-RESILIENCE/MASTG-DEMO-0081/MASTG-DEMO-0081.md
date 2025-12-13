---
platform: ios
title: Verbose Error Logging Analysis
code: [swift]
id: MASTG-DEMO-0081
test: MASTG-TEST-0318
tools: [MASTG-TOOL-0073]
---

### Sample

The code snippet below shows sample code that performs verbose error logging and debugging messages that expose implementation details such as API endpoints, authentication logic, error codes, and internal configuration.

{{ MastgTest.swift }}

### Steps

1. Unzip the app package and locate the main binary file (@MASTG-TECH-0058), which in this case is `./Payload/MASTestApp.app/MASTestApp`.
2. Open the app binary with @MASTG-TOOL-0073 with the `-i` option to run this script.

{{ verbose_logging.r2 }}

{{ run.sh }}

### Observation

The output reveals verbose logging statements in the app that expose internal implementation details.

{{ output.txt }}

### Evaluation

The test fails because the app contains multiple instances of verbose logging that expose:

- **API endpoints**: The debug log reveals the internal API endpoint `https://internal-api.example.com/v2/auth/login`.
- **Authentication details**: Logs show session token generation and authentication flow details.
- **Error handling logic**: Detailed error codes (`AUTH_001`) and module names (`AuthenticationService.validateCredentials()`).
- **Network configuration**: Debug logs expose timeout values, retry counts, and SSL pinning status.
- **Internal state**: Messages reveal caching behavior and fallback mechanisms.

These verbose logging statements should be removed or properly guarded with compilation conditions (e.g., `#if DEBUG`) to prevent them from appearing in production builds.
