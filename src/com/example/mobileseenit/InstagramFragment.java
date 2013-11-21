package com.example.mobileseenit;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

import android.app.FragmentTransaction;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.mobileseenit.SeenItLocation.LocationResult;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class InstagramFragment extends Fragment implements OnTouchListener,
		OnClickListener {

	private String stringTestText = "what?";

	private LocationManager locationManager;
	private String provider;
	public Double lat;
	public Double lng;
	TreeMap<String, String> imgUrlDistance;
	ImageView selectedImage;

	private ListView listView;
	private InstagramAdapter instaAdapter;

	private SeenItLocation loc;
	private static final String SEARCH_URL = "https://api.instagram.com/v1/media/search";
	LocationResult locationResult = new LocationResult() {
		@Override
		public void gotLocation(Location location) {
			//
			lat = location.getLatitude();
			lng = location.getLongitude();
			/*String[] params = { SEARCH_URL,
					"client_id=" + getString(R.string.insta_client_id),
					"&lat=" + location.getLatitude(),
					"&lng=" + location.getLongitude(), "&distance=" + "1000" };
			try {
				imgUrlDistance = new InstagramRequest().execute(params).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			getActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
				/*	for (TreeMap.Entry<String, String> entry : imgUrlDistance
							.entrySet()) {
						instaAdapter.add(entry.getValue());
					}*/
					setText("Location: lat: " + lat + " lng: "
							+ lng);
				}
			});

		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// loc = new SeenItLocation();
		View rootView = inflater.inflate(R.layout.fragment_instagram,
				container, false);

		loc = new SeenItLocation();
		loc.getLocation(this.getActivity(), locationResult);

		listView = (ListView) rootView.findViewById(R.id.results);
		instaAdapter = new InstagramAdapter(getActivity());
		GridAdapter a = new GridAdapter(instaAdapter);
		listView.setAdapter(a);
		instaAdapter.clear();
		

		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub\
		return false;
	}

	public void setText(String s) {
		TextView textView = (TextView) getActivity().findViewById(
				R.id.testDescription);
		textView.setText(s);

	}

	private class GridAdapter extends BaseAdapter {
		Adapter mAdapter;

		public GridAdapter(Adapter adapter) {
			mAdapter = adapter;
			mAdapter.registerDataSetObserver(new DataSetObserver() {
				@Override
				public void onChanged() {
					super.onChanged();
					notifyDataSetChanged();
				}

				@Override
				public void onInvalidated() {
					super.onInvalidated();
					notifyDataSetInvalidated();
				}
			});
		}

		@Override
		public int getCount() {
			return (int) Math.ceil((double) mAdapter.getCount() / 4d);
		}

		@Override
		public Row getItem(int position) {
			Row row = new Row();
			for (int i = position * 4; i < 4; i++) {
				if (mAdapter.getCount() < i)
					row.add(mAdapter.getItem(i));
				else
					row.add(null);
			}
			return row;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = getActivity().getLayoutInflater().inflate(
					R.layout.row, null);
			LinearLayout row = (LinearLayout) convertView;
			LinearLayout l = (LinearLayout) row.getChildAt(0);
			for (int child = 0; child < 4; child++) {
				int i = position * 4 + child;
				LinearLayout c = (LinearLayout) l.getChildAt(child);
				c.removeAllViews();

				if (i < mAdapter.getCount()) {
					View addedView = mAdapter.getView(i, null, null);
					addedView.setTag(i);
					addedView.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							showDialog(v);

						}

					});

					c.addView(addedView);

				}
			}
			return convertView;
		}

		void showDialog(View v) {
			/*
			 * Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
			 * Bitmap.Config.ARGB_8888); BitmapDrawable d = new
			 * BitmapDrawable(getResources(), b);
			 * 
			 * String stuff = Integer.toString(v.getWidth()) +
			 * Integer.toString(v.getHeight());
			 * 
			 * Toast.makeText(getActivity(), stuff, Toast.LENGTH_SHORT).show();
			 */
			Integer position = (Integer) v.getTag();
			String imgUrl = (String) mAdapter.getItem(position);
			FragmentTransaction ft = getActivity().getFragmentManager()
					.beginTransaction();
			ImageDialogSeenIt newFragment = ImageDialogSeenIt
					.newInstance(imgUrl);

			newFragment.show(ft, "dialog");
		}

		private class Row extends ArrayList {

		}

	}

	private class InstagramAdapter extends ArrayAdapter<String> {

		public InstagramAdapter(Context context) {
			super(context, 0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null)
				convertView = getActivity().getLayoutInflater().inflate(
						R.layout.image, null);

			final ImageView iv = (ImageView) convertView
					.findViewById(R.id.image);

			iv.setAnimation(null);
			// yep, that's it. it handles the downloading and showing an
			// interstitial image automagically.
			UrlImageViewHelper.setUrlDrawable(iv, getItem(position),
					R.drawable.loading, new UrlImageViewCallback() {
						@Override
						public void onLoaded(ImageView imageView,
								Bitmap loadedBitmap, String url,
								boolean loadedFromCache) {
							if (!loadedFromCache) {
								ScaleAnimation scale = new ScaleAnimation(0, 1,
										0, 1, ScaleAnimation.RELATIVE_TO_SELF,
										.5f, ScaleAnimation.RELATIVE_TO_SELF,
										.5f);
								scale.setDuration(300);
								scale.setInterpolator(new OvershootInterpolator());
								imageView.startAnimation(scale);
							}
						}
					});

			return convertView;
		}
	}

}