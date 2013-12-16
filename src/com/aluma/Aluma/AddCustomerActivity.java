package com.aluma.Aluma;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddCustomerActivity extends Activity {
	private Button okBt, cancleBt;
	private EditText etA, etO, etH, eta, etB, etP, etC, etM, etFg, etfg, etF,
			etf, etName;
	private DbOpen mDbOpen;
	private SQLiteDatabase mDB;
	private int[] mPrice;
	private String mName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_customer);
		setScreen();
	}

	private void setScreen() {

		setButton();
		setEditText();
	}

	private void setButton() {
		okBt = (Button) findViewById(R.id.bt_okadd);
		cancleBt = (Button) findViewById(R.id.bt_cancleadd);
		// ok버튼 클릭시 내용 DB에 저장
		okBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getNewInfo();
				if (mName.length() > 0)
					setNewInfo();
				onBackPressed();
			}
		});

		cancleBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}

	private void setEditText() {
		etA = (EditText) findViewById(R.id.et_num1);
		etO = (EditText) findViewById(R.id.et_num2);
		etH = (EditText) findViewById(R.id.et_num3);
		eta = (EditText) findViewById(R.id.et_num4);
		etB = (EditText) findViewById(R.id.et_num5);
		etP = (EditText) findViewById(R.id.et_num6);
		etC = (EditText) findViewById(R.id.et_num7);
		etM = (EditText) findViewById(R.id.et_num8);
		etFg = (EditText) findViewById(R.id.et_num9);
		etfg = (EditText) findViewById(R.id.et_num10);
		etF = (EditText) findViewById(R.id.et_num11);
		etf = (EditText) findViewById(R.id.et_num12);
		etName = (EditText) findViewById(R.id.et_inputname);
	}

	private void getNewInfo() {
		mPrice = new int[12];

		// 입력된 값이 있는지 확인
		mName = etName.getText().toString().trim();

		for (int i = 0; i < 12; i++) {
			mPrice[i] = 0;
		}

		String temp = etA.getText().toString().trim();
		if (temp.length() > 0) {
			mPrice[0] = Integer.parseInt(temp);
		}

		temp = etO.getText().toString().trim();
		if (temp.length() > 0) {
			mPrice[1] = Integer.parseInt(temp);
		}

		temp = etH.getText().toString().trim();
		if (temp.length() > 0) {
			mPrice[2] = Integer.parseInt(temp);
		}

		temp = eta.getText().toString().trim();
		if (temp.length() > 0) {
			mPrice[3] = Integer.parseInt(temp);
		}

		temp = etB.getText().toString().trim();
		if (temp.length() > 0) {
			mPrice[4] = Integer.parseInt(temp);
		}

		temp = etP.getText().toString().trim();
		if (temp.length() > 0) {
			mPrice[5] = Integer.parseInt(temp);
		}

		temp = etC.getText().toString().trim();
		if (temp.length() > 0) {
			mPrice[6] = Integer.parseInt(temp);
		}

		temp = etM.getText().toString().trim();
		if (temp.length() > 0) {
			mPrice[7] = Integer.parseInt(temp);
		}

		temp = etFg.getText().toString().trim();
		if (temp.length() > 0) {
			mPrice[8] = Integer.parseInt(temp);
		}

		temp = etfg.getText().toString().trim();
		if (temp.length() > 0) {
			mPrice[9] = Integer.parseInt(temp);
		}

		temp = etF.getText().toString().trim();
		if (temp.length() > 0) {
			mPrice[10] = Integer.parseInt(temp);
		}

		temp = etf.getText().toString().trim();
		if (temp.length() > 0) {
			mPrice[11] = Integer.parseInt(temp);
		}
	}

	// 정보 DB에 저장
	private void setNewInfo() {
		mDbOpen = new DbOpen(this);
		mDB = mDbOpen.getReadableDatabase();
		mDB.execSQL("insert into customer_info values ('" + mName + "', "
				+ mPrice[0] + "," + mPrice[1] + "," + mPrice[2] + ","
				+ mPrice[3] + "," + mPrice[4] + "," + mPrice[5] + ","
				+ mPrice[6] + "," + mPrice[7] + "," + mPrice[8] + ","
				+ mPrice[9] + "," + mPrice[10] + "," + mPrice[11] + ")");
		mDbOpen.close();
	}
}
