package com.example.mobileseenit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragmentSeenIt extends Fragment {

	MainActivity mainActivity;
	GoogleMap gmap;
	MapView mMapView;
	ArrayList<Marker> markers;
	LatLng zoomIn;

	static final int MAX_ICON_WIDTH = 130;
	static final int MAX_ICON_HEIGHT = 100;
	
	final static long MAX_ZOOM = 21;
	final static long MED_ZOOM = 17;
	final static long LOW_ZOOM = 10;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_map, container,
				false);

		gmap = ((SupportMapFragment) getFragmentManager().findFragmentById(
				R.id.mapview)).getMap();

		mainActivity = (MainActivity) getActivity();

		markers = new ArrayList<Marker>();

		return rootView;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	// TODO optimize this
	@Override
	public void onResume() {
		super.onResume();

		if (zoomIn == null) {
			if (mainActivity.getLat() != null && mainActivity.getLng() != null) {
				gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						mainActivity.getLat(), mainActivity.getLng()), MED_ZOOM));
			} else {
				gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
						-19.825576, -43.950723), LOW_ZOOM));
			}
		}
	}

	public void reload() {
		gmap.clear();
		if (mainActivity.locationAcquired) {
			MarkerOptions opt1 = new MarkerOptions();
			opt1.position(new LatLng(mainActivity.getLat(), mainActivity
					.getLng()));
			markers.add(gmap.addMarker(opt1));
		}
		ArrayList<PhotoWrapper> photos = mainActivity.getPhotoList();
		Collections.sort(photos, new CustomComparator());
		int maxFavs = photos.get(photos.size() - 1).getFavs();
		for (PhotoWrapper photoWrapper : photos) {
			if (photoWrapper.getLat() != null) {
				MarkerOptions opt = new MarkerOptions();
				opt.position(new LatLng(photoWrapper.getLat(), photoWrapper
						.getLng()));
				//float coef = (float) photoWrapper.getFavs() / (float) maxFavs;
				int width = MAX_ICON_WIDTH;//Math.round(MAX_ICON_WIDTH * coef);
				int height = MAX_ICON_HEIGHT;//Math.round(MAX_ICON_HEIGHT * coef);
				Bitmap bmIcon = Bitmap.createScaledBitmap(
						photoWrapper.getBitmap(), width, height, true);
				opt.title(photoWrapper.getDetailMap().get(
						PhotoWrapper.TITLE_FIELD));
				opt.icon(BitmapDescriptorFactory.fromBitmap(bmIcon));

				markers.add(gmap.addMarker(opt));

			}
		}
		LatLng myLatLng = new LatLng(mainActivity.lat, mainActivity.lng);
		gmap.addCircle(new CircleOptions().center(myLatLng)
				.radius(mainActivity.radius * 1000 * 2).strokeColor(Color.BLUE)
				.fillColor(Color.TRANSPARENT));
		gmap.animateCamera(CameraUpdateFactory.newLatLng(myLatLng));
	}

	public class CustomComparator implements Comparator<PhotoWrapper> {
		@Override
		public int compare(PhotoWrapper o1, PhotoWrapper o2) {
			return Integer.valueOf(o1.getFavs()).compareTo(
					Integer.valueOf(o2.getFavs()));
		}
	}

	public LatLng getZoomIn() {
		return zoomIn;
	}

	public void setZoomIn(LatLng zoomIn) {
		this.zoomIn = zoomIn;
		gmap.moveCamera(CameraUpdateFactory.newLatLngZoom(zoomIn, MED_ZOOM));
		this.zoomIn = null;
	}
}
