package com.aluma.Aluma;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class GetConditionInfo {
	private String mName;
	private DbOpen mDbOpen;
	private SQLiteDatabase mDB;
	private ArrayList<String> customerInfo;
	private int[] price, aTotalSale, aTotalDeposit;
	private int sale = 0, deposit = 0, year, month, totalSale, totalDeposit,
			sIndex = 0, dIndex = 0;
	private Context mContext;

	public GetConditionInfo(String name, Context context) {
		mName = name;
		mContext = context;
		getCustomerInfo();
		totalSale = 0;
		totalDeposit = 0;
	}

	public ArrayList<String> getInfo() {
		return customerInfo;
	}

	public int getIndexInfo() {
		return sIndex;
	}

	private void getCustomerInfo() {
		customerInfo = new ArrayList<String>();
		mDbOpen = new DbOpen(mContext);
		mDB = mDbOpen.getReadableDatabase();

		getTime();
		getPriceFromDB(mName);
		aTotalSale = new int[month];
		aTotalDeposit = new int[month];

		for (int i = 1; i <= month; i++) {
			String textMonth = String.format("%02d", i);
			String date = Integer.toString(year) + "/" + textMonth;

			Cursor mCur = mDB.rawQuery(
					"select * from sales_info where name = '" + mName
							+ "' and date like '" + date + "%';", null);
			int size = mCur.getColumnCount(), j;
			int[] value = new int[size - 6];
			sale = 0;

			customerInfo.add(date);
			while (mCur.moveToNext()) {
				for (j = 3; j < size - 3; j++) {
					value[j - 3] = mCur.getInt(j);
					sale += value[j - 3] * price[j - 3];
				}
				sale += mCur.getInt(j) * mCur.getInt(j + 1);
			}
			totalSale += sale;
			aTotalSale[i - 1] = totalSale;
			customerInfo.add(String.format("%,d", sale));

			getDepositDataFromDB(mName, date);
			aTotalDeposit[i - 1] = totalDeposit;
			customerInfo.add(String.format("%,d", deposit));
			if (i != month)
				customerInfo.add("-");

		}
		customerInfo.add(String.format("%,d", totalSale - totalDeposit));
		customerInfo.add(String.format("%,d", totalSale));
		customerInfo.add(String.format("%,d", totalDeposit));
		customerInfo.add(String.format("%,d", totalSale - totalDeposit));
		mDbOpen.close();
		Log.d("totalSale", String.format("%,d", totalSale));
		Log.d("totalDeposit", String.format("%,d", totalDeposit));
		Log.d("outstand", String.format("%,d", totalSale - totalDeposit));
		// 미수금 처리
		while (sIndex < month && dIndex < month) {
			if (aTotalSale[sIndex] < aTotalDeposit[dIndex]) {
				sIndex++;
			} else if (aTotalSale[sIndex] == aTotalDeposit[dIndex]) {
				dIndex++;
				sIndex++;
			} else {
				dIndex++;
			}
		}
	}

	private void getTime() {
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH) + 1;
	}

	private void getPriceFromDB(String mName) {
		Cursor mCur = mDB.rawQuery("select * from customer_info where name = '"
				+ mName + "'", null);

		int size = mCur.getColumnCount(), i;

		price = new int[size - 1];
		mCur.moveToNext();
		for (i = 1; i < size; i++) {
			price[i - 1] = mCur.getInt(i);
		}
	}

	private void getDepositDataFromDB(String mName, String date) {
		deposit = 0;
		Cursor mCur = mDB.rawQuery(
				"select MONEY from DEPOSIT_info where name = '" + mName
						+ "' and date like '" + date + "%';", null);

		while (mCur.moveToNext()) {
			deposit += mCur.getInt(0);
		}
		totalDeposit += deposit;
	}

}
