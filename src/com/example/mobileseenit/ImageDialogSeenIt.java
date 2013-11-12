package com.example.mobileseenit;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class ImageDialogSeenIt extends DialogFragment {
	private Dialog mDialog;
	private String imgUrl;
	private ImageView imgView;

	static ImageDialogSeenIt newInstance(String imgUrl) {
		ImageDialogSeenIt f = new ImageDialogSeenIt();
		f.imgUrl = imgUrl;
		return f;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_dialog, container, false);
		imgView = (ImageView) v.findViewById(R.id.imageDialog);
		UrlImageViewHelper.setUrlDrawable(imgView, imgUrl);
		return v;
	}

	public Dialog getmDialog() {
		return mDialog;
	}

	public void setmDialog(Dialog mDialog) {
		this.mDialog = mDialog;
	}

}
