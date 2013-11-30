package com.example.mobileseenit.apis;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import android.content.Context;
import android.os.AsyncTask;
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

 * @author dylanrunkel
 * 
 */
public class PxUploadTask extends AsyncTask<String, Void, JSONObject>{


	Context context;
	String path;
	Flickr f;
	PxApi pxApi;
	String id;
	String key;

	
	@Override
	protected void onPostExecute(JSONObject result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(result!=null){
			
			try {
				String error = result.getString("error");
				String status = result.getString("status");
				Log.i("status", status);
				if(error.equals("None.")){
					Toast.makeText(context, "500px upload successful!",
						     Toast.LENGTH_LONG).show();
				}
				else 
					Toast.makeText(context, "500px upload failed: User not logged in or login failed!",
						     Toast.LENGTH_LONG).show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}



	public PxUploadTask(String path, Context context, String id, String key) {
		this.path = path;
		this.context = context;
		this.id = id;
		this.key = key;
		f = ((MainActivity)this.context).getFlickr();
		pxApi = new PxApi(((MainActivity)this.context).getPxToken(),
				context.getString(R.string.px_consumer_key),
				context.getString(R.string.px_consumer_secret));
	}

	@Override
	protected JSONObject doInBackground(String... params) {

		try {
			
			HttpClient client = new DefaultHttpClient();
		    HttpPost post = new HttpPost("https://api.500px.com/v1/upload");
		    MultipartEntityBuilder builder = MultipartEntityBuilder.create();        
		    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		    Charset chars = Charset.forName("UTF-8");
		    builder.setCharset(chars);

		    final File file = new File(path);
		    FileBody fb = new FileBody(file);

		    builder.addPart("file", fb);  
		    builder.addTextBody("photo_id", id);
		    builder.addTextBody("upload_key", key);
		    builder.addTextBody("consumer_key", context.getString(R.string.px_consumer_key));
		    builder.addTextBody("access_key", ((MainActivity)this.context).getPxToken().getToken());
		    final HttpEntity entity = builder.build();
		    post.setEntity(entity);
			HttpResponse response = client.execute(post);
			final int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				final String msg = String.format(
						"Error, statusCode not OK(%d). for url: %s",
						statusCode, post.getURI().toString());
				Log.e("error:", msg);
			}

			HttpEntity responseEntity = response.getEntity();
			InputStream inputStream = responseEntity.getContent();
			BufferedReader r = new BufferedReader(new InputStreamReader(
					inputStream));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}

			JSONObject json = new JSONObject(total.toString());
			return json;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
