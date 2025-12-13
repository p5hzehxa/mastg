---
platform: ios
title: Verbose Error Logging and Debugging Messages
id: MASTG-TEST-0318
type: [static, dynamic]
weakness: MASWE-0094
knowledge: [MASTG-KNOW-0064, MASTG-KNOW-0101]
profiles: [R]
---

## Overview

This test checks for verbose error logging and debugging messages in iOS applications. While logging is useful during development, verbose logging in production builds can expose implementation details such as function names, code paths, internal state information, and error conditions that could be exploited by attackers performing reverse engineering.

Common logging APIs on iOS include `NSLog`, `print`, `println`, `dump`, `debugPrint`, and `os_log`. Applications should ensure that debug-level logging is disabled in production builds and that any error messages logged are minimal and don't reveal sensitive implementation details.

This test focuses on verbose logging that exposes implementation details. For tests specifically targeting sensitive data in logs, see @MASTG-TEST-0296 and @MASTG-TEST-0297.

## Steps

1. For static analysis:
    - Extract the app binary using @MASTG-TECH-0058.
    - Use @MASTG-TOOL-0073 to list strings and look for verbose logging patterns.
    - Search for references to logging APIs such as `NSLog`, `print`, `os_log`, `dump`, and `debugPrint`.
    - Check if the app uses preprocessor macros or compilation conditions (e.g., `#if DEBUG`) to disable logging in release builds.

2. For dynamic analysis:
    - Install the app on a device using @MASTG-TECH-0056.
    - Monitor system logs with @MASTG-TECH-0060 while interacting with the app.
    - Trigger various app functionalities including error conditions (e.g., network failures, invalid inputs).

## Observation

The output should contain:

- A list of logging function calls found in the binary.
- Log messages captured during runtime that include implementation details.
- Whether debug guards or compilation conditions are used to control logging.

## Evaluation

The test fails if:

- The app logs verbose debug messages in production builds that expose implementation details such as:
    - Internal function names or code paths
    - Detailed error messages with stack information
    - API endpoints or internal URLs
    - Internal state or configuration details
    - Library or framework version information
    - Debugging information intended only for developers
- Debug logging is not properly guarded with conditional compilation (e.g., `#if DEBUG` in Objective-C or `#if DEBUG_LOGGING` in Swift).
- The app uses verbose logging levels (e.g., `os_log` with `.debug` or `.info` types) in production without proper filtering.
