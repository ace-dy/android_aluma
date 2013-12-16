package com.aluma.Aluma;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class ChangePriceDialog extends DialogFragment {

	public interface ChangePriceDialogListener {
		void onFinishEditDialog(String inputText);
	}

	private View mView;
	private EditText etPrice;
	private Button btOk, btCancle;

	public ChangePriceDialog(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.dialog_changeprice, container);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setButton();
		setEditText();
		return mView;
	}

	private void setEditText() {
		etPrice = (EditText) mView.findViewById(R.id.et_price);
	}

	private void setButton() {
		btOk = (Button) mView.findViewById(R.id.bt_okchange);
		btCancle = (Button) mView.findViewById(R.id.bt_canclechange);

		btOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChangePriceDialogListener mActivity = (ChangePriceDialogListener) getActivity();
				mActivity.onFinishEditDialog(etPrice.getText().toString());
				ChangePriceDialog.this.dismiss();
			}
		});

		btCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChangePriceDialog.this.dismiss();
			}
		});
	}
}
