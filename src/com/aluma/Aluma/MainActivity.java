package com.aluma.Aluma;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aluma.Aluma.ChangePasswordDialog.ChangePasswordDialogListener;

public class MainActivity extends FragmentActivity implements
		ChangePasswordDialogListener {

	private EditText passWord;
	private TextView changePw;
	private Button loginBt;
	private DbOpen mDbOpen;
	private SQLiteDatabase mDB;
	private SharedPreferences shardPref;
	private SharedPreferences.Editor editor;
	private String mOldPw, mNewPw, mNewPw2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		shardPref = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.this);

		// data 한 번만 가져오기
		if (shardPref.getBoolean("isGetData", false)) {

		} else {
			copyDataToDB();
		}
		setScreen();
	}

	private void copyDataToDB() {

		AssetManager asset = getBaseContext().getResources().getAssets();
		InputStream is;
		InputStreamReader ir;
		BufferedReader reader;
		try {
			is = asset.open("data.csv");
			ir = new InputStreamReader(is, "euc-kr");
			reader = new BufferedReader(ir);

			mDbOpen = new DbOpen(this);
			mDB = mDbOpen.getWritableDatabase();

			String line;
			int[] value = new int[12];
			String name;
			int i;
			String[] rowData;

			while ((line = reader.readLine()) != null) {
				rowData = line.split(",");
				name = rowData[0];

				for (i = 1; i < rowData.length; i++) {
					value[i - 1] = Integer.parseInt(rowData[i]);
				}
				mDB.execSQL("insert into customer_info values ('" + name + "',"
						+ value[0] + "," + value[1] + "," + value[2] + ","
						+ value[3] + "," + value[4] + "," + value[5] + ","
						+ value[6] + "," + value[7] + "," + value[8] + ","
						+ value[9] + "," + value[10] + "," + value[11] + ")");
			}
			is.close();
			mDbOpen.close();
			editor = shardPref.edit();
			editor.putBoolean("isGetData", true).commit();
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setScreen() {
		passWord = (EditText) findViewById(R.id.et_password);
		loginBt = (Button) findViewById(R.id.bt_login);
		passWord.clearFocus();

		setText();
		loginBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String mPw = passWord.getText().toString().trim();
				if (mPw.equals("")) {
					Toast t = Toast.makeText(MainActivity.this, "비밀번호를 입력하세요.",
							Toast.LENGTH_SHORT);
					t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					t.show();
					return;
				} else if (loginProcess(mPw)) {
					Intent intent = new Intent(MainActivity.this,
							ConditionOfSalesActivity.class);
					startActivity(intent);
					finish();
				} else {
					Toast t = Toast.makeText(MainActivity.this,
							"비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT);
					t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
					t.show();
				}
			}
		});
	}

	private void setText() {
		changePw = (TextView) findViewById(R.id.t_changePw);
		changePw.setText(Html.fromHtml("<u>비밀번호 변경</u>"));
		changePw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = MainActivity.this
						.getSupportFragmentManager();
				ChangePasswordDialog mDialog = new ChangePasswordDialog();
				mDialog.show(fm, "ChangePasswordDialog");
			}
		});
	}

	private boolean loginProcess(String password) {
		boolean result = false;
		mDbOpen = new DbOpen(this);
		mDB = mDbOpen.getReadableDatabase();

		Cursor mCur = mDB.rawQuery("select pw from password", null);
		mCur.moveToNext();
		if (password.equals(mCur.getString(0))) {
			result = true;
		} else {
			result = false;
		}
		mDbOpen.close();

		return result;
	}

	@Override
	public void onFinishEditPwDialog(String oldPw, String newPw, String newPw2) {
		mOldPw = oldPw;
		mNewPw = newPw;
		mNewPw2 = newPw2;
		changePw();
	}

	private void changePw() {
		if (loginProcess(mOldPw)) {
			if (mNewPw.equals(mNewPw2)) {
				updatePw();
			} else {
				Toast t = Toast.makeText(MainActivity.this,
						"새로운 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT);
				t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				t.show();
			}
		} else {
			Toast t = Toast.makeText(MainActivity.this, "기존의 비밀번호가 일치하지 않습니다.",
					Toast.LENGTH_SHORT);
			t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			t.show();
		}
	}

	private void updatePw() {
		mDbOpen = new DbOpen(this);
		mDB = mDbOpen.getReadableDatabase();
		mDB.execSQL("update password set pw = '" + mNewPw + "' where pw = '"
				+ mOldPw + "';");
		mDbOpen.close();
		Toast t = Toast.makeText(MainActivity.this, "비밀번호가 변경 되었습니다.",
				Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		t.show();
	}
}
