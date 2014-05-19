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
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.mobileseenit.SeenItLocation.LocationResult;
import com.example.mobileseenit.apis.FlickrSearchTask;
import com.example.mobileseenit.apis.PxSearchTask;
import com.example.mobileseenit.helpers.PhotoStreamImageView;
import com.example.mobileseenit.helpers.PhotoWrapper;

public class MainFragment extends Fragment implements OnTouchListener,
		OnClickListener {

	ArrayList<PhotoWrapper> photos = null;
	PxSearchTask pxSearchTask;
	FlickrSearchTask flickrSearch;
	MainActivity mainActivity;

	LocationResult locationResult = new LocationResult() {
		@Override
		public void gotLocation(Location location) {

			// It's called only when the location is changed. Now that it has
			// new
			// lat and lng, it can search for new photos

			mainActivity.setLat(location.getLatitude());
			mainActivity.setLng(location.getLongitude());

			mainActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {

					// if (mainActivity.getPhotoList().isEmpty()) {
					Toast.makeText(mainActivity.getApplicationContext(),
							"Location acquired. Downloading new photos",
							Toast.LENGTH_SHORT).show();

					flickrSearch = new FlickrSearchTask(getActivity());
					flickrSearch.execute();

					// Search 500px Images
					// pxSearchTask = new PxSearchTask(getActivity());
					// pxSearchTask.execute("search");

				}
			});

		}

	};

	public void updateDisplayedPhotos() {
		// Hide progress spinner

		ProgressBar progressBar = (ProgressBar) getView().findViewById(
				R.id.photo_load_progress_bar);
		progressBar.setVisibility(View.GONE);

		// Display images
		ArrayList<PhotoWrapper> photos = mainActivity.getPhotoList();
		LinearLayout r1 = (LinearLayout) getView().findViewById(
				R.id.photo_stream);
		/*
		 * LinearLayout r2 = (LinearLayout) getView().findViewById(
		 * R.id.photo_stream3);
		 */
		// ArrayList<PhotoStreamImageView> wrappedPhotos = new
		// ArrayList<PhotoStreamImageView>();
		// boolean first = true;
		for (PhotoWrapper p : photos) {

			PhotoStreamImageView newImage = new PhotoStreamImageView(
					getActivity(), p);
			// wrappedPhotos.add(newImage);
			// if (first) {
			r1.addView(newImage);
			/*
			 * } else { r2.addView(newImage); } first = !first;
			 */
		}
		/*
		 * ArrayAdapter<PhotoStreamImageView> adapter = new
		 * ArrayAdapter<PhotoStreamImageView>( mainActivity,
		 * android.R.layout.simple_gallery_item, wrappedPhotos);
		 * r.setAdapter(adapter);
		 */

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);

		mainActivity = (MainActivity) getActivity();

		// Whenever GPS acquires location, the images are fetched and shown
		mainActivity
				.getLoc()
				.getLocationManager()
				.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						mainActivity.getLoc().getUpdateIntervalTimeMilisec(),
						mainActivity.getRadius().floatValue() * 1000,
						mainActivity.getLoc().locationListenerGps);
		mainActivity.getLoc().getLocation(getActivity(), locationResult);

		mainActivity
				.getLoc()
				.getLocationManager()
				.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
						mainActivity.getLoc().getUpdateIntervalTimeMilisec(),
						mainActivity.getRadius().floatValue() * 1000,
						mainActivity.getLoc().locationListenerGps);
		mainActivity.getLoc().getLocation(getActivity(), locationResult);
		Toast.makeText(getActivity(), "Getting location...", Toast.LENGTH_SHORT)
				.show();
		return rootView;
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
	public void onStart() {

		super.onStart();
	}

}
