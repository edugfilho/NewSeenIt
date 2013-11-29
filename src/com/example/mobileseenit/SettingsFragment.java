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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
	Dialog picker;
	Button selectAfter;
	Button selectBefore;
	Button set;

	DatePicker datePicker;

	TextView dateImgAfter;
	TextView dateImgBefore;

	MainActivity mainActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_settings, container, false);

		// Set Add Flickr Account button click handler
		ImageButton flickrButton = (ImageButton) v
				.findViewById(R.id.add_flickr_account_button);
		flickrButton.setOnClickListener(this);

		mainActivity = (MainActivity) getActivity();

		// Set date pickers after & before
		selectAfter = (Button) v.findViewById(R.id.btnSelectAfter);
		selectBefore = (Button) v.findViewById(R.id.btnSelectBefore);
		datePicker = (DatePicker) v.findViewById(R.id.datePicker);


		dateImgAfter = (TextView) v.findViewById(R.id.textDateImgAfter);
		dateImgBefore = (TextView) v.findViewById(R.id.textDateImgBefore);
		initializeDateValues();

		// Set dateAfter listener
		selectAfter.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				picker = new Dialog(mainActivity);
				picker.setContentView(R.layout.fragment_date_picker);
				picker.setTitle("Select date after:");

				datePicker = (DatePicker) picker.findViewById(R.id.datePicker);

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
				// TODO Auto-generated method stub
				picker = new Dialog(mainActivity);
				picker.setContentView(R.layout.fragment_date_picker);
				picker.setTitle("Select date after:");

				datePicker = (DatePicker) picker.findViewById(R.id.datePicker);
				set = (Button) picker.findViewById(R.id.btnSet);

				set.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
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

		// Set Add 500px Account button click handler
		ImageButton pxButton = (ImageButton) v
				.findViewById(R.id.add_500px_account_button);
		pxButton.setOnClickListener(this);

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
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {

						StringTokenizer st = new StringTokenizer(parent
								.getItemAtPosition(pos).toString());
						Double radius = Double.valueOf(st.nextToken());

						mainActivity.setRadius(radius);
					}

					public void onNothingSelected(AdapterView<?> parent) {
						StringTokenizer st = new StringTokenizer(parent
								.getItemAtPosition(0).toString());
						Double radius = Double.valueOf(st.nextToken());
						mainActivity.setRadius(radius);
					}
				});
		return v;
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

	public void updateUserInfo() {
		System.out.println("UPDATEUSERINGO");

		// Check for Users
		FlickrUser flickrUser = ((MainActivity) getActivity()).getFlickrUser();
		AccessToken pxUser = ((MainActivity) getActivity()).getPxUser();

		if (flickrUser != null) {
			// Set username text
			TextView usernameTextView = (TextView) getView().findViewById(
					R.id.flickr_username);
			usernameTextView.setText(flickrUser.getUsername());

			// Remove add button, put in Logout button
			// TODO
		}

		if (pxUser != null) {
			TextView usernameTextView = (TextView) getView().findViewById(
					R.id.px_username);
			usernameTextView.setText(pxUser.getToken());
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
