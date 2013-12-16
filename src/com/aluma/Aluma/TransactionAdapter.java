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

public class TransactionAdapter extends BaseAdapter {
	private Context mContext;
	private ArrayList<String> transactionInfo;
	private int deviceHeight;
	private String[] productName = { "A", "O", "H", "a", "부", "P", "C", "M",
			"F", "f", "F", "f" };

	public TransactionAdapter(Context context, ArrayList<String> info) {
		mContext = context;
		transactionInfo = info;
		getDisplayMetric();
	}

	@Override
	public int getCount() {

		return (transactionInfo.size() * 3) / 5;
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
		int pIndex, tIndex;

		if (oldView == null) {
			mView = new TextView(mContext);
		} else if (position < getCount()) {
			mView = new TextView(mContext);
			tIndex = position + ((position / 3) * 2 + 2);
			((TextView) mView).setHeight(deviceHeight / 15);
			((TextView) mView).setGravity(Gravity.CENTER);
			((TextView) mView).setBackgroundColor(Color.parseColor("#F3EFE7"));
			((TextView) mView).setTextColor(Color.BLACK);
			((TextView) mView).setClickable(true);
			((TextView) mView).setText(transactionInfo.get(tIndex));

			if (position % 3 == 0) { // 같은날 거래 날짜 중복 표시 제거
				if(transactionInfo.get(tIndex-1).equals("0")){
					((TextView) mView).setText(" ");
				}
			} else if (position % 3 == 2) { // 갯수만 수정 가능
				((TextView) mView).setClickable(false);
				((TextView) mView).setBackgroundColor(Color.WHITE);
			} else if (position % 3 == 1) { // 품목만 해당
				pIndex = Integer.parseInt(transactionInfo.get(tIndex));
				((TextView) mView).setText(productName[pIndex]);
				if (pIndex >= 8) {
					if (pIndex <= 9) {
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
