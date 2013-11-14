package com.example.mobileseenit;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Custom class to centralize imageview style and creation.
 *
 */
public class PhotoStreamImageView extends ImageView {

	//Style Properties
	int marginBottom = 20;
	int marginTop = 20;
	int marginLeft = 0;
	int marginRight = 0;
	float width = 280; //in dp!

	
	public PhotoStreamImageView(Context context, Bitmap p) {
		super(context);
		
		//Set image
		setImageBitmap(p);
		
		//set margins
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		
		//Width
		lp.width = dpToPixel(width);
		
		setLayoutParams(lp);		
	}
	
	private int dpToPixel(float dp)
	{
		final float scale = getContext().getResources().getDisplayMetrics().density;
		int pixels = (int) (dp * scale + 0.5f);
		return pixels;
	}

}
