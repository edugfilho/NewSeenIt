package com.example.mobileseenit.apis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;
import com.example.mobileseenit.MainActivity;

public class FlickrSearchTask extends AsyncTask<String, Void, String> {

	// Number of images to load at once.
	private static final int LOAD_COUNT = 5;

	// API key
	private static final String API_KEY = "af76271af34e193bd2f002eb32032e01";

	// Secret Key
	private static final String SECRET_KEY = "b7b96e8ab7032484";

	// Reference to the calling fragment (flickr)
	private MainActivity g;

	// Our FlickrJ object
	private Flickr f;

	// Array of bitmaps to return to the fragment
	private ArrayList<Bitmap> photos;

	public FlickrSearchTask(Activity g) {

		this.g = (MainActivity) g;
		// Setup flickrJ object
		try {
			f = new Flickr(API_KEY, SECRET_KEY, new REST());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		photos = new ArrayList<Bitmap>();

		// initialize SearchParameter object, this object stores the search
		// keyword
		SearchParameters searchParams = new SearchParameters();
		searchParams.setSort(SearchParameters.INTERESTINGNESS_DESC);

		// Create tag keyword array
		String[] tags = new String[] { "Dog", "Beagle" };
		searchParams.setTags(tags);

		// Initialize PhotosInterface object
		PhotosInterface photosInterface = f.getPhotosInterface();
		// Execute search with entered tags
		try {
			// Search params search(SearchParameters params, int perPage, int
			// page)
			PhotoList photoList = photosInterface.search(searchParams,
					LOAD_COUNT, 1);
			// get search result and fetch the photo object and get small square
			// imag's url
			if (photoList != null) {

				// Get search result and check the size of photo result
				for (int i = 0; i < photoList.size(); i++) {
					// get photo object
					Photo photo = (Photo) photoList.get(i);

					// construct url
					// http://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
					StringBuilder urlBuilder = new StringBuilder();
					urlBuilder.append("http://farm");
					urlBuilder.append(photo.getFarm());
					urlBuilder.append(".staticflickr.com/");
					urlBuilder.append(photo.getServer());
					urlBuilder.append("/");
					urlBuilder.append(photo.getId());
					urlBuilder.append("_");
					urlBuilder.append(photo.getSecret());
					urlBuilder.append(".jpg");
					Bitmap b = process(urlBuilder.toString());
					photos.add(b);
				}
			}
		} catch (Exception e) {
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
		Log.i("alal", result);
		g.displayPhotos(photos);
	}

}