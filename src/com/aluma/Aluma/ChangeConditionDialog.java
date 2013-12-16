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

public class ChangeConditionDialog extends DialogFragment {

	public interface ChangeConditionDialogListener {
		void onFinishEditCondDialog(String inputText);
	}

	private View mView;
	private EditText etTransaction;
	private Button btOk, btCancle;

	public ChangeConditionDialog(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.dialog_changecondition, container);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setButton();
		setEditText();
		return mView;
	}

	private void setEditText() {
		etTransaction = (EditText) mView.findViewById(R.id.et_cond);
	}

	private void setButton() {
		btOk = (Button) mView.findViewById(R.id.bt_okpw);
		btCancle = (Button) mView.findViewById(R.id.bt_canclepw);

		btOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChangeConditionDialogListener mActivity = (ChangeConditionDialogListener) getActivity();
				mActivity.onFinishEditCondDialog(etTransaction.getText().toString());
				ChangeConditionDialog.this.dismiss();
			}
		});

		btCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChangeConditionDialog.this.dismiss();
			}
		});
	}
}
