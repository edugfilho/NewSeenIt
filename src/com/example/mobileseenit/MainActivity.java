package com.example.mobileseenit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import com.aetrion.flickr.Flickr;
import com.example.mobileseenit.apis.FlickrBuilder;
import com.example.mobileseenit.apis.FlickrLoginDialog;
import com.example.mobileseenit.apis.FlickrSearchTask;
import com.example.mobileseenit.apis.FlickrUser;
import com.example.mobileseenit.apis.PxLoginDialog;
import com.example.mobileseenit.helpers.PhotoWrapper;
import com.fivehundredpx.api.auth.AccessToken;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, FlickrLoginDialog.OnFlickrLoggedInListener,
		FlickrLoginDialog.OnUpdateFlickrListener,
		PxLoginDialog.OnPxLoggedInListener{

	// View Pager
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	// Currently loaded photos
	ArrayList<PhotoWrapper> photoList;

	
	// Fragments
	MainFragment mainFragment;
	SettingsFragment settingsFragment;
	PhotoUploadTestFragment uploadTestFragment;

	// user objects
	FlickrUser flickrUser;
	AccessToken pxUser;

	// Shared API objects
	Flickr flickr;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Add your fragments here
		mainFragment = new MainFragment();
		settingsFragment = new SettingsFragment();
		uploadTestFragment = new PhotoUploadTestFragment();
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(mainFragment);
		fragments.add(new ImageCaptureFragment());
		fragments.add(settingsFragment);
		fragments.add(uploadTestFragment);

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
		FlickrSearchTask flickrSearch = new FlickrSearchTask(this);
		flickrSearch.execute();
		

		// Initialize Flickr Object
		flickr = FlickrBuilder.buildFlickr();
		
		mViewPager.setCurrentItem(0);

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

		/* (non-Javadoc)
		 * @see android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
		 */
		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			if(object instanceof ImageCaptureFragment && 
					fragments.get(1) instanceof ImageUploadFragment){
				return POSITION_NONE;
			}
			if(object instanceof ImageUploadFragment && 
					fragments.get(1) instanceof ImageCaptureFragment){
				return POSITION_NONE;
			}
			return POSITION_UNCHANGED;
		}

		private List<Fragment> fragments;
		private FragmentManager fm;
		private MyCaptureFragmentListener listener = new MyCaptureFragmentListener();
		private final class MyCaptureFragmentListener implements CaptureFragmentListener{

			@Override
			public void onSwitchToUpload(byte[] data, double[] loc, String path) {
				// TODO Auto-generated method stub
				fm.beginTransaction().remove(fragments.get(1)).commit();
				if(fragments.get(1) instanceof ImageCaptureFragment){
					ImageUploadFragment myUploadFragment;
					myUploadFragment = ImageUploadFragment.newInstance(data, loc, path);
					myUploadFragment.setListener(listener);
					fragments.set(1, myUploadFragment);
					notifyDataSetChanged();
					startUpdate(mViewPager);
				}
				
			}

			@Override
			public void onSwitchToCapture() {
				// TODO Auto-generated method stub
				fm.beginTransaction().remove(fragments.get(1)).commit();
				if(fragments.get(1) instanceof ImageUploadFragment){
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
			if(this.fragments.get(1) instanceof ImageCaptureFragment){
				ImageCaptureFragment myCaptureFragment = (ImageCaptureFragment) this.fragments.get(1);
				myCaptureFragment.setListener(listener);
				//this.fragments.set(1, myCaptureFragment);
			}
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
		
		//Update info in settings fragment
		settingsFragment.updateUserInfo();
	}

}
