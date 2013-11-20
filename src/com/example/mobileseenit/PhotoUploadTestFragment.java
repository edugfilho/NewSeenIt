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

public class PhotoUploadTestFragment extends Fragment implements OnTouchListener,
		OnClickListener {

	//Test photo to upload
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_photo_upload_test, container, false);

	
		return v;
	}

	@Override
	public void onClick(View v) {

		
	}


	@Override
	public void onResume() {
		super.onResume();

	
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
