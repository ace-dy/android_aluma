package com.aluma.Aluma;

import java.util.ArrayList;
import java.util.Calendar;

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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.aluma.Aluma.ChangePriceDialog.ChangePriceDialogListener;
import com.aluma.Aluma.SaleDateDialog.SaleDateDialogListener;

public class SaleActivity extends FragmentActivity implements
		SaleDateDialogListener, ChangePriceDialogListener {

	private TextView tName, tDate;
	private Button okBt, cancleBt;
	private GridView gridInfo;
	private EditText etA, etO, etH, eta, etB, etP, etC, etM, etFg, etfg, etF,
			etf;
	private ArrayList<String> customerInfo;
	private String mName, mDate, mPrice;
	private DbOpen mDbOpen;
	private SQLiteDatabase mDB;
	private int[] order;
	private int year, month, day, mPosition;
	private boolean isChange;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sale);

		setScreen();
	}

	private void setScreen() {
		Intent intent = this.getIntent();
		mName = intent.getStringExtra("name");

		getCurrentTime();
		setText();
		setDate();
		setButton();
		setGrid();
		setEditText();
	}

	private void setDate() {
		mDate = year + "/" + String.format("%02d", month) + "/"
				+ String.format("%02d", day);
		tDate.setText(mDate);
	}

	private void getCurrentTime() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
		day = c.get(Calendar.DATE);
	}

	private void setText() {
		tName = (TextView) findViewById(R.id.t_name_sale);
		tName.setText(mName);

		tDate = (TextView) findViewById(R.id.t_saledate);
		// 날짜 클릭시
		tDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = SaleActivity.this
						.getSupportFragmentManager();
				SaleDateDialog mDialog = new SaleDateDialog();
				mDialog.show(fm, "SaleDateDialog");
			}
		});
	}

	// 날짜 다이얼로그 졸요 후
	public void onFinishDateDialog(int iyear, int imonth, int iday) {
		year = iyear;
		month = imonth;
		day = iday;
		setDate();
	}

	private void setButton() {
		okBt = (Button) findViewById(R.id.bt_okadd);
		cancleBt = (Button) findViewById(R.id.bt_cancleadd);
		// ok버튼 클릭시 내용 DB에 저장
		okBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getOrderInfo();
				if (isChange) {
					setOrderInfo();
				}
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

	// 물품정보 표시
	private void setGrid() {
		getPriceFromDB(mName);

		gridInfo = (GridView) findViewById(R.id.gridprice);
		PriceInfoAdapter priceAdapter = new PriceInfoAdapter(this, customerInfo);
		gridInfo.setAdapter(priceAdapter);
		gridInfo.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				mPosition = position;
				FragmentManager fm = SaleActivity.this
						.getSupportFragmentManager();
				ChangePriceDialog mDialog = new ChangePriceDialog();
				mDialog.show(fm, "ChangePriceDialog");
			}
		});
	}

	// 단가 변경 후
	public void onFinishEditDialog(String inputText) {
		mPrice = inputText;
		String temp = String.format("%,d", Integer.parseInt(mPrice));
		// 이전의 단가와 동일할 경우 단가 변경하지 않음
		if (!(customerInfo.get(mPosition).equals(temp))) {
			updateInfo();
		}
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
	}

	private void getOrderInfo() {
		order = new int[12];

		// 입력된 값이 있는지 확인
		isChange = false;

		for (int i = 0; i < 12; i++) {
			order[i] = 0;
		}

		String temp = etA.getText().toString().trim();
		if (temp.length() > 0) {
			order[0] = Integer.parseInt(temp);
			isChange = true;
		}

		temp = etO.getText().toString().trim();
		if (temp.length() > 0) {
			order[1] = Integer.parseInt(temp);
			isChange = true;
		}

		temp = etH.getText().toString().trim();
		if (temp.length() > 0) {
			order[2] = Integer.parseInt(temp);
			isChange = true;
		}

		temp = eta.getText().toString().trim();
		if (temp.length() > 0) {
			order[3] = Integer.parseInt(temp);
			isChange = true;
		}

		temp = etB.getText().toString().trim();
		if (temp.length() > 0) {
			order[4] = Integer.parseInt(temp);
			isChange = true;
		}

		temp = etP.getText().toString().trim();
		if (temp.length() > 0) {
			order[5] = Integer.parseInt(temp);
			isChange = true;
		}

		temp = etC.getText().toString().trim();
		if (temp.length() > 0) {
			order[6] = Integer.parseInt(temp);
			isChange = true;
		}

		temp = etM.getText().toString().trim();
		if (temp.length() > 0) {
			order[7] = Integer.parseInt(temp);
			isChange = true;
		}

		temp = etFg.getText().toString().trim();
		if (temp.length() > 0) {
			order[8] = Integer.parseInt(temp);
			isChange = true;
		}

		temp = etfg.getText().toString().trim();
		if (temp.length() > 0) {
			order[9] = Integer.parseInt(temp);
			isChange = true;
		}

		temp = etF.getText().toString().trim();
		if (temp.length() > 0) {
			order[10] = Integer.parseInt(temp);
			isChange = true;
		}

		temp = etf.getText().toString().trim();
		if (temp.length() > 0) {
			order[11] = Integer.parseInt(temp);
			isChange = true;
		}
	}

	// 주문정보 DB에 저장
	private void setOrderInfo() {
		mDbOpen = new DbOpen(this);
		mDB = mDbOpen.getReadableDatabase();
		mDB.execSQL("insert into sales_info (name, date,"
				+ " a_s, o_s, h_s, as_s, b_s, p_s, c_s, m_s,"
				+ " fg_s, fsg_s, f_s, fs_s, etc_p, etc_s, etc_n)"
				+ " values ('"
				+ mName
				+ "', '"
				+ mDate
				+ "',"
				+ order[0]
				+ ","
				+ order[1]
				+ ","
				+ order[2]
				+ ","
				+ order[3]
				+ ","
				+ order[4]
				+ ","
				+ order[5]
				+ ","
				+ order[6]
				+ ","
				+ order[7]
				+ ","
				+ order[8]
				+ ","
				+ order[9]
				+ ","
				+ order[10]
				+ ","
				+ order[11]
				+ ", null, null, null)");
		mDbOpen.close();
	}

}
