package com.example.mobileseenit.apis;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.aetrion.flickr.auth.AuthInterface;
import com.example.mobileseenit.R;

public class FlickrLoginDialog extends DialogFragment {

	AuthInterface authInterface;
	WebView myWeb;
	FlickrLoginTask loginTask;
	String frob;

	public static FlickrLoginDialog newInstance() {
		FlickrLoginDialog f = new FlickrLoginDialog();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		myWeb = (WebView) v.findViewById(R.id.flickr_login_webview);

		try {
			loginTask = new FlickrLoginTask(myWeb, this, authInterface);
			loginTask.execute("login");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return v;
	}

	//Called from FlickrAuth after user logs in.
	//User is created. Send this to MainAcitity
	public void acceptFlickrUser(FlickrUser user) {
		FlickrUser uu = user;
		System.out.println();

		// Tell MainActivity that user is logged in on Flickr
		mCallback.onFlickLoggedIn(user);
	}

	public void updateAuth(AuthInterface a, String frob) {
		this.frob = frob;
		this.authInterface = a;
	}

	//When user closes login dialog for Flickr
	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			
			//Check the Auth, build a FlickrUser
			FlickrTestAuthTask lol = new FlickrTestAuthTask(this, authInterface);
			lol.execute(frob);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Interface to communicate with MainActivity
	 */
	OnFlickrLoggedInListener mCallback;

	// Container Activity must implement this interface
	public interface OnFlickrLoggedInListener {
		public void onFlickLoggedIn(FlickrUser user);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnFlickrLoggedInListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFlickrLoggedInListener");
		}
	}

}
