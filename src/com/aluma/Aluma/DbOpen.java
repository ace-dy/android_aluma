package com.aluma.Aluma;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbOpen extends SQLiteOpenHelper {

	private static final String dbName = "aluma.db";

	public DbOpen(Context context) {

		super(context, dbName, null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// password information db
		db.execSQL("CREATE TABLE PASSWORD" + "(PW text primary key);");
		// customer information db
		db.execSQL("CREATE TABLE CUSTOMER_INFO" + "(NAME text primary key,"
				+ "A_I INTEGER," + "O_I INTEGER," + "H_I INTEGER,"
				+ "as_I INTEGER," + "B_I INTEGER," + "P_I INTEGER,"
				+ "C_I INTEGER," + "M_I INTEGER," + "Fg_I INTEGER,"
				+ "fsg_I INTEGER," + "F_I INTEGER," + "fs_I INTEGER);");
		// sales information db
		db.execSQL("CREATE TABLE SALES_INFO"
				+ "(SALES_NUM INTEGER primary key AUTOINCREMENT,"
				+ "NAME text NOT NULL," + "DATE text NOT NULL,"
				+ "A_S INTEGER," + "O_S INTEGER," + "H_S INTEGER,"
				+ "as_S INTEGER," + "B_S INTEGER," + "P_S INTEGER,"
				+ "C_S INTEGER," + "M_S INTEGER," + "Fg_S INTEGER,"
				+ "fsg_S INTEGER," + "F_S INTEGER," + "fs_S INTEGER,"
				+ "ETC_P INTEGER," + "ETC_S INTEGER," + "ETC_N text,"
				+ "UNIQUE(SALES_NUM, NAME, DATE));");
		// password initialization
		db.execSQL("CREATE TABLE DEPOSIT_INFO"
				+ "(NAME text NOT NULL," + "DATE text NOT NULL,"
				+ "MONEY INTEGER," + "primary key(NAME, DATE));");
		db.execSQL("CREATE TABLE DIRECT_DEAL"
				+ "(DEAL_NUM INTEGER primary key AUTOINCREMENT,"
				+ "DATE text NOT NULL," + "PRODUCT_P INTEGER,"
				+ "PRODUCT_S INTEGER," + "PRODUCT_N text,"
				+ "UNIQUE(DEAL_NUM, DATE));");
		db.execSQL("INSERT INTO PASSWORD VALUES ('p');");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

}
