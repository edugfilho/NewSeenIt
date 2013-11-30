package com.example.mobileseenit.apis;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.photos.GeoData;
import com.aetrion.flickr.uploader.UploadMetaData;

/**
 * Async task used to upload a photo with metadata to Flickr. Uses the FlickrJ
 * library.
 * 
 * @author dylanrunkel
 * 
 */
public class FlickrUploadTask extends AsyncTask<String, Void, String> {

	// Flickrj object
	Flickr f;
	Context context;
	GeoData location;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if (result != null) {
			if (result.equals("loginError")) {
				Toast.makeText(context,
						"Flickr upload failed: User not logged in or login failed!",
						Toast.LENGTH_LONG).show();
			} else{
				Toast.makeText(context, "Flickr upload successful!", Toast.LENGTH_LONG)
						.show();
				FlickrSetLocationTask f = new FlickrSetLocationTask(location,context,result);
				f.execute("");
			}
		}
	}

	// Data array of photo (done in fragment before calling)
	byte data[];

	// Any metadata (title etc) added to the image. Done in fragment.
	UploadMetaData meta;

	// Required for upload request
	RequestContext requestContext;

	/**
	 * Constructor to build this upload task. Calling classes must pass in all 3
	 * parameters.
	 * 
	 * @param flickrObject
	 *            - FlickrJ object from MainActivity
	 * @param data
	 *            - The preprocessed byte array of the photo to upload
	 * @param metaData
	 *            - meta data about the photo.
	 */
	public FlickrUploadTask(Flickr flickrObject, byte data[],
			UploadMetaData metaData, Context context, GeoData location) {
		this.data = data;
		this.meta = metaData;
		this.f = flickrObject;
		this.context = context;
		this.location = location;
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			return upload(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Actual upload logic. Prepares the request context with our Flickr Auth,
	 * and attempts the upload.
	 * 
	 * @param searchTerms
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public String upload(String... searchTerms) throws IOException,
			SAXException, ParserConfigurationException {

		requestContext = RequestContext.getRequestContext();
		requestContext.setAuth(f.getAuth());

		try {
			return f.getUploader().upload(data, meta);
		} catch (FlickrException e) {
			String err = e.getErrorCode();
			Log.i("error code", "error code: " + err);
			if (err.equals("99") || err.equals("98")) {
				return "loginError";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
