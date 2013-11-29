package com.example.mobileseenit;

import android.graphics.Bitmap;


public interface CaptureFragmentListener {
	void onSwitchToUpload(byte[] data, double[] loc, String path);
	void onSwitchToCapture();
}
