package com.example.mobileseenit;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class SeenItLocation implements LocationListener {

	// 1h
	private final int updateIntervalTimeMilisec = 60 * 60 * 1000;
	Timer timer1;
	private LocationManager locationManager;
	private LocationResult locationResult;
	boolean gps_enabled = false;
	boolean network_enabled = false;
	private Context context;

	public SeenItLocation(Context context) {
		this.context = context;
		// Get the location manager
		if (locationManager == null) {
			locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
		}

		try {
			gps_enabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		try {
			network_enabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}
	}

	public boolean getLocation(Context context, LocationResult result) {

		locationResult = result;

		// don't start listeners if no provider is enabled
		if (!gps_enabled && !network_enabled) {
			return false;
		}

		// timer1 = new Timer();
		// timer1.schedule(new GetLastLocation(), 3000);

		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		getLocationManager().removeUpdates(this);
		locationResult.gotLocation(location);

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	class GetLastLocation extends TimerTask {
		@Override
		public void run() {

			// locationManager.removeUpdates(locationListenerGps);
			// locationManager.removeUpdates(locationListenerNetwork);

			Location net_loc = null, gps_loc = null;
			if (gps_enabled)
				gps_loc = locationManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			if (network_enabled)
				net_loc = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			// if there are both values use the latest one
			if (gps_loc != null && net_loc != null) {
				if (gps_loc.getTime() > net_loc.getTime())
					locationResult.gotLocation(gps_loc);
				else
					locationResult.gotLocation(net_loc);
				return;
			}

			if (gps_loc != null) {
				locationResult.gotLocation(gps_loc);
				return;
			}
			if (net_loc != null) {
				locationResult.gotLocation(net_loc);
				return;
			}
			locationResult.gotLocation(null);
		}
	}

	public static abstract class LocationResult {
		public abstract void gotLocation(Location location);

	}

	public LocationManager getLocationManager() {
		if (locationManager == null) {
			locationManager = (LocationManager) context
					.getSystemService(Context.LOCATION_SERVICE);
		}
		return locationManager;
	}

	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public int getUpdateIntervalTimeMilisec() {
		return updateIntervalTimeMilisec;
	}

}
