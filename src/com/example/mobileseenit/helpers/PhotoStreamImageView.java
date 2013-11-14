package com.example.mobileseenit.helpers;

import com.example.mobileseenit.ImageDialogSeenIt;
import com.example.mobileseenit.MainActivity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Custom class to centralize imageview style and creation.
 *
 */
public class PhotoStreamImageView extends ImageView implements OnClickListener {

	//Style Properties
	int marginBottom = 20;
	int marginTop = 20;
	int marginLeft = 0;
	int marginRight = 0;
	float width = 280; //in dp!
	
	PhotoWrapper pWrapper;

	
	public PhotoStreamImageView(Context context, PhotoWrapper p) {
		super(context);
		
		//Set image
		setImageBitmap(p.getBitmap());
		pWrapper = p;
		
		//set margins
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		
		//Width
		lp.width = dpToPixel(width);
		
		//Add action so user can click image and
		//load image details activity
		setClickable(true);
		setOnClickListener((OnClickListener) this);
		
		setLayoutParams(lp);		
	}
	
	
	
	private int dpToPixel(float dp)
	{
		final float scale = getContext().getResources().getDisplayMetrics().density;
		int pixels = (int) (dp * scale + 0.5f);
		return pixels;
	}



	//User can click the picture to bring up the image details.
	@Override
	public void onClick(View v) {
		System.out.println("Clicked me!");
		System.out.println(this.pWrapper.getDetailMap().get(PhotoWrapper.TITLE_FIELD));
		
		FragmentTransaction ft = ((MainActivity)getContext()).getFragmentManager()
				.beginTransaction();
		ImageDialogSeenIt newFragment = ImageDialogSeenIt.newInstance(this.pWrapper);
		newFragment.show(ft, "dialog");
	}

}
