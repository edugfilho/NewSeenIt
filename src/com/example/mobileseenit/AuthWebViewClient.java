package com.example.mobileseenit;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AuthWebViewClient extends WebViewClient {
	
	String request_token = null;
	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
	    if (url.contains("google")) {
	            System.out.println(url);
		    String parts[] = url.split("=");
	            request_token = parts[1];  //This is your request token.
		    //InstagramLoginDialog.this.dismiss();
		    return true;
	        }
	    return false;
	}

}
