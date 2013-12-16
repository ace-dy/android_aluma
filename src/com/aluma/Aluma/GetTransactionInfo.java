package com.aluma.Aluma;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GetTransactionInfo {
	private String mName;
	private DbOpen mDbOpen;
	private SQLiteDatabase mDB;
	private ArrayList<String> customerInfo;
	private int year, month;
	private Context mContext;

	public GetTransactionInfo(String name, Context context) {
		mName = name;
		mContext = context;
		getCustomerInfo();
	}

	public ArrayList<String> getInfo() {
		return customerInfo;
	}

	private void getCustomerInfo() {
		customerInfo = new ArrayList<String>();
		mDbOpen = new DbOpen(mContext);
		mDB = mDbOpen.getReadableDatabase();

		getTime();

		for (int i = month; i >= 1; i--) {
			String textMonth = String.format("%02d", i);
			String date = Integer.toString(year) + "/" + textMonth;

			Cursor mCur = mDB.rawQuery(
					"select * from sales_info where name = '" + mName
							+ "' and date like '" + date + "%' order by date desc;", null);
			int size = mCur.getColumnCount(), j;
			
			// sales_num | flag | date | code | value | 이렇게 저장
			while (mCur.moveToNext()) {
				customerInfo.add(Integer.toString(mCur.getInt(0))); // sales_num
				customerInfo.add("1"); //flag
				customerInfo.add(mCur.getString(2)); //date
				
				for (j = 3; j < size - 3; j++) {
					if (mCur.getInt(j) != 0) {
						customerInfo.add(Integer.toString(j - 3));  //column_code
						customerInfo.add(String.format("%,d", mCur.getInt(j))); //column_value
						customerInfo.add(Integer.toString(mCur.getInt(0))); // sales_num
						customerInfo.add("0"); //flag
						customerInfo.add(mCur.getString(2)); //date
					}
				}
				//기타 판매 품목 여부 확인
				if (mCur.getInt(j) == 0) {
					int last = customerInfo.size() - 1;
					customerInfo.remove(last);
					customerInfo.remove(last-1);
					customerInfo.remove(last-2);
				}else{
					customerInfo.add(mCur.getString(j+2));
					customerInfo.add(String.format("%,d", mCur.getInt(j+1)));
				}
			}
		}
		
		mDbOpen.close();
	}

	private void getTime() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
	}

}
