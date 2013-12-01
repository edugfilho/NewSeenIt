package com.example.mobileseenit;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.aetrion.flickr.Flickr;
import com.aetrion.flickr.REST;
import com.example.mobileseenit.apis.FlickrAuthRetrieveTask;
import com.example.mobileseenit.apis.FlickrLoginDialog;
import com.example.mobileseenit.apis.FlickrUser;
import com.example.mobileseenit.apis.PxLoginDialog;
import com.example.mobileseenit.helpers.PhotoWrapper;
import com.fivehundredpx.api.auth.AccessToken;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, FlickrLoginDialog.OnFlickrLoggedInListener,
		FlickrLoginDialog.OnUpdateFlickrListener,
		PxLoginDialog.OnPxLoggedInListener {

	// View Pager
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	// Currently loaded photos
	ArrayList<PhotoWrapper> photoList;

	ArrayList<String> urls;

	// Fragments
	MainFragment mainFragment;
	SettingsFragment settingsFragment;

	// user objects
	FlickrUser flickrUser;
	AccessToken pxUser;

	// Shared API objects
	Flickr flickr;

	// Location objects
	Double lat;
	Double lng;
	Double radius;

	SeenItLocation loc;

	// Settings
	public boolean useDateRange;
	public Calendar imgsAfter;
	public Calendar imgsBefore;

	// token
	String flickr_token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SharedPreferences sp = this.getSharedPreferences("seenit_prefs", 0);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Initialize location object
		loc = new SeenItLocation();
		radius = 1.0; // default value, change for constant

		if (getLoc().gps_enabled) {
			getLoc().getLocationManager().requestLocationUpdates(
					LocationManager.GPS_PROVIDER,
					getLoc().getUpdateIntervalTimeMilisec(),
					getRadius().floatValue(), getLoc().locationListenerGps);
			Toast.makeText(this, "Getting location...", Toast.LENGTH_SHORT)
					.show();

		}

		// Default date range to false
		useDateRange = false;

		// Set settings default values
		imgsAfter = Calendar.getInstance();
		// imgsAfter.set(Calendar.YEAR, 1990);
		imgsBefore = Calendar.getInstance();

		// Add your fragments here
		mainFragment = new MainFragment();
		settingsFragment = new SettingsFragment();
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(mainFragment);
		fragments.add(new ImageCaptureFragment());
		fragments.add(settingsFragment);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager(), fragments);

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}

		photoList = new ArrayList<PhotoWrapper>();
		urls = new ArrayList<String>();

		// Initialize Flickr Object
		try {
			String flickrApi = getString(R.string.flickr_api_key);
			String flickrSecret = getString(R.string.flickr_secret);
			flickr = new Flickr(flickrApi,flickrSecret, new REST());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		String token = sp.getString("px_token", null);
		String tokensecret = sp.getString("px_tokensecret", null);

		if (token != null && tokensecret != null) {
			pxUser = new AccessToken(token, tokensecret);
			Log.i("pxtoken", token);
			Log.i("pxtokensecret", tokensecret);
		}

		flickr_token = sp.getString("flickr_token", null);
		if (flickr_token != null) {
			FlickrAuthRetrieveTask retrieveTask = new FlickrAuthRetrieveTask(
					flickr_token, this, this, this);
			retrieveTask.execute("");
		}
		mViewPager.setCurrentItem(0);

	}

	// Can be called from search methods. Adds the PhotoWrappers to
	// the current list
	public void addPhotos(LinkedList<PhotoWrapper> photos) {

		// Store photo url
		for (PhotoWrapper photoWrapper : photos) {
			urls.add(photoWrapper.getDetailMap().get(PhotoWrapper.LINK_FIELD));
		}
		photoList.addAll(photos);
		if (!mainFragment.isDetached())
			mainFragment.updateDisplayedPhotos();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	public ArrayList<PhotoWrapper> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(ArrayList<PhotoWrapper> photoList) {
		this.photoList = photoList;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object
		 * )
		 */
		@Override
		public int getItemPosition(Object object) {
			if (object instanceof ImageCaptureFragment
					&& fragments.get(1) instanceof ImageUploadFragment) {
				return POSITION_NONE;
			}
			if (object instanceof ImageUploadFragment
					&& fragments.get(1) instanceof ImageCaptureFragment) {
				return POSITION_NONE;
			}
			return POSITION_UNCHANGED;
		}

		private List<Fragment> fragments;
		private FragmentManager fm;
		private MyCaptureFragmentListener listener = new MyCaptureFragmentListener();

		private final class MyCaptureFragmentListener implements
				CaptureFragmentListener {

			@Override
			public void onSwitchToUpload(byte[] data, double[] loc, String path) {
				fm.beginTransaction().remove(fragments.get(1)).commit();
				if (fragments.get(1) instanceof ImageCaptureFragment) {
					ImageUploadFragment myUploadFragment;
					myUploadFragment = ImageUploadFragment.newInstance(data,
							loc, path);
					myUploadFragment.setListener(listener);
					fragments.set(1, myUploadFragment);
					notifyDataSetChanged();
					startUpdate(mViewPager);
				}

			}

			@Override
			public void onSwitchToCapture() {
				fm.beginTransaction().remove(fragments.get(1)).commit();
				if (fragments.get(1) instanceof ImageUploadFragment) {
					ImageCaptureFragment myCptFragment = new ImageCaptureFragment();
					myCptFragment.setListener(listener);
					fragments.set(1, myCptFragment);
					notifyDataSetChanged();
					startUpdate(mViewPager);
				}
			}
		}

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
			this.fm = fm;
		}

		public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
			this.fm = fm;
			if (this.fragments.get(1) instanceof ImageCaptureFragment) {
				ImageCaptureFragment myCaptureFragment = (ImageCaptureFragment) this.fragments
						.get(1);
				myCaptureFragment.setListener(listener);
			}
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			// TODO switch these back to R.String properties
			case 0:
				// return getString(R.string.title_section1).toUpperCase(l);
				return "Main";
			case 1:
				return "Capture";
			case 2:
				return "Settings";
			}
			return null;
		}

	}

	public FlickrUser getFlickrUser() {
		return flickrUser;
	}

	public void setFlickrUser(FlickrUser flickrUser) {
		this.flickrUser = flickrUser;
	}

	public Flickr getFlickr() {
		return flickr;
	}

	public void setFlickr(Flickr flickr) {
		this.flickr = flickr;
	}

	public AccessToken getPxUser() {
		return pxUser;
	}

	public void setPxUser(AccessToken pxUser) {
		this.pxUser = pxUser;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public SeenItLocation getLoc() {
		return loc;
	}

	public void setLoc(SeenItLocation loc) {
		this.loc = loc;
	}

	/**
	 * Interface with FlickrLoginDialog
	 */
	@Override
	public void onFlickLoggedIn(FlickrUser u) {
		System.out.println("FLickr User Logged in!");
		this.flickrUser = u;

		// Update info in settings fragment
		settingsFragment.updateUserInfo();
	}

	@Override
	public void onUpdateFlickr(Flickr f) {
		System.out.println("Update Flickr object!");
		this.flickr = f;
	}

	@Override
	public void onPxLoggedIn(AccessToken user) {
		System.out.println("500 px User logged in!");
		this.pxUser = user;

		SharedPreferences sp = this.getSharedPreferences("seenit_prefs", 0);
		Editor editor = sp.edit();
		editor.putString("px_token", user.getToken());
		editor.putString("px_tokensecret", user.getTokenSecret());
		editor.commit();

		// Update info in settings fragment
		settingsFragment.updateUserInfo();
	}

	public SettingsFragment getSettingsFragment() {
		return settingsFragment;
	}

	public void setSettingsFragment(SettingsFragment settingsFragment) {
		this.settingsFragment = settingsFragment;
	}

	public AccessToken getPxToken() {
		return pxUser;
	}

	public String getFlickrToken() {
		return flickr_token;
	}

	public Double getRadius() {
		return radius;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
	}

	public ArrayList<String> getUrls() {
		return urls;
	}

	public void setUrls(ArrayList<String> urls) {
		this.urls = urls;
	}

	public Calendar getImgsAfter() {
		return imgsAfter;
	}

	public void setImgsAfter(Calendar imgsAfter) {
		this.imgsAfter = imgsAfter;
	}

	public Calendar getImgsBefore() {
		return imgsBefore;
	}

	public void setImgsBefore(Calendar imgsBefore) {
		this.imgsBefore = imgsBefore;
	}

	@Override
	public void onFlickrAuthRetrieved(FlickrUser user) {
		// TODO Auto-generated method stub
		this.flickrUser = user;
	}

	public boolean isUseDateRange() {
		return useDateRange;
	}

	public void setUseDateRange(boolean useDateRange) {
		this.useDateRange = useDateRange;
	}

}
