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

public class ChangePasswordDialog extends DialogFragment {

	public interface ChangePasswordDialogListener {
		void onFinishEditPwDialog(String oldPw, String newPw, String newPw2);
	}

	private View mView;
	private EditText etOldPw, etNewPw, etNewPw2;
	private Button btOk, btCancle;

	public ChangePasswordDialog() {
		

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.dialog_changepassword, container);
		getDialog().setTitle("비밀번호 변경");

		setButton();
		setEditText();
		return mView;
	}

	private void setEditText() {
		etOldPw = (EditText) mView.findViewById(R.id.et_oldpw);
		etNewPw = (EditText) mView.findViewById(R.id.et_newpw);
		etNewPw2 = (EditText) mView.findViewById(R.id.et_newpw2);
	}

	private void setButton() {
		btOk = (Button) mView.findViewById(R.id.bt_okpw);
		btCancle = (Button) mView.findViewById(R.id.bt_canclepw);

		btOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChangePasswordDialogListener mActivity = (ChangePasswordDialogListener) getActivity();
				mActivity.onFinishEditPwDialog(etOldPw.getText().toString(),
						etNewPw.getText().toString(), etNewPw2.getText()
								.toString());
				ChangePasswordDialog.this.dismiss();
			}
		});

		btCancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ChangePasswordDialog.this.dismiss();
			}
		});
	}
}
