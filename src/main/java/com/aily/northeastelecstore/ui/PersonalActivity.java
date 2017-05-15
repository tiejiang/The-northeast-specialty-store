package com.aily.northeastelecstore.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aily.northeastelecstore.R;
import com.aily.northeastelecstore.ui.base.BaseActivity;
import com.aily.northeastelecstore.utils.CommonTools;
import com.aily.northeastelecstore.utils.ExitView;
import com.aily.northeastelecstore.widgets.CustomScrollView;


public class PersonalActivity extends BaseActivity implements OnClickListener {

	private ImageView mBackgroundImageView = null;
	private ImageView personal_icon_03;
	private Button mLoginButton,mMoreButton,mExitButton;
	private CustomScrollView mScrollView = null;
	private Intent mIntent=null;
	private ExitView exit;
	private LinearLayout Ly_login,Ly_Other;
	private RelativeLayout Ly_personalInfo;
	private TextView username, user_msg;
	private int LOGIN_CODE=100;
	private SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal);
		findViewById();
		initView();
		//检查是否等了，如果已经登录则不显示登录按钮的view
		preferences = this.getSharedPreferences("user_msg", Context.MODE_WORLD_READABLE);
		String userName = preferences.getString("id", null);
		Log.d("TIEJIANG", "PersonalActivity---userName= " + userName);
		if (userName != null){
			mLoginButton.setVisibility(View.INVISIBLE);
			user_msg = (TextView)findViewById(R.id.user_msg);
			user_msg.setText("用户名：" + userName);
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		//检查是否等了，如果已经登录则不显示登录按钮的view
		preferences = this.getSharedPreferences("user_msg", Context.MODE_WORLD_READABLE);
		String userName = preferences.getString("id", null);
		Log.d("TIEJIANG", "PersonalActivity---userName= " + userName);
		if (userName != null){
			mLoginButton.setVisibility(View.INVISIBLE);
		}else {
			mLoginButton.setVisibility(View.VISIBLE);
		}

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		mBackgroundImageView = (ImageView) findViewById(R.id.personal_background_image);
		mLoginButton = (Button) findViewById(R.id.personal_login_button);
		mScrollView = (CustomScrollView) findViewById(R.id.personal_scrollView);
		mMoreButton=(Button)this.findViewById(R.id.personal_more_button);
		mExitButton=(Button)this.findViewById(R.id.personal_exit);
		
		
		Ly_login=(LinearLayout)findViewById(R.id.login);
		Ly_personalInfo=(RelativeLayout)findViewById(R.id.personal);
		Ly_Other=(LinearLayout)findViewById(R.id.other_layout);
		username=(TextView)findViewById(R.id.username);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mScrollView.setImageView(mBackgroundImageView);
		personal_icon_03 = (ImageView)findViewById(R.id.personal_icon_03);
		
		mLoginButton.setOnClickListener(this);
		mMoreButton.setOnClickListener(this);
		mExitButton.setOnClickListener(this);
		personal_icon_03.setOnClickListener(this);  //全部订单
		
	}

	@Override
	public void onClick(View v) {
		//CommonTools.showShortToast(PersonalActivity.this, "稍后开放");
		switch (v.getId()) {
		case R.id.personal_login_button:
			mIntent=new Intent(PersonalActivity.this, LoginActivity.class);
			
			startActivityForResult(mIntent, LOGIN_CODE);
			break;

		case R.id.personal_more_button:
			mIntent=new Intent(PersonalActivity.this, MoreActivity.class);
			startActivity(mIntent);
			break;
			
		case R.id.personal_exit:
			
			//实例化SelectPicPopupWindow
			exit = new ExitView(PersonalActivity.this, itemsOnClick);
			//显示窗口
			exit.showAtLocation(PersonalActivity.this.findViewById(R.id.layout_personal), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置

			
			break;
		case R.id.personal_icon_03:
			Toast.makeText(PersonalActivity.this, "暂未开放订单检索功能", Toast.LENGTH_SHORT).show();
			
		default:
			break;
		}
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
		if(resultCode==20){
//			String name=data.getExtras().getString("username");
//			Log.i("name", name);
//			username.setText(name);
			if(Ly_login.isShown()){
				Ly_personalInfo.setVisibility(View.VISIBLE);
				Ly_login.setVisibility(View.GONE);
				Ly_Other.setVisibility(View.VISIBLE);
			}
			Ly_personalInfo.setVisibility(View.VISIBLE);
			Ly_login.setVisibility(View.GONE);
			Ly_Other.setVisibility(View.VISIBLE);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	//为弹出窗口实现监听类
    private OnClickListener  itemsOnClick = new OnClickListener(){

		public void onClick(View v) {
			
			switch (v.getId()) {
			case R.id.btn_exit:
				CommonTools.showShortToast(PersonalActivity.this, "退出程序");
				// 将用户名和密码清空
				SharedPreferences pre = getSharedPreferences("user_msg", MODE_WORLD_WRITEABLE);
				SharedPreferences.Editor editor = pre.edit();
				editor.putString("id", null);
				editor.putString("name", null);
				editor.commit();
				PersonalActivity.this.dismissDialog(R.id.btn_exit);
				break;
			case R.id.btn_cancel:
				PersonalActivity.this.dismissDialog(R.id.btn_cancel);
				
				break;
			default:
				break;
			}
		}
    };
	
}
