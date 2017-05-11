package com.aily.northeastelecstore.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.aily.northeastelecstore.R;
import com.aily.northeastelecstore.ui.base.BaseActivity;

import java.util.Vector;


public class CartActivity extends BaseActivity implements OnClickListener {

	private Button cart_login,cart_market;
	private ImageView cartButton;
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
				Log.d( "TIEJIANG", "handle message cartNum= " + cartNum);
				cart.add(cartNum);

				// test
				for (int i = 0; i < cart.size(); i ++){
					Log.d( "TIEJIANG", "handle message cart[i]= " + cart.get(i));
				}

			}
		};
	}

	@Override
	protected void findViewById() {
		cart_login=(Button)this.findViewById(R.id.cart_login);
		cart_market=(Button)this.findViewById(R.id.cart_market);
		cartButton = (ImageView)findViewById(R.id.cart);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		cart.removeAllElements();
	}

	@Override
	protected void initView() {
		cart_login.setOnClickListener(this);
		cart_market.setOnClickListener(this);
		cartButton.setOnClickListener(this);
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

			// TEST CODE BEGIN
//			Intent mIntent = new Intent(CartActivity.this, ShoppingCartActivity.class);
//			startActivity(mIntent);
			// TEST CODE BEGIN
			break;

		case R.id.cart:
			//判断cart容器是否为空，如果有数据则跳转到购物车的购物列表
			if (!cart.isEmpty()){
				Intent mIntent = new Intent(CartActivity.this, ShoppingCartActivity.class);
//				mIntent.putExtra("cartName", (String)cart.get(0));
				Bundle mBundle = new Bundle();
				mBundle.putSerializable("cartVector", cart);
				mIntent.putExtras(mBundle);
				startActivity(mIntent);
				Log.d("TIEJIANG","GOTO ShoppingCartActivity");
			}else{
				Toast.makeText(CartActivity.this, "亲，购物车是空的哦～", Toast.LENGTH_SHORT).show();
			}
			break;


		default:
			break;
		}
		
	}

}
