package com.example.mobileseenit.apis;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.AuthInterface;
import com.aetrion.flickr.auth.Permission;
import com.example.mobileseenit.MainActivity;

//Code based off Example
//https://github.com/r0man/flickrj/blob/master/examples/AuthExample.java
public class FlickrLoginTask extends AsyncTask<String, Void, String> {

	Flickr flickr;
	RequestContext requestContext;
	String frob = "";
	String token = "";
	WebView myWebView;

	DialogFragment fragment;
	AuthInterface a;

	public FlickrLoginTask(WebView v, DialogFragment gg) {
		fragment = gg;
		myWebView = v;
	
		//Get flickr object from MainAcivity
		flickr = ((MainActivity) gg.getActivity()).getFlickr();
	}

	@Override
	protected String doInBackground(String... searchTerms) {

		// params comes from the execute() call. Will be the search terms.
		try {
			if (searchTerms[0] == "login")
				return search(searchTerms);

		} catch (IOException e) {
			return "Unable to retrieve web page. URL may be invalid.";
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "done";
	}

	public String search(String... searchTerms) throws IOException,
			SAXException, ParserConfigurationException {

		Flickr.debugStream = false;
		requestContext = RequestContext.getRequestContext();
		a = flickr.getAuthInterface();
		try {
			frob = a.getFrob();
		} catch (FlickrException e) {
			e.printStackTrace();
		}
		URL url = a.buildAuthenticationUrl(Permission.WRITE, frob);
		System.out
				.println("Press return after you granted access at this URL:");
		System.out.println(url.toExternalForm());

		return url.toExternalForm();
	}

	/**
	 * After execute, dsplay the flickr login page.
	 * The user should close this after completion.
	 */
	@Override
	protected void onPostExecute(String result) {
		if (result.contains("http")) {
			
			myWebView.setWebViewClient(new WebViewClient());
			myWebView.clearCache(true);
			myWebView.getSettings().setJavaScriptEnabled(true);
			myWebView.loadUrl(result);
			
			((FlickrLoginDialog) fragment).updateFlickr(flickr, frob);
		}

	}

}
