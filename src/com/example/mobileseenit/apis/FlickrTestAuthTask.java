package com.example.mobileseenit.apis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Auth;
import com.aetrion.flickr.auth.AuthInterface;
import com.example.mobileseenit.MainActivity;

/**
 * Async task to finish the Auth process for Flickr. There should be a better
 * way to do this (ie. in just the loginTask), but we need the user to complete
 * the login page and exit the dialog before we can proceed. In order to do this
 * simply, the 2 parts of the authorization were broken up (FlickrLoginTask and
 * this class).
 * 
 * Based off example here:
 * https://github.com/r0man/flickrj/blob/master/examples/AuthExample.java
 * 
 * @author dylanrunkel
 * 
 */
public class FlickrTestAuthTask extends AsyncTask<String, Void, String> {

	Flickr f;
	RequestContext requestContext;
	String frob = "";
	String token = "";

	FlickrLoginDialog fragment;
	AuthInterface a;
	MainActivity mainActivity;
	Auth auth;
	Context context;
	boolean goodAuth;

	public FlickrTestAuthTask(FlickrLoginDialog gg, AuthInterface a,
			Context context) {

		fragment = gg;
		mainActivity = (MainActivity) gg.getActivity();
		this.context = context;

		f = mainActivity.getFlickr();
		this.a = f.getAuthInterface();
		goodAuth = false;
	}

	@Override
	protected String doInBackground(String... searchTerms) {
		try {
			return search(searchTerms[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String search(String... frobString) throws IOException,
			SAXException, ParserConfigurationException {

		BufferedReader infile = new BufferedReader(new InputStreamReader(
				System.in));
		String line = infile.readLine();
		try {
			frob = frobString[0];
			auth = f.getAuthInterface().getToken(frob);
			f.setAuth(auth);
			System.out.println("Authentication success");
			// This token can be used until the user revokes it.
			System.out.println("Token: " + auth.getToken());
			System.out.println("nsid: " + auth.getUser().getId());
			System.out.println("Realname: " + auth.getUser().getRealName());
			System.out.println("Username: " + auth.getUser().getUsername());
			System.out.println("Permission: " + auth.getPermission().getType());
			goodAuth = true;

		} catch (FlickrException e) {
			System.out.println("Authentication failed");
			e.printStackTrace();
		}

		return "test";
	} 

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		// Check if auth succeeded
		if (goodAuth) {
			FlickrUser newUser = new FlickrUser();
			newUser.setUsername(auth.getUser().getUsername());

			SharedPreferences sp = context.getSharedPreferences("seenit_prefs",
					0);
			sp.edit().putString("flickr_token", auth.getToken()).commit();

			fragment.updateFlickr(f, "");
			fragment.acceptFlickrUser(newUser);
		}
	}

}
