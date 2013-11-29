package com.example.mobileseenit.apis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.widget.Toast;

import com.aetrion.flickr.RequestContext;
import com.example.mobileseenit.MainActivity;
import com.example.mobileseenit.R;
import com.fivehundredpx.api.PxApi;
import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.api.auth.OAuthProvider;
/**

 * @author dylanrunkel
 * 
 */
public class PxUploadKeyTask extends AsyncTask<String, Void, String>{


	MainActivity context;

	
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
	HashMap<String,String> meta;

	// Required for upload request
	RequestContext requestContext;


	public PxUploadKeyTask(byte[] data, HashMap<String,String> metaData, Context context) {
		this.data = data;
		this.meta = metaData;
		this.context =(MainActivity) context;
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

		//Get token
		AccessToken token = context.getPxUser();
		
		
		Resources r = context.getResources();
		PxApi px = new PxApi(r.getString( R.string.px_consumer_key));
		List params = new ArrayList();
		params.add(new BasicNameValuePair("description", "testDesc"));
		params.add(new BasicNameValuePair("name", "testName"));
		params.add(new BasicNameValuePair("category", "0"));
		px.post("/photos",params);
		
		return "done";
	}

}
