package com.example.mobileseenit.apis;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import android.os.AsyncTask;
import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.RequestContext;
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
			UploadMetaData metaData) {
		this.data = data;
		this.meta = metaData;
		this.f = flickrObject;
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
			f.getUploader().upload(data, meta);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
