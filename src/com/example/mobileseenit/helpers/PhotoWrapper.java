package com.example.mobileseenit.helpers;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Bitmap;

import com.aetrion.flickr.photos.Photo;

/**
 * Wrapper class to hold the bitmap of the photo and its related information.
 * 
 */
public class PhotoWrapper {

	// Type constants
	public static final String FLICKR_OBJECT = "FLICKR_OBJECT_TYPE";
	public static final String INSTAGRAM_OBJECT = "INSTAGRAM_OBJECT_TYPE";
	public static final String PX_OBJECT = "500PX_OBJECT_TYPE";

	// Field constants
	public static final String TITLE_FIELD = "TITLE_FIELD_KEY"; // title/caption
	public static final String LINK_FIELD = "LINK_FIELD_KEY";

	private Bitmap bitmap;
	private HashMap<String, String> detailMap;

	// Takes in a bitmap of the image, the photo object returned from the api,
	// and a string constant type
	public PhotoWrapper(Bitmap p, Object rawPhoto, String type) {
		this.bitmap = p;

		if (type.equals(FLICKR_OBJECT)) {
			detailMap = processFlickr(rawPhoto);
		} else if (type.equals(INSTAGRAM_OBJECT)) {
			detailMap = processInstagrm(rawPhoto); 
		} else if (type.equals(PX_OBJECT)) {
			detailMap = processPx(rawPhoto);
		}
	}

	// Convert the object returned by Flickr api to detail map
	private HashMap<String, String> processFlickr(Object o) {
		HashMap<String, String> details = new HashMap<String, String>();

		Photo flickrPhoto = (Photo) o;
		details.put(TITLE_FIELD, flickrPhoto.getTitle());
		details.put(LINK_FIELD, flickrPhoto.getUrl());

		return details;
	}

	// Convert Instagram api object to details map
	private HashMap<String, String> processInstagrm(Object o) {
		// TODO whatever you need to for instagram
		HashMap<String, String> details = new HashMap<String, String>();

		details.put(TITLE_FIELD, "dummycaption");
		details.put(LINK_FIELD, "dummylink");

		return details;
	}

	// Convert 500px json object to fields
	private HashMap<String, String> processPx(Object o) {

		// Grab the object
		JSONObject jsonPhoto = (JSONObject) o;
		HashMap<String, String> details = new HashMap<String, String>();

		try {
			details.put(TITLE_FIELD, jsonPhoto.getString("description"));
			details.put(LINK_FIELD, jsonPhoto.getString("image_url"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return details;

	}

	// Access Methods
	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public HashMap<String, String> getDetailMap() {
		return detailMap;
	}

	public void setDetailMap(HashMap<String, String> detailMap) {
		this.detailMap = detailMap;
	}

}
