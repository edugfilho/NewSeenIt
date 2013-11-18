package com.example.mobileseenit.apis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.webkit.WebView;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;

//Code based off Example
//https://github.com/r0man/flickrj/blob/master/examples/AuthExample.java
public class FlickrTestAuthTask extends AsyncTask<String, Void, String>{

	Flickr f;
	RequestContext requestContext;
	String frob = "";
	String token = "";
	private String API_KEY = "af76271af34e193bd2f002eb32032e01";
	private String SECRET = "b7b96e8ab7032484";
	FlickrLoginDialog g;
	DialogFragment fragment;
	AuthInterface a;
	
	public FlickrTestAuthTask(DialogFragment gg, AuthInterface a) {
		this.a = a;
		fragment = gg;
		// Setup flickrJ object
		try {
			f = new Flickr(API_KEY, SECRET, new REST());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	protected String doInBackground(String... searchTerms) {

		// params comes from the execute() call. Will be the search terms.
		try {
				return search(searchTerms[0]);

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
	public String search(String... frobString) throws IOException, SAXException, ParserConfigurationException {

		BufferedReader infile =
		          new BufferedReader ( new InputStreamReader (System.in) );
		        String line = infile.readLine();
		        try {
		        	frob = frobString[0];
		            Auth auth = a.getToken(frob);
		            System.out.println("Authentication success");
		            // This token can be used until the user revokes it.
		            System.out.println("Token: " + auth.getToken());
		            System.out.println("nsid: " + auth.getUser().getId());
		            System.out.println("Realname: " + auth.getUser().getRealName());
		            System.out.println("Username: " + auth.getUser().getUsername());
		            System.out.println("Permission: " + auth.getPermission().getType());
		        } catch (FlickrException e) {
		            System.out.println("Authentication failed");
		            e.printStackTrace();
		        }

		return "test";
	}
	

	
//	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
	
	}
	

}
