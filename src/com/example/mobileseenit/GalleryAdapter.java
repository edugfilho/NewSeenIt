/*package com.example.mobileseenit;

import java.util.ArrayList;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class GalleryAdapter extends BaseAdapter {
	public GalleryAdapter(Adapter adapter) {
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

	Adapter mAdapter;

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
		convertView = getALayoutInflater().inflate(R.layout.row, null);
		LinearLayout row = (LinearLayout) convertView;
		LinearLayout l = (LinearLayout) row.getChildAt(0);
		for (int child = 0; child < 4; child++) {
			int i = position * 4 + child;
			LinearLayout c = (LinearLayout) l.getChildAt(child);
			c.removeAllViews();
			if (i < mAdapter.getCount()) {
				c.addView(mAdapter.getView(i, null, null));
			}
		}

		return convertView;
	}
	private class Row extends ArrayList {

	}
}

*/