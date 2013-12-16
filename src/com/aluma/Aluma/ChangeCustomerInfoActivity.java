package com.aluma.Aluma;

import java.util.ArrayList;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.aluma.Aluma.ChangePriceDialog.ChangePriceDialogListener;

public class ChangeCustomerInfoActivity extends FragmentActivity implements
		ChangePriceDialogListener {

	private TextView tName;
	private Button okBt;
	private GridView gridInfo;
	private ArrayList<String> customerInfo;
	private String mName, mPrice;
	private DbOpen mDbOpen;
	private SQLiteDatabase mDB;
	private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_changecustomerinfo);

		setScreen();
	}

	private void setScreen() {
		Intent intent = this.getIntent();
		mName = intent.getStringExtra("name");

		setText();
		setButton();
		setGrid();
	}

	private void setText() {
		tName = (TextView) findViewById(R.id.t_name_changeinfo);
		tName.setText(mName);
	}

	private void setButton() {
		okBt = (Button) findViewById(R.id.bt_okchangeinfo);
		okBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	// 물품정보 표시
	private void setGrid() {
		getPriceFromDB(mName);

		gridInfo = (GridView) findViewById(R.id.gridpricechange);
		PriceInfoAdapter priceAdapter = new PriceInfoAdapter(this, customerInfo);
		gridInfo.setAdapter(priceAdapter);
		gridInfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				mPosition = position;
				FragmentManager fm = ChangeCustomerInfoActivity.this
						.getSupportFragmentManager();
				ChangePriceDialog mDialog = new ChangePriceDialog();
				mDialog.show(fm, "ChangePriceDialog");
			}
		});
	}

	// 단가 DB에 저장
	private void updateInfo() {
		String[] columnName = { "A_I", "O_I", "H_I", "as_I", "B_I", "P_I",
				"C_I", "M_I", "Fg_I", "fsg_I", "F_I", "fs_I" };
		int index = Integer.parseInt(customerInfo.get(mPosition - 1));

		mDbOpen = new DbOpen(this);
		mDB = mDbOpen.getReadableDatabase();
		mDB.execSQL("update customer_info set " + columnName[index] + " = "
				+ mPrice + " where name = '" + mName + "';");
		mDbOpen.close();
		setGrid();
	}

	private void getPriceFromDB(String mName) {
		customerInfo = new ArrayList<String>();
		mDbOpen = new DbOpen(this);
		mDB = mDbOpen.getReadableDatabase();
		Cursor mCur = mDB.rawQuery("select * from customer_info where name = '"
				+ mName + "'", null);

		int size = mCur.getColumnCount(), i;
		mCur.moveToNext();
		for (i = 1; i < size; i++) {
			customerInfo.add(Integer.toString(i - 1));
			customerInfo.add(String.format("%,d", mCur.getInt(i)));
		}
		mDbOpen.close();
	}

	@Override
	public void onFinishEditDialog(String inputText) {
		mPrice = inputText;
		String temp = String.format("%,d", Integer.parseInt(mPrice));
		// 이전의 단가와 동일할 경우 단가 변경하지 않음
		if (!(customerInfo.get(mPosition).equals(temp))) {
			updateInfo();
		}
	}

}
