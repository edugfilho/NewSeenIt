package com.example.mobileseenit.apis;



import com.example.mobileseenit.R;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FlickrLoginDialog extends DialogFragment{

	private Dialog mDialog;
	private String imgUrl;
	private ImageView imgView;

	public static FlickrLoginDialog newInstance() {
		FlickrLoginDialog f = new FlickrLoginDialog();
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		
		return v;
	}
	
}
