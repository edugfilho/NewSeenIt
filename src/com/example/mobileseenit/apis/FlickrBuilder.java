package com.example.mobileseenit.apis;

import javax.xml.parsers.ParserConfigurationException;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;

/**
 * Static helper class to encapsulate building a Flickr
 * Object for user with FlickrJ library.
 * 
 * Should only be called once in MainActivity, shared 
 * and updated by other fragments.
 *
 */
public class FlickrBuilder {

	private static String API_KEY = "af76271af34e193bd2f002eb32032e01";
	private static String SECRET = "b7b96e8ab7032484";
	Flickr r;
	
	public static Flickr buildFlickr()
	{
		Flickr flickrObject = null;
		try {
			flickrObject = new Flickr(API_KEY, SECRET, new REST());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return flickrObject;
	}
}
