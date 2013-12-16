package com.aluma.Aluma;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ConditionOfSalesActivity extends Activity {

	private TextView tDate;
	private TextView tSale, tDeposit, tOutstanding;
	private Timer timer;
	private boolean mIsBackButtonTouched = false;
	private DbOpen mDbOpen;
	private SQLiteDatabase mDB;
	private int sale = 0, deposit = 0, outstanding = 0, year;
	private int[] price; // 거래처 단가 정보 저장

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_condition);

		setTime();
		getSalesDataFromDB();
		getDepositDataFromDB();
		setScreen();

	}

	@Override
	protected void onResume() {
		super.onResume();
		getSalesDataFromDB();
		getDepositDataFromDB();
		setScreen();
	}

	private void setTime() {

		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);

		String strTime = year + "년 " + month + "월 " + day + "일";

		tDate = (TextView) findViewById(R.id.t_date);
		tDate.setText(strTime);
	}

	// 2013년 판매 정보 가져오기
	private void getSalesDataFromDB() {
		mDbOpen = new DbOpen(this);
		mDB = mDbOpen.getReadableDatabase();
		sale = 0;
		Cursor mCur = mDB.rawQuery("select * from sales_info where date like '"
				+ year + "%';", null);

		int size = mCur.getColumnCount(), i;
		String name = null;
		int[] value = new int[size - 6];
		while (mCur.moveToNext()) {
			i = 1;
			name = mCur.getString(i);
			getPriceFromDB(name);
			for (i = 3; i < size - 3; i++) {
				value[i - 3] = mCur.getInt(i);
				sale += value[i - 3] * price[i - 3];
			}
			sale += mCur.getInt(i) * mCur.getInt(i + 1);
		}
		// mDbOpen.close();
	}

	// 거래처별 단가 정보 가져오기
	private void getPriceFromDB(String mName) {
		Cursor mCur = mDB.rawQuery("select * from customer_info where name ='"
				+ mName + "'", null);

		int size = mCur.getColumnCount(), i;

		price = new int[size - 1];
		mCur.moveToNext();
		for (i = 1; i < size; i++) {
			price[i - 1] = mCur.getInt(i);
		}
	}

	private void getDepositDataFromDB() {
		deposit = 0;
		outstanding = 0;
		Cursor mCur = mDB.rawQuery(
				"select MONEY from DEPOSIT_info where date like '" + year
						+ "%';", null);
		while (mCur.moveToNext()) {
			deposit += mCur.getInt(0);
		}
		outstanding = sale - deposit;
		mDbOpen.close();
	}

	private void setScreen() {
		tSale = (TextView) findViewById(R.id.t_sale);
		tDeposit = (TextView) findViewById(R.id.t_deposit);
		tOutstanding = (TextView) findViewById(R.id.t_outstand);

		tSale.setText(String.format("%,d", sale) + "원");
		tDeposit.setText(String.format("%,d", deposit) + "원");
		tOutstanding.setText(String.format("%,d", outstanding) + "원");
		LinearLayout layout = (LinearLayout) findViewById(R.id.con_layout);

		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ConditionOfSalesActivity.this,
						CustomerListActivity.class);
				startActivity(intent);
			}
		});
	}

	// 뒤로가기 두번 클릭시 종료
	public void onBackPressed() {
		if (mIsBackButtonTouched == false) {
			Toast.makeText(this, "뒤로 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT)
					.show();
			mIsBackButtonTouched = true;

			TimerTask second = new TimerTask() {
				@Override
				public void run() {
					timer.cancel();
					timer = null;
					mIsBackButtonTouched = false;
				}
			};
			if (timer != null) {
				timer.cancel();
				timer = null;
			}
			timer = new Timer();
			timer.schedule(second, 2000);
		} else
			super.onBackPressed();
	}
}
