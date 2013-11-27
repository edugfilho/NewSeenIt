package com.example.mobileseenit;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Spinner;

import com.example.mobileseenit.apis.FlickrLoginDialog;
import com.example.mobileseenit.apis.FlickrUser;
import com.example.mobileseenit.apis.PxLoginDialog;
import com.fivehundredpx.api.auth.AccessToken;

public class SettingsFragment extends Fragment implements OnTouchListener,
		OnClickListener {

	// Login Dialogs
	FlickrLoginDialog flickrLoginDialog;

	//SPinner for Search Diameter
	public Spinner searchDiameter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_settings, container, false);

		// Set Add Flickr Account button click handler
		ImageButton flickrButton = (ImageButton) v
				.findViewById(R.id.add_flickr_account_button);
		flickrButton.setOnClickListener(this);
		
		// Set Add 500px Account button click handler
		ImageButton pxButton = (ImageButton) v
				.findViewById(R.id.add_500px_account_button);
		pxButton.setOnClickListener(this);


		// Check for flickr user
		FlickrUser flickrUser = ((MainActivity) getActivity()).getFlickrUser();
		if (flickrUser != null) {
			// Set username text
			TextView usernameTextView = (TextView) v
					.findViewById(R.id.flickr_username);
			usernameTextView.setText(flickrUser.getUsername());
		}
		
		//Check for 500px user
		AccessToken pxUser = ((MainActivity) getActivity()).getPxUser();
		if (pxUser != null) {
			// Set username text
			TextView usernameTextView = (TextView) v
					.findViewById(R.id.px_username);
			usernameTextView.setText(pxUser.getToken());
		}
		
		//Set the Search Diameter Spinner
		searchDiameter = (Spinner)v.findViewById(R.id.spinner1);

		return v;
	}

	@Override
	public void onClick(View v) {

		// Handle add_flickr_account button
		if (v.getId() == R.id.add_flickr_account_button) {

			// Create and display login dialog
			FragmentTransaction ft = ((MainActivity) v.getContext())
					.getFragmentManager().beginTransaction();
			flickrLoginDialog = FlickrLoginDialog.newInstance();
			flickrLoginDialog.show(ft, "dialog");
		}
		
		//Handle 500px user login
		else if(v.getId() == R.id.add_500px_account_button)
		{
			// Create and display login dialog
			FragmentTransaction ft = ((MainActivity) v.getContext())
					.getFragmentManager().beginTransaction();
			PxLoginDialog pxLogin = PxLoginDialog.newInstance();
			pxLogin.show(ft, "dialog");
		}
	}

	public void updateUserInfo() {
		System.out.println("UPDATEUSERINGO");

		// Check for Users
		FlickrUser flickrUser = ((MainActivity) getActivity()).getFlickrUser();
		AccessToken pxUser = ((MainActivity) getActivity()).getPxUser();
		
		if (flickrUser != null) {
			// Set username text
			TextView usernameTextView = (TextView) getView().findViewById(
					R.id.flickr_username);		
			usernameTextView.setText(flickrUser.getUsername());
			
			//Remove add button, put in Logout button
			//TODO
		}
		
		if(pxUser != null)
		{
			TextView usernameTextView = (TextView)getView()
					.findViewById(R.id.px_username);
			usernameTextView.setText(pxUser.getToken());
		}
		
		
	
	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("ONRESUME");
		// Check for flickr user
		FlickrUser flickrUser = ((MainActivity) getActivity()).getFlickrUser();
		if (flickrUser != null) {
			// Set username text
			TextView usernameTextView = (TextView) getView().findViewById(
					R.id.flickr_username);
			usernameTextView.setText(flickrUser.getUsername());
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
