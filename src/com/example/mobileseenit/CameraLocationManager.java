package com.example.mobileseenit;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

public class CameraLocationManager {
	
	private LocationManager mLocationManager;
	private Context context;
	boolean isValid = false;
	
	CameraLocationListener [] mLocationListeners = new CameraLocationListener[]{
		new CameraLocationListener(LocationManager.GPS_PROVIDER),
		new CameraLocationListener(LocationManager.NETWORK_PROVIDER),
	};
	
	public CameraLocationManager(Context context){
		this.context = context;
	}
	public Location getLocation(){
		
		for(int i = 0; i<mLocationListeners.length;i++){
			if(mLocationListeners[i].getLocation()!=null)
				return mLocationListeners[i].getLocation();
		}
		return null;
		
	}
	void startRecordLocations() {
		if(mLocationManager==null){
			mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		}
		try{
			mLocationManager.requestLocationUpdates
			(LocationManager.NETWORK_PROVIDER, 2000, 0, mLocationListeners[1]);
		} catch (Exception ex){
			Log.d("exception", null, ex);
		}
		try{
			mLocationManager.requestLocationUpdates
			(LocationManager.GPS_PROVIDER, 360000, 0, mLocationListeners[0]);
		} catch (Exception ex){
			Log.d("exception", null, ex);
		}
	}
	
	void stopRecordLocations() {
		if(mLocationManager!=null){
			try{
				mLocationManager.removeUpdates(mLocationListeners[0]);
				mLocationManager.removeUpdates(mLocationListeners[1]);
			} catch(Exception ex){
				Log.d("exception", null, ex);
			}
		}
	}
	private class CameraLocationListener implements LocationListener{
		String mProvider;
		Location myLocation;
		public CameraLocationListener(String mProvider){
			this.mProvider = mProvider;
			myLocation = new Location(this.mProvider);
			
		}
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			myLocation.set(location);
			isValid = true;
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			isValid = false;
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			switch(status){
				case LocationProvider.OUT_OF_SERVICE:
				case LocationProvider.TEMPORARILY_UNAVAILABLE:
					isValid = false;
			}
		}
		public Location getLocation(){
			boolean hasLatLong;
			if((myLocation.getLatitude()!=0.0)||(myLocation.getLongitude()!=0.0))
				hasLatLong = true;
			else
				hasLatLong = false;
			if(isValid&&hasLatLong)
				return myLocation;
			else
				return null;
		}
		
	}

}
