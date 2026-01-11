#!/bin/bash

plutil -convert json -o Info.json Info.plist

# pretty print json
jq . Info.json > Info.json.tmp && mv Info.json.tmp Info.json

gron -m Info.json \
| egrep 'json\.NSAppTransportSecurity\.(NSAllowsArbitraryLoads|NSAllowsArbitraryLoadsInWebContent|NSAllowsArbitraryLoadsForMedia|NSAllowsArbitraryLoadsForLocalNetworking|NSExceptionDomains\["[^"]+"\]\.(NSExceptionAllowsInsecureHTTPLoads|NSTemporaryExceptionAllowsInsecureHTTPLoads|NSAllowsArbitraryLoads))' \
| egrep ' = (true|"true"|1|"1");$' > output.txt
