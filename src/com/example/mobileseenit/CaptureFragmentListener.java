package com.example.mobileseenit;

import java.io.File;

import android.location.Location;

public interface CaptureFragmentListener {
	void onSwitchToUpload(byte[] data, double lat, double lon);
	void onSwitchToCapture();
}
