package com.example.mobileseenit.apis;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

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
import com.example.mobileseenit.FlickrFragment;

public class FlickrSearchTask extends AsyncTask<String, Void, String> {

	FlickrFragment g;
	Flickr f;
	ArrayList<Bitmap> photos;

	public FlickrSearchTask(FlickrFragment g) {

		this.g = g;
		// Setup flickrJ object
		try {
			f = new Flickr(
					// API key
					"af76271af34e193bd2f002eb32032e01",
					// Secret
					"b7b96e8ab7032484", new REST());
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
	private String search(String... searchTerms)
			throws IOException {

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
			//Search params search(SearchParameters params, int perPage, int page) 
			PhotoList photoList = photosInterface.search(searchParams, 1, 1);
			// get search result and fetch the photo object and get small square
			// imag's url
			if (photoList != null) {
				
				// Get search result and check the size of photo result
				for (int i = 0; i < photoList.size(); i++) {
					// get photo object
					Photo photo = (Photo) photoList.get(i);
					
					//construct url
					//http://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
					//http://farm9.staticflickr.com/8512/f88a24ede9.jpg
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
	            //in.reset();
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