package com.example.mobileseenit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class InstagramRequest extends AsyncTask<String, Integer, TreeMap<String, String>> {

	private static final String AUTHURL = "https://api.instagram.com/oauth/authorize/";
	private static final String TOKENURL = "https://api.instagram.com/oauth/access_token";
	public static final String APIURL = "https://api.instagram.com/v1";
	public static String CALLBACKURL = "http://varundroid.blog.com";
	TreeMap<String, String> imgUrlDistance;

	@Override
	protected TreeMap<String, String> doInBackground(String... params) {
		// String request = SEARCH_URL +
		String parameters = params[1]+params[2]+params[3]+params[4];
		JSONArray dataArray = null;
		imgUrlDistance = new TreeMap<String, String>();
		try {
			URL url = new URL(params[0] + "?" + parameters);

			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
					.openConnection();
			// httpsURLConnection.setRequestMethod("GET");
			// httpsURLConnection.setDoInput(true);
			// httpsURLConnection.setDoOutput(true);
			// httpsURLConnection.setUseCaches(false);

			httpsURLConnection.setRequestProperty("Content-Type",
					"application/json");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					httpsURLConnection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			Log.e("USER Response", response.toString());
			JSONObject jsonObject = (JSONObject) new JSONObject(response.toString());
			Log.e("USER Response", jsonObject.toString());
			dataArray = jsonObject.getJSONArray("data");
			
			
			String id;
			String imgUrl;
			for(int i=0; i < dataArray.length(); i++){
				
				//Is it a picture? Throw it in the map.
				if(dataArray.getJSONObject(i).get("type").toString().compareTo("image") == 0){
					id = dataArray.getJSONObject(i).getString("id");
					imgUrl = dataArray.getJSONObject(i).getJSONObject("images")
							.getJSONObject("standard_resolution").get("url").toString();
					imgUrlDistance.put(id, imgUrl);
				}
			}
			
			/* String[] imageNames = array.get.; */
			System.out.println("debugging purp");

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return imgUrlDistance;
	}

	public String streamToString(InputStream is) throws IOException {
		String string = "";

		if (is != null) {
			StringBuilder stringBuilder = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			string = stringBuilder.toString();
		}

		return string;
	}
}
