package com.example.mobileseenit.apis;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Permission;

import android.os.AsyncTask;

public class FlickrUploadTask extends AsyncTask<String, Void, String> {

	Flickr f;
	private String API_KEY = "af76271af34e193bd2f002eb32032e01";
	private String SECRET = "b7b96e8ab7032484";

	public FlickrUploadTask() {
		// Setup flickrJ object
		try {
			f = new Flickr(API_KEY, SECRET, new REST());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected String doInBackground(String... params) {
		// params comes from the execute() call. Will be the search terms.
		try {
			return upload(params);

		} catch (IOException e) {
			return "Unable to retrieve web page. URL may be invalid.";
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String upload(String... searchTerms) throws IOException,
			SAXException, ParserConfigurationException {

	

		return "done";
	}

}
