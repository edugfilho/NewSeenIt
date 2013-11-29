package com.example.mobileseenit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.widget.Toast;

import com.aetrion.flickr.Flickr;
import com.example.mobileseenit.SeenItLocation.LocationResult;
import com.example.mobileseenit.apis.FlickrBuilder;
import com.example.mobileseenit.apis.FlickrLoginDialog;
import com.example.mobileseenit.apis.FlickrSearchTask;
import com.example.mobileseenit.apis.FlickrUser;
import com.example.mobileseenit.helpers.PhotoWrapper;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, FlickrLoginDialog.OnFlickrLoggedInListener,
		FlickrLoginDialog.OnUpdateFlickrListener {

	// View Pager
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	// Currently loaded photos
	ArrayList<PhotoWrapper> photoList;

	// Fragments
	MainFragment mainFragment;
	SettingsFragment settingsFragment;
	PhotoUploadTestFragment uploadTestFragment;
	InstagramFragment instagramFragment;

	// user objects
	FlickrUser flickrUser;

	// Shared API objects
	Flickr flickr;

	// Location settings
	SettingsFragment settings;

	Double lat;
	Double lng;

	SeenItLocation loc;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		//Initialize location object
		loc = new SeenItLocation();

		// Add your fragments here
		mainFragment = new MainFragment();
		settingsFragment = new SettingsFragment();
		uploadTestFragment = new PhotoUploadTestFragment();
		//instagramFragment = new InstagramFragment();
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(mainFragment);
		fragments.add(new ImageCaptureFragment());
		fragments.add(settingsFragment);
		fragments.add(uploadTestFragment);
		// fragments.add(instagramFragment);

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



		mViewPager.setCurrentItem(0);

		// Initialize Flickr Object
		flickr = FlickrBuilder.buildFlickr();

	}

	// Can be called from search methods. Adds the PhotoWrappers to
	// the current list
	public void addPhotos(ArrayList<PhotoWrapper> photos) {

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

		private List<Fragment> fragments;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public SectionsPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.

			// Fragment fragment = new DummySectionFragment();
			// Bundle args = new Bundle();
			// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position +
			// 1);
			// fragment.setArguments(args);
			System.out.println("position: " + position);
			return fragments.get(position);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			// TODO switch these back to R.String properties
			case 0:
				// return getString(R.string.title_section1).toUpperCase(l);
				return "Main";
			case 1:
				return "Capture";
			case 2:
				return "Settings";
			case 3:
				return "Upload";
			case 4:
				return "InstaTest";
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

	/**
	 * Interface with FlickrLoginDialog
	 */
	@Override
	public void onFlickLoggedIn(FlickrUser u) {
		System.out.println("FLickr User Logged in!");
		this.flickrUser = u;

		// Update info
		settingsFragment.updateUserInfo();
	}

	@Override
	public void onUpdateFlickr(Flickr f) {
		System.out.println("Update Flickr object!");
		this.flickr = f;
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



}
