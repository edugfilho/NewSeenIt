package com.example.mobileseenit;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.mobileseenit.apis.FlickrSearchTask;

public class FlickrFragment extends Fragment implements OnTouchListener,
		OnClickListener {

	private static final String TAG = "FlickrFragment";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_flickr, container,
				false);
		 Button b = (Button) v.findViewById(R.id.searchFlickr);
	        b.setOnClickListener(this);
		return v;
	}

	// Search button
	public void searchFlickr(View view) {
		ConnectivityManager connMgr = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			FlickrSearchTask searchTask = new FlickrSearchTask(this);
			searchTask.execute();
			
			//get photos
			Log.i(TAG,"Photos Loaded");
			
		} else {
			Log.i(TAG, "No network connection available.");
		}
	}
	
	public void displayPhotos(ArrayList<Bitmap> photos)
	{
		for(Bitmap p : photos)
		{
			ImageView image = new ImageView(getActivity());
			image.setImageBitmap(p);
			image.setX(100);
			image.setY(100);
			RelativeLayout r = (RelativeLayout)getActivity().findViewById(R.id.flickr_relative_layout);
			r.addView(image);
			return;
		}
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
        case R.id.searchFlickr:
        	searchFlickr(v);
		}


	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
