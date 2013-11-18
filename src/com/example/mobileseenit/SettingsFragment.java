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
import android.widget.Button;
import android.widget.ImageButton;

import com.example.mobileseenit.apis.FlickrLoginDialog;

public class SettingsFragment extends Fragment implements OnTouchListener,  OnClickListener{

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_settings,
				container, false);
		
		//Set Add Flickr Account button click handler
		 ImageButton b = (ImageButton) v.findViewById(R.id.add_flickr_account_button);
	        b.setOnClickListener(this);
	        
		return v;
	}
	@Override
	public void onClick(View v) {
		
		//Handle add_flickr_account button
		if(v.getId() == R.id.add_flickr_account_button)
		{
			FragmentTransaction ft = ((MainActivity) v.getContext()).getFragmentManager()
			.beginTransaction();
			FlickrLoginDialog newFragment = FlickrLoginDialog.newInstance();
			newFragment.show(ft, "dialog");
		}
		
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}
	


}
