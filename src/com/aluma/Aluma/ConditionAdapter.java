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

public class ConditionAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> conditionInfo;
	private int deviceHeight;
	private int sIndex, pIndex; //미수금 처리 위한 부분

	public ConditionAdapter(Context context, ArrayList<String> info, int index) {
		mContext = context;
		conditionInfo = info;
		sIndex = index;
		pIndex = 4 * sIndex;
		getDisplayMetric();
	}

	@Override
	public int getCount() {
		return conditionInfo.size();
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

		if (oldView == null) {
			mView = new TextView(mContext);
		} else if (position < getCount()) {
			mView = new TextView(mContext);
			((TextView) mView).setHeight(deviceHeight / 15);
			((TextView) mView).setGravity(Gravity.CENTER);
			((TextView) mView).setClickable(true);
			((TextView) mView).setBackgroundColor(Color.parseColor("#F3EFE7"));
			((TextView) mView).setText(conditionInfo.get(position));
			((TextView) mView).setTextColor(Color.BLACK);

			if (position % 4 == 2) {
				((TextView) mView).setClickable(false);
				((TextView) mView).setBackgroundColor(Color.WHITE);
			}
			//미수금 처리
			if (position >= pIndex){
				((TextView) mView).setTextColor(Color.RED);
			}
//			if(position >= getCount()-4){ //총합 출력
//				((TextView) mView).setTextColor(Color.BLACK);
//				((TextView) mView).setBackgroundColor(Color.parseColor("#EFECF7"));
//			}
			
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
