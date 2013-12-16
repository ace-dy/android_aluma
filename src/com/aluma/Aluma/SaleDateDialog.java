package com.aluma.Aluma;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;

public class SaleDateDialog extends DialogFragment {

	public interface SaleDateDialogListener {
		void onFinishDateDialog(int year, int month, int day);
	}

	private Button btOk, btCancle;
	private DatePicker mTimepicker;
	private View mView;

	public SaleDateDialog() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.dialog_saledate, container);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setTimepicker();
		setButton();
		return mView;
	}

	private void setTimepicker() {
		mTimepicker = (DatePicker) mView.findViewById(R.id.dp_year);
	}

	private void setButton() {
		btOk = (Button) mView.findViewById(R.id.bt_okdate);
		btCancle = (Button) mView.findViewById(R.id.bt_cancledate);

		btOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SaleDateDialogListener mActivity = (SaleDateDialogListener) getActivity();
				mActivity.onFinishDateDialog(mTimepicker.getYear(),
						mTimepicker.getMonth() + 1, mTimepicker.getDayOfMonth());
				SaleDateDialog.this.dismiss();
			}
		});

		btCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SaleDateDialog.this.dismiss();
			}
		});

	}
}
