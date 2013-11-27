package com.example.mobileseenit;

import java.io.File;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class ImageUploadFragment extends Fragment implements OnClickListener {
	private CaptureFragmentListener listener;
	private String path;
	private Button back;
	private Button upload;
	private byte[] data;
	private double lat;
	private double lon;
	private Bitmap myImage;
	private ImageView imageview;
	
	static ImageUploadFragment newInstance(byte[] image, double[] loc, String path){
		ImageUploadFragment f = new ImageUploadFragment();
		Bundle args = new Bundle();
		args.putByteArray("image", image);
		args.putDoubleArray("location", loc);
		args.putString("path", path);
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
		imageview = (ImageView) rootView.findViewById(R.id.myImage);
		back = (Button) rootView.findViewById(R.id.retake);
		upload = (Button) rootView.findViewById(R.id.upload);
		back.setOnClickListener(this);
		upload.setOnClickListener(this);
		Bundle args = this.getArguments();
		data = args.getByteArray("image");
		double[] loc = args.getDoubleArray("location");
		lat = loc[0];
		lon = loc[1];
		this.path = args.getString("path");
		Log.i("path", path);
		myImage = BitmapFactory.decodeFile(path);
		double height = myImage.getHeight();
		double width = myImage.getWidth();
		double ratio = width/height;
		Log.i("ratio", "ratio: " + ratio);
		RelativeLayout.LayoutParams p = (RelativeLayout.LayoutParams)imageview.getLayoutParams();
		p.height = (int) (p.width/ratio);
		imageview.setImageBitmap(myImage);
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
