import UIKit
import WebKit

// SUMMARY: This sample demonstrates a WKWebView configured to allow file access from file URLs, which could enable malicious JavaScript to access local files.

class MastgTest {
    
    var webView: WKWebView!
    
    func mastgTest() -> String {
        
        // FAIL: [MASTG-TEST-0318] Configure WKWebView with file access enabled
        let configuration = WKWebViewConfiguration()
        
        // Enable JavaScript
        configuration.preferences.javaScriptEnabled = true
        
        // FAIL: [MASTG-TEST-0318] Set undocumented properties to allow file access
        // These properties are not officially documented and must be set via KVC
        configuration.preferences.setValue(true, forKey: "allowFileAccessFromFileURLs")
        configuration.setValue(true, forKey: "allowUniversalAccessFromFileURLs")
        
        // Create WebView with the configuration
        webView = WKWebView(frame: .zero, configuration: configuration)
        
        // FAIL: [MASTG-TEST-0318] Load a local HTML file
        // In a real attack scenario, this HTML file could contain malicious JavaScript
        if let htmlPath = Bundle.main.url(forResource: "index", withExtension: "html") {
            // FAIL: [MASTG-TEST-0318] Grant read access to the directory containing the HTML file
            // This allows the WebView to access all files in that directory
            let readAccessURL = htmlPath.deletingLastPathComponent()
            webView.loadFileURL(htmlPath, allowingReadAccessTo: readAccessURL)
        }
        
        return "WebView configured with file access enabled"
    }
}
