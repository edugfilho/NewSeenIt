package com.example.mobileseenit;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mobileseenit.helpers.PhotoWrapper;
import com.google.android.gms.maps.model.LatLng;

public class ImageDialogSeenIt extends DialogFragment {
	MainActivity mainActivity;
	private Dialog mDialog;
	private String imgUrl;
	private ImageView imgView;
	private PhotoWrapper pWrapper;
	private Button btShowMap;

	public static ImageDialogSeenIt newInstance(PhotoWrapper p) {
		ImageDialogSeenIt f = new ImageDialogSeenIt();
		f.pWrapper = p;
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_dialog, container, false);

		mainActivity = (MainActivity) getActivity();
		
		// Get fields to fill
		imgView = (ImageView) v.findViewById(R.id.imageDialog);
		TextView title = (TextView) v.findViewById(R.id.photo_title);
		TextView link = (TextView) v.findViewById(R.id.photo_link);

		// Set image
		imgView.setImageBitmap(pWrapper.getBitmap());

		// Set details
		title.setText(pWrapper.getDetailMap().get(PhotoWrapper.TITLE_FIELD));

		// Create clickable link for original image
		link.setText(Html.fromHtml("<a href="
				+ pWrapper.getDetailMap().get(PhotoWrapper.LINK_FIELD)
				+ ">View Online</a> "));
		link.setMovementMethod(LinkMovementMethod.getInstance());

		// Show on map button
		btShowMap = (Button) v.findViewById(R.id.btnShowMap);
		btShowMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
				LatLng latLng = new LatLng(pWrapper.getLat(), pWrapper.getLng());
				mainActivity.mapFragment.setZoomIn(latLng);
				mainActivity.switchToFragment(mainActivity.mapFragment);
				
			}
		});

		return v;
	}

	public Dialog getmDialog() {
		return mDialog;
	}

	public void setmDialog(Dialog mDialog) {
		this.mDialog = mDialog;
	}

}