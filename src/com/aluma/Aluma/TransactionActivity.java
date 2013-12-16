package com.aluma.Aluma;

import java.util.ArrayList;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aluma.Aluma.ChangeConditionDialog.ChangeConditionDialogListener;
import com.aluma.Aluma.ChangeTransactionDialog.ChangeTransactionDialogListener;

public class TransactionActivity extends FragmentActivity implements
		ChangeTransactionDialogListener, ChangeConditionDialogListener {

	private Button customerBt, saleBt;
	private String name, numOforder, deposit;
	private GridView gridCondition, gridTransaction;
	private ArrayList<String> customerInfo, transactionInfo;
	private TextView tCondition, tTransaction;
	private LinearLayout layoutCond, layoutTran;
	private int mCondPosition, mTranPosition;
	private DbOpen mDbOpen;
	private SQLiteDatabase mDB;
	private final int INSERT = 0, UPDATE = 1, DELETE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tran);

		setScreen();
	}

	@Override
	protected void onResume() {
		super.onResume();
		gridCondition();
		gridTransaction();
	}

	private void setScreen() {
		Intent intent = this.getIntent();
		name = intent.getStringExtra("name");
		layoutCond = (LinearLayout) findViewById(R.id.LinearLayoutCond);
		layoutTran = (LinearLayout) findViewById(R.id.LinearLayoutTran);

		setCustomerBt();
		setSaleBt();
		setConditionT();
		setTransactionT();
		gridCondition();
		gridTransaction();
		conditionView();

	}

	private void setCustomerBt() {
		customerBt = (Button) findViewById(R.id.b_customer);
		customerBt.setText(name);
		customerBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TransactionActivity.this,
						ChangeCustomerInfoActivity.class);
				intent.putExtra("name", name);
				startActivity(intent);
			}
		});
	}

	private void setSaleBt() {
		saleBt = (Button) findViewById(R.id.b_sale);
		saleBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TransactionActivity.this,
						SaleActivity.class);
				intent.putExtra("name", name);
				startActivity(intent);
			}
		});
	}

	private void setConditionT() {
		tCondition = (TextView) findViewById(R.id.t_cond);
		tCondition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				conditionView();
			}
		});
	}

	private void setTransactionT() {
		tTransaction = (TextView) findViewById(R.id.t_tran);
		tTransaction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				transactionView();
			}
		});
	}

	private void conditionView() {
		layoutCond.setVisibility(View.VISIBLE);
		layoutTran.setVisibility(View.GONE);
		tCondition.setBackgroundColor(Color.parseColor("#D0D7E7"));
		tTransaction.setBackgroundColor(Color.parseColor("#F2F2F2"));
	}

	private void transactionView() {
		layoutCond.setVisibility(View.GONE);
		layoutTran.setVisibility(View.VISIBLE);
		tCondition.setBackgroundColor(Color.parseColor("#F2F2F2"));
		tTransaction.setBackgroundColor(Color.parseColor("#D0D7E7"));
	}

	private void gridCondition() {
		GetConditionInfo getInfo = new GetConditionInfo(name, this);
		customerInfo = getInfo.getInfo();
		int sIndex = getInfo.getIndexInfo();
		
		setConditionText();
		
		gridCondition = (GridView) findViewById(R.id.gridCondition);
		ConditionAdapter conditionAdapter = new ConditionAdapter(
				TransactionActivity.this, customerInfo, sIndex);
		gridCondition.setAdapter(conditionAdapter);
		gridCondition.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				mCondPosition = position;
				FragmentManager fm = TransactionActivity.this
						.getSupportFragmentManager();
				ChangeConditionDialog mDialog = new ChangeConditionDialog();
				mDialog.show(fm, "ChangeConditionDialog");
			}
		});
	}

	private void setConditionText() {
		TextView mTextView;
		int last = customerInfo.size()-1;
		
		mTextView = (TextView) findViewById(R.id.t_outstand);
		mTextView.setText(customerInfo.get(last));
		customerInfo.remove(last--);
		
		mTextView = (TextView) findViewById(R.id.t_totaldeposit);
		mTextView.setText(customerInfo.get(last));
		customerInfo.remove(last--);
		
		
		mTextView = (TextView) findViewById(R.id.t_totalsale);
		mTextView.setText(customerInfo.get(last));
		customerInfo.remove(last);
	}

	@Override
	public void onFinishEditCondDialog(String inputText) {
		deposit = inputText;
		String temp = String.format("%,d", Integer.parseInt(deposit));
		// 이전의 입금액과 동일할 경우 변경하지 않음
		if (customerInfo.get(mCondPosition).equals("0")) {
			if (!(customerInfo.get(mCondPosition).equals(temp))) {
				modifyConditionInfo(INSERT);
			}
		} else {
			if (deposit.equals("0")) {
				modifyConditionInfo(DELETE);
			} else if (!(customerInfo.get(mCondPosition).equals(temp))) {
				modifyConditionInfo(UPDATE);
			}
		}
	}

	private void modifyConditionInfo(int flag) {
		String mDate = customerInfo.get(mCondPosition - 2);

		mDbOpen = new DbOpen(this);
		mDB = mDbOpen.getReadableDatabase();
		switch (flag) {
		case INSERT:
			mDB.execSQL("insert into deposit_info values ('" + name + "', '"
					+ mDate + "', " + deposit + ");");
			break;
		case UPDATE:
			mDB.execSQL("update deposit_info set money = " + deposit
					+ " where name = '" + name + "' and date = '" + mDate
					+ "';");
			break;
		case DELETE:
			mDB.execSQL("delete from deposit_info where name = '" + name
					+ "' and date = '" + mDate + "';");
			break;
		}
		mDbOpen.close();
		gridCondition();
	}

	private void gridTransaction() {
		GetTransactionInfo getInfo = new GetTransactionInfo(name, this);
		transactionInfo = getInfo.getInfo();

		gridTransaction = (GridView) findViewById(R.id.gridTransaction);
		TransactionAdapter transactionAdapter = new TransactionAdapter(
				TransactionActivity.this, transactionInfo);
		gridTransaction.setAdapter(transactionAdapter);
		gridTransaction.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				mTranPosition = position;
				FragmentManager fm = TransactionActivity.this
						.getSupportFragmentManager();
				ChangeTransactionDialog mDialog = new ChangeTransactionDialog();
				mDialog.show(fm, "ChangeTransactionDialog");
			}
		});
	}

	@Override
	public void onFinishEditTranDialog(String inputText) {
		numOforder = inputText;
		String temp = String.format("%,d", Integer.parseInt(numOforder));
		int index = mTranPosition + ((mTranPosition / 3) * 2 + 2);
		// 이전의 갯수와 동일할 경우 갯수 변경하지 않음
		if (!(transactionInfo.get(index).equals(temp))) {
			updateTransactionInfo();
		}
	}

	private void updateTransactionInfo() {
		// TODO Auto-generated method stub
		String[] columnName = { "A_S", "O_S", "H_S", "as_S", "B_S", "P_S",
				"C_S", "M_S", "Fg_S", "fsg_S", "F_S", "fs_S" };
		int cIndex, dIndex, salesNum;
		String date, productCode;

		dIndex = mTranPosition + ((mTranPosition / 3) * 2 + 2);
		cIndex = Integer.parseInt(transactionInfo.get(dIndex - 1));

		salesNum = Integer.parseInt(transactionInfo.get(dIndex - 4));
		date = transactionInfo.get(dIndex - 2);
		productCode = columnName[cIndex];

		mDbOpen = new DbOpen(this);
		mDB = mDbOpen.getReadableDatabase();
		mDB.execSQL("update sales_info set " + productCode + " = " + numOforder
				+ " where name = '" + name + "' and sales_num = " + salesNum
				+ " and date = '" + date + "';");
		mDbOpen.close();
		gridTransaction();
	}

}
