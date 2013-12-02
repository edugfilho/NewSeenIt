package com.example.mobileseenit.apis;

import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;
import com.aetrion.flickr.Flickr;
import com.example.mobileseenit.MainActivity;
import com.example.mobileseenit.R;
import com.fivehundredpx.api.PxApi;
/**
 * Async Task that requests an upload key from
 * 500px in order to upload a picture.
 */
public class PxUploadKeyTask extends AsyncTask<String, Void, JSONObject>{


	Context context;
	String path;
	List<NameValuePair> param;
	Flickr f;
	PxApi pxApi;

	
	@Override
	protected void onPostExecute(JSONObject result) {
		super.onPostExecute(result);
		if(result!=null&&!result.isNull("upload_key")){
			try {
				String key = result.getString("upload_key");
				JSONObject photo = result.getJSONObject("photo");
				String id = ""+photo.getInt("id");
				
				PxUploadTask p = new PxUploadTask(path,context,id,key);
				p.execute("");
			} catch (JSONException e) {
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
