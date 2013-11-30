package com.example.mobileseenit.apis;

import java.io.IOException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.auth.Auth;
import com.example.mobileseenit.MainActivity;
import com.example.mobileseenit.apis.FlickrLoginDialog.OnFlickrLoggedInListener;
import com.example.mobileseenit.apis.FlickrLoginDialog.OnUpdateFlickrListener;

import android.content.Context;
import android.os.AsyncTask;

public class FlickrAuthRetrieveTask extends AsyncTask<String, Void, Auth> {

	String token;
	Context context;
	Flickr f;
	OnFlickrLoggedInListener mCallback;
	OnUpdateFlickrListener updateFlickrCallback;
	
	@Override
	protected Auth doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			Auth auth = f.getAuthInterface().checkToken(token);
			f.setAuth(auth);
			return auth;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FlickrException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Auth result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		updateFlickrCallback.onUpdateFlickr(f);
		FlickrUser newUser = new FlickrUser();
		newUser.setUsername(result.getUser().getUsername());
		mCallback.onFlickrAuthRetrieved(newUser);
		
	}

	public FlickrAuthRetrieveTask(String token,OnFlickrLoggedInListener mCallback,
	OnUpdateFlickrListener updateFlickrCallback, Context context){
		this.token = token;
		this.context = context;
		this.mCallback = mCallback;
		this.updateFlickrCallback = updateFlickrCallback;
		f = ((MainActivity)context).getFlickr();
		
	}

}
