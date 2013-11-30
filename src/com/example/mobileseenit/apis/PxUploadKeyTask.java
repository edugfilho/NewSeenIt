package com.example.mobileseenit.apis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.uploader.UploadMetaData;
import com.example.mobileseenit.MainActivity;
import com.example.mobileseenit.R;
import com.fivehundredpx.api.PxApi;
/**

 * @author dylanrunkel & YixuanLi
 * 
 */
public class PxUploadKeyTask extends AsyncTask<String, Void, JSONObject>{


	Context context;
	String path;
	List<NameValuePair> param;
	Flickr f;
	PxApi pxApi;

	
	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result!=null&&!result.isNull("upload_key")){
			try {
				String key = result.getString("upload_key");
				JSONObject photo = result.getJSONObject("photo");
				String id = ""+photo.getInt("id");
				
				PxUploadTask p = new PxUploadTask(path,context,id,key);
				p.execute("");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public PxUploadKeyTask(List<NameValuePair> param, Context context,
			String path) {
		this.param = param;
		this.path = path;
		this.context = context;
		f = ((MainActivity)this.context).getFlickr();
		pxApi = new PxApi(((MainActivity)this.context).getPxToken(),
				context.getString(R.string.px_consumer_key),
				context.getString(R.string.px_consumer_secret));
		
	}

	@Override
	protected JSONObject doInBackground(String... params) {
		JSONObject obj = pxApi.post("/photos", param);
		return obj;
	}

}
