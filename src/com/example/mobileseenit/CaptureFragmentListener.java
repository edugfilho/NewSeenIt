package com.example.mobileseenit;


public interface CaptureFragmentListener {
	void onSwitchToUpload(byte[] data, double[] loc, String path);
	void onSwitchToCapture();
}