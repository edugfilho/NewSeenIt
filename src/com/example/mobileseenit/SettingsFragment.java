package com.example.mobileseenit;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.aetrion.flickr.Flickr;
import com.example.mobileseenit.apis.FlickrLoginDialog;
import com.example.mobileseenit.apis.FlickrUser;
import com.example.mobileseenit.apis.PxLoginDialog;
import com.fivehundredpx.api.auth.AccessToken;

public class SettingsFragment extends Fragment implements OnTouchListener,
		OnClickListener {

	// Login Dialogs
	FlickrLoginDialog flickrLoginDialog;

	// SPinner for Search Diameter
	public Spinner searchDiameter;

	// Date picker components
	private Dialog picker;
	private Button selectAfter;
	private Button selectBefore;
	private Button set;
	private Button reload;
	private TextView beforeLabel;
	private TextView afterLabel;
	private DatePicker datePicker;
	private TextView dateImgAfter;
	private TextView dateImgBefore;

	private MainActivity mainActivity;

	//Login/logout buttons
	private ImageButton loginFlickr;
	private ImageButton logoutFlickr;
	private ImageButton loginPx;
	private ImageButton logoutPx;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// setup
		View v = inflater.inflate(R.layout.fragment_settings, container, false);
		mainActivity = (MainActivity) getActivity();

		// Set Add Flickr Account button click handler
		ImageButton flickrButton = (ImageButton) v
				.findViewById(R.id.add_flickr_account_button);
		flickrButton.setOnClickListener(this);

		reload = (Button) v.findViewById(R.id.btnReload);
		reload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainActivity.reloadFragment(mainActivity.mainFragment);
				
			}
		});
		
		// Set date pickers after & before
		selectAfter = (Button) v.findViewById(R.id.btnSelectAfter);
		selectBefore = (Button) v.findViewById(R.id.btnSelectBefore);
		datePicker = (DatePicker) v.findViewById(R.id.datePicker);
		dateImgAfter = (TextView) v.findViewById(R.id.textDateImgAfter);
		dateImgBefore = (TextView) v.findViewById(R.id.textDateImgBefore);
		afterLabel = (TextView) v.findViewById(R.id.settings_after_label);
		beforeLabel = (TextView) v.findViewById(R.id.settings_before_label);
		initializeDateValues();

		// Set dateAfter listener
		selectAfter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				picker = new Dialog(mainActivity);
				picker.setContentView(R.layout.fragment_date_picker);
				picker.setTitle("Show images after this date:");

				datePicker = (DatePicker) picker.findViewById(R.id.datePicker);
				
				
				//Setting the minimum date for datePicker:
				Calendar minDate = Calendar.getInstance();
				minDate.set(Calendar.YEAR, minDate.get(Calendar.YEAR)-1);	
				datePicker.setMinDate(minDate.getTimeInMillis());
				
				//Setting the maximum date
				datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
				
				datePicker.setCalendarViewShown(false);
				
				set = (Button) picker.findViewById(R.id.btnSet);

				set.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						mainActivity.getImgsAfter().set(datePicker.getYear(),
								datePicker.getMonth(),
								datePicker.getDayOfMonth());

						// Updates date in text format
						updateDateValues(dateImgAfter);

						picker.dismiss();
					}
				});
				picker.show();

			}
		});

		// Set dateBefore listener
		selectBefore.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				picker = new Dialog(mainActivity);
				picker.setContentView(R.layout.fragment_date_picker);
				picker.setTitle("Show images before this date:");

				datePicker = (DatePicker) picker.findViewById(R.id.datePicker);
				datePicker.setCalendarViewShown(false);

				//Setting the minimum date for datePicker:
				String minDate = (String) dateImgAfter.getText();
				datePicker.setMinDate(Date.parse(minDate));
				
				//Setting the maximum date
				datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
				set = (Button) picker.findViewById(R.id.btnSet);

				set.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						mainActivity.getImgsBefore().set(datePicker.getYear(),
								datePicker.getMonth(),
								datePicker.getDayOfMonth());

						// Update dates in text format
						updateDateValues(dateImgBefore);

						picker.dismiss();
					}
				});
				picker.show();
			}
		});

		// Set checkbox onclick listener
		final CheckBox useDateRange = (CheckBox) v.findViewById(R.id.checkBox1);

		useDateRange.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				toggleChecked(useDateRange.isChecked());
			}
		});

		// Check if user has selected to use date range
		toggleChecked(mainActivity.useDateRange);

		// Set logout button actions
		logoutFlickr = (ImageButton) v
				.findViewById(R.id.logout_flickr_account_button);
		loginFlickr = (ImageButton) v
				.findViewById(R.id.add_flickr_account_button);
		loginPx = (ImageButton)v.findViewById(R.id.add_500px_account_button);
		logoutPx = (ImageButton)v.findViewById(R.id.logout_px_account_button);

		logoutFlickr.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logoutFlickr();
			}
		});
		
		logoutPx.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logoutPx();
			}
		});

		// Set Add 500px Account button click handler

		loginPx.setOnClickListener(this);

		// Check for flickr user
		FlickrUser flickrUser = ((MainActivity) getActivity()).getFlickrUser();
		if (flickrUser != null) {
			// Set username text
			TextView usernameTextView = (TextView) v
					.findViewById(R.id.flickr_username);
			usernameTextView.setText(flickrUser.getUsername());
		}

		// Check for 500px user
		AccessToken pxUser = ((MainActivity) getActivity()).getPxUser();
		if (pxUser != null) {
			// Set username text
			TextView usernameTextView = (TextView) v
					.findViewById(R.id.px_username);
			usernameTextView.setText(pxUser.getToken());
		}

		// Set the Search Radius Spinner
		searchDiameter = (Spinner) v.findViewById(R.id.spinner1);

		// Every time a value changes on the Spinner, it updates it on the
		// MainActivity
		searchDiameter
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {

						StringTokenizer st = new StringTokenizer(parent
								.getItemAtPosition(pos).toString());
						Double radius = Double.valueOf(st.nextToken());

						mainActivity.setRadius(radius);
					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						StringTokenizer st = new StringTokenizer(parent
								.getItemAtPosition(0).toString());
						Double radius = Double.valueOf(st.nextToken());
						mainActivity.setRadius(radius);
					}
				});

		return v;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		updateUserInfo();
	}
	

	private void initializeDateValues() {
		Calendar cal = mainActivity.getImgsAfter();
		dateImgAfter
				.setText(cal.get(Calendar.MONTH) + "/"
						+ cal.get(Calendar.DAY_OF_MONTH) + "/"
						+ cal.get(Calendar.YEAR));
		cal = mainActivity.getImgsBefore();
		dateImgBefore
				.setText(cal.get(Calendar.MONTH) + "/"
						+ cal.get(Calendar.DAY_OF_MONTH) + "/"
						+ cal.get(Calendar.YEAR));
	}

	private void updateDateValues(TextView view) {
		view.setText(datePicker.getMonth() + 1 + "/"
				+ datePicker.getDayOfMonth() + "/" + datePicker.getYear());
	}

	@Override
	public void onClick(View v) {

		// Handle add_flickr_account button
		if (v.getId() == R.id.add_flickr_account_button) {

			// Create and display login dialog
			FragmentTransaction ft = ((MainActivity) v.getContext())
					.getFragmentManager().beginTransaction();
			flickrLoginDialog = FlickrLoginDialog.newInstance();
			flickrLoginDialog.show(ft, "dialog");
		}

		// Handle 500px user login
		else if (v.getId() == R.id.add_500px_account_button) {
			// Create and display login dialog
			FragmentTransaction ft = ((MainActivity) v.getContext())
					.getFragmentManager().beginTransaction();
			PxLoginDialog pxLogin = PxLoginDialog.newInstance();
			pxLogin.show(ft, "dialog");
		}
	}

	/**
	 * Called from an login auth tasks. Updates the username of the user that
	 * just logged in. Also displays logout button once they are logged in.
	 * 
	 * Toggles login/logout buttons
	 */
	public void updateUserInfo() {
		// Check for Users
		Flickr flickr = mainActivity.getFlickr();
		AccessToken pxUser = ((MainActivity) getActivity()).getPxUser();

		TextView flickrUserNameText = (TextView) getView().findViewById(
				R.id.flickr_username);
		TextView pxUserNameText = (TextView) getView().findViewById(
				R.id.px_username);
		
		if (flickr.getAuth() != null) {
			// Set username text
			flickrUserNameText.setText(flickr.getAuth().getUser().getUsername());

			loginFlickr.setVisibility(View.GONE);
			logoutFlickr.setVisibility(View.VISIBLE);

		} else {
			flickrUserNameText.setText("Not Logged In");
			loginFlickr.setVisibility(View.VISIBLE);
			logoutFlickr.setVisibility(View.GONE);
		}

		if (pxUser != null) {
			// Set username text
			pxUserNameText.setText(pxUser.getToken());

			loginPx.setVisibility(View.GONE);
			logoutPx.setVisibility(View.VISIBLE);

		} else {
			pxUserNameText.setText("Not Logged In");
			loginPx.setVisibility(View.VISIBLE);
			logoutPx.setVisibility(View.GONE);
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		System.out.println("ONRESUME");

		// Check for flickr user
		FlickrUser flickrUser = ((MainActivity) getActivity()).getFlickrUser();
		if (flickrUser != null) {
			// Set username text
			TextView usernameTextView = (TextView) getView().findViewById(
					R.id.flickr_username);
			usernameTextView.setText(flickrUser.getUsername());
		}
		updateUserInfo();
	}

	/**
	 * Toggles option to use Date Range to search for images. Will update
	 * setting in MainActivity and show/hide related form elements in this
	 * settings view.
	 * 
	 * @param checked
	 */
	private void toggleChecked(boolean checked) {
		// Convert to int for visible
		int visible;
		if (checked)
			visible = View.VISIBLE;
		else
			visible = View.INVISIBLE;

		// If checked, enable date fields
		selectAfter.setVisibility(visible);
		selectBefore.setVisibility(visible);
		dateImgAfter.setVisibility(visible);
		dateImgBefore.setVisibility(visible);
		afterLabel.setVisibility(visible);
		beforeLabel.setVisibility(visible);

		// save in MainActivity for later
		mainActivity.setUseDateRange(checked);
	}

	/**
	 * Nulls the flickr auth in MainActivity, effectively
	 * logging out.
	 */
	private void logoutFlickr() {
		mainActivity.getFlickr().setAuth(null);
		updateUserInfo();
	}
	
	/**
	 * Nulls the accessToken for 500px, logging
	 * the user out
	 */
	private void logoutPx()
	{
		mainActivity.setPxUser(null);
		updateUserInfo();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	public Spinner getSearchDiameter() {
		return searchDiameter;
	}

	public void setSearchDiameter(Spinner searchDiameter) {
		this.searchDiameter = searchDiameter;
	}

}
