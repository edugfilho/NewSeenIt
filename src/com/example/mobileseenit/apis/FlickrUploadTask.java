package com.example.mobileseenit.apis;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import android.os.AsyncTask;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.auth.Permission;
import com.aetrion.flickr.uploader.UploadMetaData;

public class FlickrUploadTask extends AsyncTask<String, Void, String> {

	Flickr f;

	byte data[] ;
	UploadMetaData meta;
	RequestContext requestContext;

	
	public FlickrUploadTask(Flickr flickrObject, byte data[], UploadMetaData m) {

		
		this.data = data;
		this.meta = m;
		this.f = flickrObject;
	}

	@Override
	protected String doInBackground(String... params) {
		// params comes from the execute() call. Will be the search terms.
		try {
			return upload(params);

		} catch (IOException e) {
			return "Unable to retrieve web page. URL may be invalid.";
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public String upload(String... searchTerms) throws IOException,
			SAXException, ParserConfigurationException {
		
		requestContext = RequestContext.getRequestContext();
		requestContext.setAuth(f.getAuth());

		try {
			f.getAuth().setPermission(Permission.WRITE);
			f.getUploader().upload(data, meta);
		} catch (Exception e) {
			e.printStackTrace();
		}

	

		return "done";
	}

}
