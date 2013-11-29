package com.example.mobileseenit;

import java.util.ArrayList;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobileseenit.SeenItLocation.LocationResult;
import com.example.mobileseenit.apis.FlickrSearchTask;
import com.example.mobileseenit.helpers.PhotoStreamImageView;
import com.example.mobileseenit.helpers.PhotoWrapper;

public class MainFragment extends Fragment implements OnTouchListener,
		OnClickListener {

	FlickrSearchTask flickrSearch;
	MainActivity mainAct;

	LocationResult locationResult = new LocationResult() {
		@Override
		public void gotLocation(Location location) {
			// It's called after location is obtained. Now that it has lat and
			// lng, it can search for pictures
			mainAct.setLat(location.getLatitude());
			mainAct.setLng(location.getLongitude());
			
			//Only displays pictures on the screen if there are none
			if (mainAct.getPhotoList().isEmpty()) {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {

						Toast.makeText(getActivity().getApplicationContext(),
								"Location acquired. Displaying pictures",
								Toast.LENGTH_SHORT).show();
					}
				});

				flickrSearch = new FlickrSearchTask(getActivity());
				flickrSearch.execute();
			}

		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		mainAct = (MainActivity) getActivity();

		// Gets location and calls gotLocation when it's done
		mainAct.getLoc().getLocation(getActivity(), locationResult);

		return rootView;
	}

	public void updateDisplayedPhotos() {
		// Hide progress spinner
		ProgressBar progressBar = (ProgressBar) getView().findViewById(
				R.id.photo_load_progress_bar);
		progressBar.setVisibility(View.GONE);

		// Display images
		MainActivity mainActivity = (MainActivity) getActivity();
		ArrayList<PhotoWrapper> photos = mainActivity.getPhotoList();
		for (PhotoWrapper p : photos) {

			LinearLayout r = (LinearLayout) getView().findViewById(
					R.id.photo_stream);
			PhotoStreamImageView newImage = new PhotoStreamImageView(
					getActivity(), p);
			r.addView(newImage);
		}

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onPause() {
		// Stops any location request
		mainAct.getLoc().getLocationManager()
				.removeUpdates(mainAct.getLoc().locationListenerGps);
		super.onPause();
	}

	@Override
	public void onStop() {
		// Stops any location request
		mainAct.getLoc().getLocationManager()
				.removeUpdates(mainAct.getLoc().locationListenerGps);
		super.onPause();
	}

	@Override
	public void onResume() {
		// When the app is resumed, location updates start being requested again
		if (mainAct.getLoc().gps_enabled) {
			mainAct.getLoc()
					.getLocationManager()
					.requestLocationUpdates(LocationManager.GPS_PROVIDER,
							mainAct.getLoc().getUpdateIntervalTimeMilisec(),
							1000, mainAct.getLoc().locationListenerGps);
			Toast.makeText(getActivity(), "Getting location...",
					Toast.LENGTH_SHORT).show();

		}
		super.onResume();
	}
}
