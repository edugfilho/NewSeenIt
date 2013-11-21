package com.example.mobileseenit.helpers;

import com.example.mobileseenit.ImageDialogSeenIt;
import com.example.mobileseenit.MainActivity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Custom class to centralize ImageView style and creation.
 * Imageviews are used to display the photos returned 
 * from the APIs.
 * @author dylanrunkel
 */
public class PhotoStreamImageView extends ImageView implements OnClickListener {

	//Style Properties
	private int marginBottom = 20;
	private int marginTop = 20;
	private int marginLeft = 0;
	private int marginRight = 0;
	private float width = 280; //in dp!
	
	//Contains a PhotoWrapper that contains
	//the photo bitmap and metadeta
	private PhotoWrapper pWrapper;

	
	/**
	 * Constructor to build the ImageView. Sets the style
	 * info defined above.
	 * @param context
	 * @param p
	 */
	public PhotoStreamImageView(Context context, PhotoWrapper p) {
		super(context);
		
		//Set image
		setImageBitmap(p.getBitmap());
		pWrapper = p;
		
		//set margins
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(marginLeft, marginTop, marginRight, marginBottom);
		
		//Width
		lp.width = dpToPixel(width);
		
		//Add action so user can click image and
		//load image details activity
		setClickable(true);
		setOnClickListener(this);
		
		setLayoutParams(lp);		
	}
	
	
	//Helper method to convert dp to pixels.
	private int dpToPixel(float dp)
	{
		final float scale = getContext().getResources().getDisplayMetrics().density;
		int pixels = (int) (dp * scale + 0.5f);
		return pixels;
	}



	//User can click the picture to bring up the image details.
	@Override
	public void onClick(View v) {
		//TODO Log.
		FragmentTransaction ft = ((MainActivity)getContext()).getFragmentManager()
				.beginTransaction();
		ImageDialogSeenIt newFragment = ImageDialogSeenIt.newInstance(this.pWrapper);
		newFragment.show(ft, "dialog");
	}

}
