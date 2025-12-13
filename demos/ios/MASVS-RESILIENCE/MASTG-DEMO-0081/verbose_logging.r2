e scr.color=0
e scr.interactive=false

?e === Analyzing iOS Binary for Verbose Logging ===
?e

?e [*] Searching for NSLog references:
afl~NSLog

?e
?e [*] Searching for print/debugPrint functions:
afl~print

?e
?e [*] Searching for os_log references:
afl~os_log

?e
?e [*] Searching for dump function:
afl~dump

?e
?e [*] Looking for debug-related strings:
izz~DEBUG

?e
?e [*] Looking for error logging strings:
izz~ERROR

?e
?e [*] Looking for API endpoint references in strings:
izz~api,endpoint,http

?e
?e [*] Looking for authentication-related logging:
izz~auth,login,token,credential
