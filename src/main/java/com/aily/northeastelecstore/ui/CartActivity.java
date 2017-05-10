package com.aily.northeastelecstore.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.aily.northeastelecstore.R;
import com.aily.northeastelecstore.ui.base.BaseActivity;

import java.util.Vector;


public class CartActivity extends BaseActivity implements OnClickListener {

	private Button cart_login,cart_market;
	private Intent mIntent;
	//购物车的数据
	protected Vector cart = new Vector();
	protected String cartNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cart);
		findViewById();
		initView();


		/**
		 * 接受消息   必须先启动这个activity 实例化 此handler 才能保证 CategoryActivity提交购物车时候不出现空指针
		 **/
		CategoryActivity.mHandler = new Handler(){
			@Override
			public void handleMessage(Message msg)
			{
				cartNum = msg.obj.toString().trim();
				cart.add(cartNum);
				Log.d( "TIEJIANG", "handle message cartNum= " + cartNum);
			}
		};

	}

	@Override
	protected void findViewById() {
		cart_login=(Button)this.findViewById(R.id.cart_login);
		cart_market=(Button)this.findViewById(R.id.cart_market);
	}

	@Override
	protected void initView() {
		cart_login.setOnClickListener(this);
		cart_market.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cart_login:
			mIntent=new Intent(this, LoginActivity.class);
			startActivity(mIntent);
			
			break;
			
		case R.id.cart_market:
			mIntent=new Intent(this, CategoryActivity.class);
			startActivity(mIntent);
			break;

		default:
			break;
		}
		
	}

}
