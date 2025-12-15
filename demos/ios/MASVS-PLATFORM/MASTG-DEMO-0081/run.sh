#!/bin/bash

# Assumes you have the MASTestApp binary extracted in the current directory

echo "=== Searching for WKWebView references ===" > output.txt
rabin2 -zz ./MASTestApp | grep -i "WKWebView" >> output.txt

echo "" >> output.txt
echo "=== Searching for setValue:forKey: references ===" >> output.txt
rabin2 -zz ./MASTestApp | grep -i "setValue:forKey:" >> output.txt

echo "" >> output.txt
echo "=== Searching for allowFileAccessFromFileURLs ===" >> output.txt
rabin2 -zz ./MASTestApp | grep -i "allowFileAccessFromFileURLs" >> output.txt

echo "" >> output.txt
echo "=== Searching for allowUniversalAccessFromFileURLs ===" >> output.txt
rabin2 -zz ./MASTestApp | grep -i "allowUniversalAccessFromFileURLs" >> output.txt

echo "" >> output.txt
echo "=== Searching for loadFileURL references ===" >> output.txt
rabin2 -zz ./MASTestApp | grep -i "loadFileURL" >> output.txt

cat output.txt
