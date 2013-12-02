package com.example.mobileseenit.apis;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.mobileseenit.MainActivity;
import com.example.mobileseenit.helpers.PhotoWrapper;
import com.fivehundredpx.api.PxApi;

public class PxSearchTask extends AsyncTask<String, Void, String> {

	// Number of images to load at once.
	private static final int LOAD_COUNT = 7;

	// Reference to the calling fragment (flickr)
	private MainActivity g;

	// 500px API object
	private PxApi pxApi = null;

	// Array of bitmaps to return to the fragment
	private LinkedList<PhotoWrapper> photos;

	public PxSearchTask(Activity g) {

		this.g = (MainActivity) g;
		pxApi = new PxApi("dZA3TRGzQovCSMnmKrTlg9wqWl14MeYpUSxbLGDm");
	}

	@Override
	protected String doInBackground(String... searchTerms) {

		// params comes from the execute() call. Will be the search terms.
		try {
			return search(searchTerms);
		} catch (IOException e) {
			return "Unable to retrieve web page. URL may be invalid.";
		}
	}

	// The actual search method
	private String search(String... searchTerms) throws IOException {

		// Initialize list of imageviews
		photos = new LinkedList<PhotoWrapper>();
		String search = "/photos/search?image_size=4&geo="
				+ g.getLat().toString() + "," + g.getLng().toString() + ","
				+ g.getRadius() + "km&rpp=" + LOAD_COUNT;
		JSONObject j = pxApi.get(search);
		JSONArray photoJsonArray;
		try {
			photoJsonArray = j.getJSONArray("photos");
			for (int i = 0; i < photoJsonArray.length(); i++) {
				JSONObject tempObject = (JSONObject) photoJsonArray.get(i);
				String url = tempObject.getString("image_url");

				Bitmap b = process(url);
				PhotoWrapper p = new PhotoWrapper(b, tempObject,
						PhotoWrapper.PX_OBJECT);
				photos.add(p);

			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "done";
	}

	protected Bitmap process(String url) {
		String urldisplay = url;
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
			// in.reset();
			mIcon11 = BitmapFactory.decodeStream(in);
		} catch (Exception e) {
			Log.e("Error", e.getMessage());
			e.printStackTrace();
		}
		return mIcon11;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {
		// TODO keep it? \/
		if (photos.isEmpty()) {
			Toast.makeText(g, "500px didn't find anything", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(g, "500px photos loaded", Toast.LENGTH_SHORT).show();
		}
		g.addPhotos(photos);
	}

}