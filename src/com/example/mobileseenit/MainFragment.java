package com.example.mobileseenit;

import java.util.ArrayList;

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

import com.example.mobileseenit.helpers.PhotoStreamImageView;
import com.example.mobileseenit.helpers.PhotoWrapper;

public class MainFragment extends Fragment implements OnTouchListener,  OnClickListener{

	ArrayList<PhotoWrapper> photos= null;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_main,
				container, false);
		
		//Display images
		MainActivity mainActivity = (MainActivity) getActivity();
		ArrayList<PhotoWrapper> photos = mainActivity.getPhotoList();
		if(photos != null && photos.size() > 0 )
		{
			//Hide progress spinner
			ProgressBar progressBar = (ProgressBar)rootView.findViewById(R.id.photo_load_progress_bar);
			progressBar.setVisibility(View.GONE);
			
			for (PhotoWrapper p : photos ) {

				LinearLayout r = (LinearLayout) rootView.findViewById(R.id.photo_stream);
				PhotoStreamImageView newImage = new PhotoStreamImageView(getActivity(), p);
				r.addView(newImage);
			}
		}
				
		return rootView;
	}
	
	public void updateDisplayedPhotos()
	{
		//Hide progress spinner
		ProgressBar progressBar = (ProgressBar)getView().findViewById(R.id.photo_load_progress_bar);
		progressBar.setVisibility(View.GONE);
		
		//Display images
		MainActivity mainActivity = (MainActivity) getActivity();
		ArrayList<PhotoWrapper> photos = mainActivity.getPhotoList();
		for (PhotoWrapper p : photos ) {

			LinearLayout r = (LinearLayout) getView().findViewById(R.id.photo_stream);
			PhotoStreamImageView newImage = new PhotoStreamImageView(getActivity(), p);
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

}
