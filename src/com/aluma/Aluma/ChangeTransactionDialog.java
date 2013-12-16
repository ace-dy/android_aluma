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

public class ChangeTransactionDialog extends DialogFragment {

	public interface ChangeTransactionDialogListener {
		void onFinishEditTranDialog(String inputText);
	}

	private View mView;
	private EditText etTransaction;
	private Button btOk, btCancle;

	public ChangeTransactionDialog(){
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.dialog_changetransaction, container);
		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setButton();
		setEditText();
		return mView;
	}

	private void setEditText() {
		etTransaction = (EditText) mView.findViewById(R.id.et_tran);
	}

	private void setButton() {
		btOk = (Button) mView.findViewById(R.id.bt_oktran);
		btCancle = (Button) mView.findViewById(R.id.bt_cancletran);

		btOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChangeTransactionDialogListener mActivity = (ChangeTransactionDialogListener) getActivity();
				mActivity.onFinishEditTranDialog(etTransaction.getText().toString());
				ChangeTransactionDialog.this.dismiss();
			}
		});

		btCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChangeTransactionDialog.this.dismiss();
			}
		});
	}
}
