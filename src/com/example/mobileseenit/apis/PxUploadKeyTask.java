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
import com.aetrion.flickr.uploader.UploadMetaData;
/**

 * @author dylanrunkel
 * 
 */
public class PxUploadKeyTask extends AsyncTask<String, Void, String>{


	Context context;

	
	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result!=null){
			if(result.equals("loginError")){
				Toast.makeText(context, "Upload failed: User not logged in or login failed!",
					     Toast.LENGTH_LONG).show();
			}
			else
				Toast.makeText(context, "Upload successful!",
					     Toast.LENGTH_LONG).show();
		}
	}

	// Data array of photo (done in fragment before calling)
	byte data[];

	// Any metadata (title etc) added to the image. Done in fragment.
	UploadMetaData meta;

	// Required for upload request
	RequestContext requestContext;


	public PxUploadKeyTask(Flickr flickrObject, byte data[],
			UploadMetaData metaData, Context context) {
		this.data = data;
		this.meta = metaData;
		this.context = context;
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


	public String upload(String... searchTerms) throws IOException,
			SAXException, ParserConfigurationException {

		requestContext = RequestContext.getRequestContext();
		return null;
	}

}
