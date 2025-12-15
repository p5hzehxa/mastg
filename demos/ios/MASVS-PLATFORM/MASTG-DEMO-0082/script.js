// Frida script to enumerate WKWebView instances and inspect their file access configuration

console.log("[*] Starting WKWebView file access monitor...");

// Enumerate all WKWebView instances
ObjC.choose(ObjC.classes['WKWebView'], {
  onMatch: function (webView) {
    console.log("\n[+] Found WKWebView instance: " + webView);
    
    try {
      // Get the current URL
      var url = webView.URL();
      console.log("    URL: " + (url ? url.toString() : "null"));
      
      // Get the configuration
      var config = webView.configuration();
      var prefs = config.preferences();
      
      // Check JavaScript enabled
      var jsEnabled = prefs.javaScriptEnabled();
      console.log("    javaScriptEnabled: " + jsEnabled);
      
      // Check undocumented file access properties using valueForKey:
      try {
        var allowFileAccessFromFileURLs = prefs.valueForKey_("allowFileAccessFromFileURLs");
        console.log("    allowFileAccessFromFileURLs: " + allowFileAccessFromFileURLs);
      } catch (e) {
        console.log("    allowFileAccessFromFileURLs: Error reading property - " + e);
      }
      
      try {
        var allowUniversalAccessFromFileURLs = config.valueForKey_("allowUniversalAccessFromFileURLs");
        console.log("    allowUniversalAccessFromFileURLs: " + allowUniversalAccessFromFileURLs);
      } catch (e) {
        console.log("    allowUniversalAccessFromFileURLs: Error reading property - " + e);
      }
      
    } catch (e) {
      console.log("    Error inspecting WebView: " + e);
    }
  },
  onComplete: function () {
    console.log("\n[*] WKWebView enumeration complete");
  }
});

console.log("[*] Script loaded. Waiting for WebView instances...");
