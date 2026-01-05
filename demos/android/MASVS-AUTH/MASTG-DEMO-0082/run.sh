#!/bin/bash
NO_COLOR=true semgrep -c ../../../../rules/mastg-android-biometric-device-credential-fallback.yml ./MastgTest_reversed.java --text -o output.txt
