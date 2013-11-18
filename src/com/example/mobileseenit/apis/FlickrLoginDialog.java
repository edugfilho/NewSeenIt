package com.example.mobileseenit.apis;



import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import com.aetrion.flickr.auth.AuthInterface;
import com.example.mobileseenit.R;

public class FlickrLoginDialog extends DialogFragment{

	private Dialog mDialog;
	private String imgUrl;
	
	private ImageView imgView;
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
			loginTask =new FlickrLoginTask(myWeb, this, authInterface);
			loginTask.execute("login");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return v;
	}
	
	public void updateAuth(AuthInterface a, String frob)
	{
		this.frob = frob;
		this.authInterface = a;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		try {
			FlickrTestAuthTask lol  =new FlickrTestAuthTask( this, authInterface);
			lol.execute(frob);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
}
