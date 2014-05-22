package com.example.mobileseenit;

import java.util.ArrayList;

import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
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

	Integer[] imageIDs = { R.drawable.loading, R.drawable.loading,
			R.drawable.loading, R.drawable.loading, R.drawable.loading,
			R.drawable.loading, R.drawable.loading };

	LocationResult locationResult = new LocationResult() {
		@Override
		public void gotLocation(Location location) {
			String provider = location.getProvider();

			// It's called only when the location is changed. Now that it has
			// new lat and lng, it can search for new photos

			mainActivity.setLat(location.getLatitude());
			mainActivity.setLng(location.getLongitude());

			Toast.makeText(
					mainActivity.getApplicationContext(),
					"Location acquired by " + provider
							+ ". Downloading new photos", Toast.LENGTH_SHORT)
					.show();
			mainActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {

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
		photos = mainActivity.getPhotoList();
		// Hide progress spinner
		/*
		 * getView().findViewById(R.id.photo_load_progress_bar).setVisibility(
		 * View.GONE);
		 */
		// Display images
		GridView gridView = (GridView) getView().findViewById(R.id.gridview);
		gridView.setVisibility(View.VISIBLE);
		gridView.setAdapter(new ImageAdapter(mainActivity));

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
						mainActivity.getLoc());
		// mainActivity.getLoc().getLocation(getActivity(), locationResult);

		mainActivity
				.getLoc()
				.getLocationManager()
				.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
						mainActivity.getLoc().getUpdateIntervalTimeMilisec(),
						mainActivity.getRadius().floatValue() * 1000,
						mainActivity.getLoc());
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

	public class ImageAdapter extends BaseAdapter {
		private Context context;

		public ImageAdapter(Context c) {
			context = c;
		}

		// ---returns the number of images---
		public int getCount() {
			return photos.size();
		}

		// ---returns the ID of an item---
		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		// ---returns an ImageView view---
		public View getView(int position, View convertView, ViewGroup parent) {
			PhotoStreamImageView imageView;
			if (convertView == null) {
				imageView = new PhotoStreamImageView(context,
						photos.get(position));
				imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
				imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imageView.setPadding(5, 5, 5, 5);
			} else {
				imageView = (PhotoStreamImageView) convertView;
			}
			imageView.setImageBitmap(photos.get(position).getBitmap());
			return imageView;
		}
	}
}
