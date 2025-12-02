---
platform: ios
title: Hardcoded HTTP URLs
id: MASTG-TEST-0313
type: [static]
weakness: MASWE-0050
related-tests: [MASTG-TEST-0314, MASTG-TEST-0236]
profiles: [L1, L2]
---

## Overview

An iOS app may have hardcoded HTTP URLs embedded in the app binary, library binaries, or other resources within the IPA. These URLs may indicate potential locations where the app communicates with servers over an unencrypted connection.

!!! warning Limitations
    The presence of HTTP URLs alone does not necessarily mean they are actively used for communication. Their usage may depend on runtime conditions, such as how the URLs are invoked and whether cleartext traffic is allowed in the app's ATS configuration. For example, HTTP requests may fail if App Transport Security (ATS) is enabled and no exceptions are configured. See @MASTG-TEST-0314.

## Steps

1. Extract the app (@MASTG-TECH-0058).
2. Run a static analysis tool such as @MASTG-TOOL-0073 on the app binary and search for any `http://` URLs using string analysis commands (e.g., `izz~http://` to search strings or `/` for pattern search).

## Observation

The output should contain a list of URLs and their locations within the app.

## Evaluation

The test case fails if any HTTP URLs are confirmed to be used for communication.

The presence of hardcoded HTTP URLs does not inherently mean they are used; their actual usage must be validated through careful inspection and testing:

- **Reverse Engineering**: Inspect the code locations where the HTTP URLs are referenced. Determine if they are merely stored as constants or actively used to create HTTP requests through networking APIs like `URLSession` or `Network` framework.
- **Static Analysis**: Analyze the app's `Info.plist` configuration to identify whether cleartext traffic is permitted via ATS exceptions. Refer to @MASTG-TEST-0314 for detailed guidance.

Additionally, complement this static inspection with dynamic testing methods:

- **Network Traffic Interception**: Capture and analyze network traffic using tools like @MASTG-TOOL-0077, @MASTG-TOOL-0079, or @MASTG-TOOL-0081. This approach confirms whether the app connects to the identified HTTP URLs during real-world usage but depends on the tester's ability to exercise the app's functionality comprehensively. See @MASTG-TEST-0236.
