package com.aluma.Aluma;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class CustomerListActivity extends Activity {

	private EditText search;
	private Button addBt;
	private ListView listView;
	private ArrayList<String> arrayList;
	private ArrayAdapter<String> adapter;
	private DbOpen mDbOpen;
	private SQLiteDatabase mDB;
	private AlertDialog mDialog = null;
	private String mMassage = null;
	private int mPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_list);

		getArrayList();
		setScreen();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getArrayList();
		setListView();
	}

	private void getArrayList() {
		mDbOpen = new DbOpen(this);
		mDB = mDbOpen.getReadableDatabase();
		Cursor mCur = mDB.rawQuery("select name from customer_info;", null);
		arrayList = new ArrayList<String>();
		while (mCur.moveToNext()) {
			arrayList.add(mCur.getString(0));
		}
		mDB.close();
	}

	private void setScreen() {
		setSearch();
		setAddBt();
		setListView();
	}

	private void setSearch() {
		search = (EditText) findViewById(R.id.et_search);
		search.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				CustomerListActivity.this.adapter.getFilter().filter(s);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void setAddBt() {
		addBt = (Button) findViewById(R.id.b_add);
		addBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CustomerListActivity.this,
						AddCustomerActivity.class);
				startActivity(intent);
			}
		});
	}

	private void setListView() {
		listView = (ListView) findViewById(R.id.list_customer);

		search.clearFocus();
		adapter = new ArrayAdapter<String>(this, R.layout.layout_forlist,
				R.id.t_forlist, arrayList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(CustomerListActivity.this,
						TransactionActivity.class);
				intent.putExtra("name", arrayList.get(position));
				startActivity(intent);
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				mPosition = position;
				mMassage = "'" + arrayList.get(mPosition) + "' 항목을 삭제 하시겠습니까?";
				mDialog = createDialog();
				mDialog.show();

				return false;
			}
		});
	}

	// 삭제 확인 다이얼로그
	private AlertDialog createDialog() {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle(mMassage);
		ab.setCancelable(false);
		ab.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				deleteInfo();
				CustomerListActivity.this.onResume();
				mDialog.dismiss();
			}

		});

		ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				mDialog.dismiss();
			}
		});

		return ab.create();
	}

	private void deleteInfo() {
		mDbOpen = new DbOpen(this);
		mDB = mDbOpen.getReadableDatabase();
		mDB.execSQL("delete from customer_info where name = '"
				+ arrayList.get(mPosition) + "';");
		mDbOpen.close();
	}
}
