package com.example.mobileseenit.apis;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;
import com.aetrion.flickr.photos.PhotosInterface;
import com.aetrion.flickr.photos.SearchParameters;
import com.aetrion.flickr.photos.geo.GeoInterface;
import com.example.mobileseenit.MainActivity;
import com.example.mobileseenit.helpers.PhotoWrapper;

/**
 * Async task to search Flickr for images based on the current location.
 * 
 * Uses the FlickrJ library.
 * 
 * @author dylanrunkel
 * 
 */
public class FlickrSearchTask extends AsyncTask<String, Void, String> {

	// Number of images to load at once.
	private int loadCount;

	// Reference the activity
	private MainActivity mainActivity;

	// Our FlickrJ object
	private Flickr flickr;

	// Array of photowrappers to transport photos
	private LinkedList<PhotoWrapper> photos;

	private ProgressDialog dialog;

	/** progress dialog to show user that the backup is processing. */
	/** application context. */
	@Override
	protected void onPreExecute() {
		dialog = ProgressDialog.show(mainActivity, "", "Loading photos, please wait...",
				false);
	}

	/**
	 * Constructor just to get the MainActivity and it's Flickr Object
	 * 
	 * @param mainActivity
	 */
	public FlickrSearchTask(Activity mainActivity) {
		loadCount = ((MainActivity) mainActivity).getNumberOfPhotos();
		// get ref to Main Activity
		this.mainActivity = (MainActivity) mainActivity;

		// Take the shared flickr object
		flickr = this.mainActivity.getFlickr();
	}

	// Async task - call the method to run.
	@Override
	protected String doInBackground(String... searchTerms) {
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

		// initialize SearchParameter object, this object stores the search
		// keyword
		SearchParameters searchParams = new SearchParameters();
		searchParams.setSort(SearchParameters.INTERESTINGNESS_DESC);

		// searchParams.setTags(tags);
		try {
			searchParams.setMedia("photos");
		} catch (FlickrException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		searchParams.setHasGeo(true);
		searchParams.setLatitude(mainActivity.getLat().toString());
		searchParams.setLongitude(mainActivity.getLng().toString());
		searchParams.setRadius(mainActivity.getRadius().intValue());
		searchParams.setRadiusUnits("km");

		if (mainActivity.useDateRange) {
			searchParams.setMinTakenDate(mainActivity.getImgsAfter().getTime());
			searchParams
					.setMaxTakenDate(mainActivity.getImgsBefore().getTime());
		}

		// Initialize Photos & geo Interface object
		PhotosInterface photosInterface = flickr.getPhotosInterface();
		GeoInterface geoInterface = flickr.getGeoInterface();

		// Execute search with entered tags
		try {

			PhotoList photoList = photosInterface.search(searchParams,
					loadCount, 1);
			// get search result and fetch the photo object and get small square
			// imag's url
			if (photoList != null) {

				// Get search result and check the size of photo result
				for (int i = 0; i < photoList.size(); i++) {
					// get photo object
					Photo photo = (Photo) photoList.get(i);

					// Taking advantage of a deprecated field to store favs
					// count. This is really nasty.
					int favs = photosInterface.getFavorites(photo.getId(), 100,
							1).size();
					photo.setViews(favs);

					// Adds geoinfo to the photo
					photo.setGeoData(geoInterface.getLocation(photo.getId()));

					// Checks if the photo isn't already in the photoList before
					// adding

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
					// TODO \/ hardcoded for small images. Consider using
					// photo.getSmallUrl instead
					urlBuilder.append("_m");
					urlBuilder.append(".jpg");

					Bitmap b = process(urlBuilder.toString());
					b.getByteCount();
					// Construct new photowrapper for each image.
					PhotoWrapper newPhoto = new PhotoWrapper(b,
							photoList.get(i), PhotoWrapper.FLICKR_OBJECT);
					photos.add(newPhoto);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "done";
	}

	/**
	 * Fetch the bitmap corresponding the image at the supplied url.
	 * 
	 * @param url
	 * @return Bitmap
	 */
	protected Bitmap process(String url) {
		String urldisplay = url;
		Bitmap mIcon11 = null;
		try {
			InputStream in = new java.net.URL(urldisplay).openStream();
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
		// TODO keep it?\/
		if (photos.isEmpty()) {
			Toast.makeText(mainActivity, "Flickr didn't find anything",
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mainActivity, "Flickr photos loaded",
					Toast.LENGTH_SHORT).show();
		}
		mainActivity.addPhotos(photos);
		dialog.dismiss();

	}

}