package com.example.mobileseenit.apis;

import java.io.IOException;

import org.xml.sax.SAXException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.FlickrException;
import com.aetrion.flickr.RequestContext;
import com.aetrion.flickr.photos.GeoData;
import com.example.mobileseenit.MainActivity;

import android.content.Context;
import android.os.AsyncTask;

public class FlickrSetLocationTask extends AsyncTask<String, Void, String> {

	GeoData location;
	Context context;
	RequestContext rc;
	Flickr f;
	String id;
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		rc = RequestContext.getRequestContext();
		try {
			rc.setAuth(f.getAuthInterface().checkToken(((MainActivity)context).getFlickrToken()));
			f.getGeoInterface().setLocation(id, location);
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
	
	public FlickrSetLocationTask(GeoData location, Context context, String id){
		this.location = location;
		this.context = context;
		this.id = id;
		f = ((MainActivity)context).getFlickr();
	}

}
