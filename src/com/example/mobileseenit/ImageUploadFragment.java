package com.example.mobileseenit;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.uploader.UploadMetaData;
import com.example.mobileseenit.apis.FlickrUploadTask;

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
	private EditText title;
	private EditText description;
	private TextView tv_location;
	private CheckBox cb_flickr;
	private CheckBox cb_500px;
	private Flickr f;
	
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
		title = (EditText) rootView.findViewById(R.id.et_title);
		description = (EditText) rootView.findViewById(R.id.et_decription);
		tv_location = (TextView) rootView.findViewById(R.id.tv_location);
		cb_flickr = (CheckBox) rootView.findViewById(R.id.cb_flickr);
		cb_500px = (CheckBox) rootView.findViewById(R.id.cb_500px);
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
		Geocoder g = new Geocoder(this.getActivity(), Locale.getDefault());
		try {
			List<Address> list = g.getFromLocation(lat, lon, 1);
			String address = "";
			for(int i=0; i<list.get(0).getMaxAddressLineIndex();i++){
				address = address + list.get(0).getAddressLine(i) + " ";
			}
			tv_location.setText(address);
		} catch(IOException e){
			Log.d("geocoder err", "err: " + e);
		}
		return rootView;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if(view==back){
			listener.onSwitchToCapture();
		}
		else if(view==upload){
			Log.i("upload", "upload");
			if(!cb_flickr.isChecked()&&!cb_500px.isChecked()){
				Toast.makeText(this.getActivity(), "Please select Flickr and/or 500px to upload to.",
					     Toast.LENGTH_LONG).show();
			}
			else{
				if(cb_flickr.isChecked()){
					uploadToFlickr();
				}
				if(cb_500px.isChecked()){
					uploadTo500px();
				}
				InputMethodManager imm = (InputMethodManager)this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
				listener.onSwitchToCapture();
			}
			
		}
	}
	private void uploadToFlickr(){
		f = ((MainActivity)this.getActivity()).getFlickr();
		UploadMetaData uploadMetaData = new UploadMetaData();
		uploadMetaData.setTitle(title.getText().toString());
		uploadMetaData.setDescription(description.getText().toString());
		FlickrUploadTask t = new FlickrUploadTask(f,data,uploadMetaData,this.getActivity());
		t.execute("");
	}
	private void uploadTo500px(){
		
		//Place your uploadTo500px code here.
		
		
	}
	
}
