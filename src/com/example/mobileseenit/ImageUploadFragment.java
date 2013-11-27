package com.example.mobileseenit;

import java.io.File;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class ImageUploadFragment extends Fragment implements OnClickListener {
	private CaptureFragmentListener listener;
	private File myImg;
	private Location mLoc;
	private Button back;
	private Button upload;
	
	static ImageUploadFragment newInstance(byte[] image, double[] loc){
		ImageUploadFragment f = new ImageUploadFragment();
		Bundle args = new Bundle();
		args.putByteArray("image", image);
		args.putDoubleArray("location", loc);
		f.setArguments(args);
		return f;
	}

	public void setListener(CaptureFragmentListener listener){
		this.listener = listener;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.fragment_image_upload,
				container, false);
		back = (Button) rootView.findViewById(R.id.retake);
		upload = (Button) rootView.findViewById(R.id.upload);
		back.setOnClickListener(this);
		upload.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view==back){
			listener.onSwitchToCapture();
		}
	}
	
}
