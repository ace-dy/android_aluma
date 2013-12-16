package com.aluma.Aluma;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PriceInfoAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> priceInfo;
	private int deviceHeight;
	private String[] productName = { "A", "O", "H", "a", "부", "P", "C", "M",
			"F", "f", "F", "f" };

	public PriceInfoAdapter(Context context, ArrayList<String> info) {
		mContext = context;
		priceInfo = info;
		getDisplayMetric();
	}

	@Override
	public int getCount() {
		return priceInfo.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View oldView, ViewGroup parent) {
		View mView = null;
		int index;

		if (oldView == null) {
			mView = new TextView(mContext);
		} else if (position < getCount()) {
			mView = new TextView(mContext);

			((TextView) mView).setHeight(deviceHeight / 15);
			((TextView) mView).setGravity(Gravity.CENTER);
			((TextView) mView).setClickable(false);
			((TextView) mView).setBackgroundColor(Color.WHITE);
			((TextView) mView).setText(priceInfo.get(position));
			((TextView) mView).setTextColor(Color.BLACK);
			
			//품목만 해당
			if (position % 2 == 0) {
				index = Integer.parseInt(priceInfo.get(position));
				((TextView) mView).setText(productName[index]);
				((TextView) mView).setClickable(true);
				((TextView) mView).setBackgroundColor(Color.parseColor("#F3EFE7"));
				if (index >= 8) {
					if (index <= 9) {
						((TextView) mView).setTextColor(Color.GREEN);
					} else {
						((TextView) mView).setTextColor(Color.GRAY);
					}
				}
			}
		} else {
			mView = (View) oldView;
		}
		return mView;
	}

	private void getDisplayMetric() {
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		deviceHeight = metrics.heightPixels;
	}
}
