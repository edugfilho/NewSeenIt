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

import com.example.mobileseenit.apis.FlickrLoginDialog;
import com.example.mobileseenit.apis.FlickrUser;

public class SettingsFragment extends Fragment implements OnTouchListener,
		OnClickListener {

	// The login dialog for flickr
	FlickrLoginDialog flickrLoginDialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_settings, container, false);

		// Set Add Flickr Account button click handler
		ImageButton b = (ImageButton) v
				.findViewById(R.id.add_flickr_account_button);
		b.setOnClickListener(this);

		// Check for flickr user
		FlickrUser flickrUser = ((MainActivity) getActivity()).getFlickrUser();
		if (flickrUser != null) {
			// Set username text
			TextView usernameTextView = (TextView) v
					.findViewById(R.id.flickr_username);
			usernameTextView.setText(flickrUser.getUsername());
		}

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
	}

	public void updateUserInfo() {
		
		// Check for flickr user
		FlickrUser flickrUser = ((MainActivity) getActivity()).getFlickrUser();
		
		if (flickrUser != null) {
			// Set username text
			TextView usernameTextView = (TextView) getView().findViewById(
					R.id.flickr_username);		
			usernameTextView.setText(flickrUser.getUsername());
			
			//Remove add button, put in Logout button
			//TODO
		}
	
	}

	@Override
	public void onResume() {
		super.onResume();

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
