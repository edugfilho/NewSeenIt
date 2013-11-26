package com.example.mobileseenit;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.uploader.UploadMetaData;
import com.aetrion.flickr.uploader.Uploader;
import com.example.mobileseenit.apis.FlickrUploadTask;

public class PhotoUploadTestFragment extends Fragment implements
		OnTouchListener, OnClickListener {

	Flickr f;
	String photo = "llama.png";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_photo_upload_test,
				container, false);

		// Set Upload button click handler
		Button b = (Button) v.findViewById(R.id.upload_test_button);
		b.setOnClickListener(this);

		return v;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.upload_test_button) {
			
			f = ((MainActivity) getActivity()).getFlickr();
			System.out.println();
			UploadMetaData uploadMetaData = new UploadMetaData();
			uploadMetaData.setTitle("hello Dylan");

			Uploader u = f.getUploader();

			// Get data into stream
			ByteArrayOutputStream out = new ByteArrayOutputStream();

			InputStream in;
			try {
				in =getResources().openRawResource(R.raw.llama_613_600x450) ;
				int i;
				byte[] buffer = new byte[1024];
				while ((i = in.read(buffer)) != -1) {
					out.write(buffer, 0, i);
				}
				in.close();

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			byte data[] = out.toByteArray();
			
			FlickrUploadTask t = new FlickrUploadTask(f,data, uploadMetaData);
			t.execute("");

		}

	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

}
