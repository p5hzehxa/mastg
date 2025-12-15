#!/bin/bash

# Replace with your app's bundle identifier
BUNDLE_ID="com.owasp.mastestapp"

echo "Starting Frida script to monitor WKWebView file access..."
echo "Press Ctrl+C to stop"
echo ""

frida -U -f "$BUNDLE_ID" -l script.js --no-pause
