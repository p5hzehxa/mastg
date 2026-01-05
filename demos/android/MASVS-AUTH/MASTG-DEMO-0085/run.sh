#!/bin/bash
NO_COLOR=true semgrep -c ../../../../rules/mastg-android-biometric-no-confirmation-required.yml ../MASTG-DEMO-0082/MastgTest_reversed.java --text -o output.txt
