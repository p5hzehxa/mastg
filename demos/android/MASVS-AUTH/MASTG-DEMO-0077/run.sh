#!/bin/bash
NO_COLOR=true semgrep -c ../../../../rules/mastg-android-biometric-event-bound.yml ../MASTG-DEMO-0076/MastgTest_reversed.java --text -o output.txt
