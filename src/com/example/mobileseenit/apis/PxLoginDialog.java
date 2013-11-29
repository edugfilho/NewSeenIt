package com.example.mobileseenit.apis;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.aetrion.flickr.Flickr;
import com.example.mobileseenit.MainActivity;
import com.example.mobileseenit.R;
import com.example.mobileseenit.apis.FlickrLoginDialog.OnFlickrLoggedInListener;
import com.example.mobileseenit.apis.FlickrLoginDialog.OnUpdateFlickrListener;
import com.fivehundredpx.api.FiveHundredException;
import com.fivehundredpx.api.auth.AccessToken;
import com.fivehundredpx.api.tasks.XAuth500pxTask;

public class PxLoginDialog extends DialogFragment implements
		XAuth500pxTask.Delegate, OnClickListener {

	public final String TAG = this.getClass().getName();

	public static PxLoginDialog newInstance() {
		PxLoginDialog f = new PxLoginDialog();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_500px_login, container, false);

		// Register login button
		Button loginButton = (Button) v
				.findViewById(R.id.px_submit_login_button);
		loginButton.setOnClickListener(this);

		return v;
	}

	@Override
	public void onSuccess(AccessToken result) {
		// TODO Auto-generated method stub
		Log.i(TAG, "success " + result);
		
		//Set the user back in main activity
		mCallback.onPxLoggedIn(result);

		//Close this dialog
		this.dismiss();
	}

	@Override
	public void onFail(FiveHundredException e) {
		// TODO Auto-generated method stub
		//TODO login fail

	}

	@Override
	public void onClick(View v) {
		//Submit login
		if (v.getId() == R.id.px_submit_login_button) {
			XAuth500pxTask loginTask = new XAuth500pxTask(this);
			
			//Get username and pass
			EditText usernameField = (EditText)getView().findViewById(R.id.px_username_input);
			EditText passwordField = (EditText)getView().findViewById(R.id.px_password_input);
			String username = usernameField.getText().toString();
			String password = passwordField.getText().toString();

			loginTask.execute(getString(R.string.px_consumer_key),
					getString(R.string.px_consumer_secret), username,
					password);
		}
	}
	
	/**
	 * Interface to communicate with MainActivity
	 */
	OnPxLoggedInListener mCallback;

	// Container Activity must implement this interface
	public interface OnPxLoggedInListener {
		public void onPxLoggedIn(AccessToken user);
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception
		try {
			mCallback = (OnPxLoggedInListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFlickrLoggedInListener");
		}
	}

}
