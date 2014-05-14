package com.example.mobileseenit;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mobileseenit.helpers.PhotoWrapper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragmentSeenIt extends Fragment {

	MainActivity mainActivity;
	GoogleMap gmap;
	MapView mMapView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_map, container,
				false);

		gmap = ((SupportMapFragment) getFragmentManager().findFragmentById(
				R.id.mapview)).getMap();

		mainActivity = (MainActivity) getActivity();
		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				-19.825576, -43.950723), 14.0f));

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	public void reload() {
		gmap.clear();
		if (mainActivity.locationAcquired) {
			MarkerOptions opt1 = new MarkerOptions();
			opt1.position(new LatLng(mainActivity.getLat(), mainActivity
					.getLng()));
			gmap.addMarker(opt1);
		}
		ArrayList<PhotoWrapper> photos = mainActivity.getPhotoList();
		for (PhotoWrapper photoWrapper : photos) {
			if (photoWrapper.getLat() != null) {
				MarkerOptions opt = new MarkerOptions();
				opt.position(new LatLng(photoWrapper.getLat(), photoWrapper
						.getLng()));
				Bitmap bmIcon = Bitmap.createScaledBitmap(
						photoWrapper.getBitmap(), 100, 100, true);
				opt.title(photoWrapper.getDetailMap().get(
						PhotoWrapper.TITLE_FIELD));
				opt.icon(BitmapDescriptorFactory.fromBitmap(bmIcon));
				gmap.addMarker(opt);

			}
		}
		LatLng myLatLng = new LatLng(mainActivity.lat, mainActivity.lng);
		gmap.addCircle(new CircleOptions()
	     .center(myLatLng)
	     .radius(mainActivity.radius*1000*2)
	     .strokeColor(Color.BLUE)
	     .fillColor(Color.TRANSPARENT));
		gmap.animateCamera(CameraUpdateFactory.newLatLng(myLatLng));
	}
}
