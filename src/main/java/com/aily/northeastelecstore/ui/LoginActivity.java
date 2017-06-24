package com.aily.northeastelecstore.ui;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aily.northeastelecstore.R;
import com.aily.northeastelecstore.utils.ObtainData;
import com.aily.northeastelecstore.utils.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity implements OnClickListener {
	private EditText userEditText, pwdEditText, ipSet;
	private Button cancelButton, loginButton, registerButton;
	private String username, pwd;
	private static String jsonStr = "" ;
	private static final int MAX_REQUIRE_TIME = 3;
	private static final int SHOW_HINT_TIME = 500;
	private boolean isConnect;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		//检查网络状态
		ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);
		boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		if(wifi|internet){
			//执行相关操作
			isConnect = true;
		}else{
			Toast.makeText(getApplicationContext(),  "请检查网络连接", Toast.LENGTH_LONG).show();
			isConnect = false;
		}

		userEditText = (EditText)findViewById(R.id.userEditText);
		pwdEditText = (EditText)findViewById(R.id.pwdEditText);
		ipSet  = (EditText)findViewById(R.id.ip_set);
		cancelButton = (Button)findViewById(R.id.cancelButton);
		loginButton = (Button)findViewById(R.id.loginButton);
		registerButton = (Button)findViewById(R.id.registerButton);



		cancelButton.setOnClickListener(this);
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
//		String primitiveUrl = "http://192.168.10.241";
		String IP = ipSet.getText().toString().trim();
		Log.d("TIEJINAG", "IP= " + IP);
		switch (arg0.getId()) {
			case R.id.loginButton:
				if (isConnect) {
					if (validate()) {
						if (!queryForHttpPost(username, pwd, "http://" + IP + "/web/php/")) {
							Toast.makeText(LoginActivity.this, "登录失败！", Toast.LENGTH_SHORT).show();
						}else {
							saveUserMsg(username, pwd);
							//登陆条件符合 进入主界面
							Intent intent = new Intent();
							intent.setClass(LoginActivity.this, CategoryActivity.class);
							startActivity(intent);
							finish();
						}
					}
				}else {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("提醒")
							.setMessage("请检查是否连接网络")
							.setPositiveButton("确定", null);
					builder.setCancelable(false);
					builder.create()
							.show();
				}

				break;

			case R.id.cancelButton:
//			Intent intent = new Intent();
//			intent.setClass(LoginActivity.this, MainActivity.class);
//			overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				finish();
				System.exit(0);
				break;
			case R.id.registerButton:
				//注册的业务逻辑
				LayoutInflater layoutInflater = LayoutInflater.from(LoginActivity.this);
				final View registerView = layoutInflater.inflate(R.layout.register, null);
				showRegister(registerView);
				break;
		}
	}
	//注册的dialog 和 view
	private void showRegister(final View view){
		AlertDialog builder = new AlertDialog.Builder(LoginActivity.this)
				.setView(view)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						boolean is_success = false;
						EditText username = (EditText)view.findViewById(R.id.username);
						EditText password = (EditText)view.findViewById(R.id.password);
						EditText name = (EditText)view.findViewById(R.id.name);
						EditText phonenum = (EditText)view.findViewById(R.id.phonenum);

						String usernameStr = username.getText().toString().trim();
						String passwordStr = password.getText().toString().trim();
						String nameStr = name.getText().toString().trim();
						String phonenumStr = phonenum.getText().toString().trim();
						//上传注册信息
						is_success = ObtainData.postRegisterMsg(usernameStr, passwordStr, nameStr, phonenumStr);
						if (is_success) {
							Toast.makeText(LoginActivity.this, "注册成功,请登录", Toast.LENGTH_LONG).show();
							userEditText.setText(usernameStr);
							pwdEditText.setText(passwordStr);
						}else {
							Toast.makeText(LoginActivity.this, "此用户名已经注册", Toast.LENGTH_LONG).show();
						}
					}
				})
				.setNegativeButton("取消", null)
				.create();
		builder.show();
	}
	// 验证方法  输入是否为空
	private boolean validate(){
		username = userEditText.getText().toString().trim();
		if(username.equals("")){
			Toast.makeText(LoginActivity.this, "用户名称是必填项！", Toast.LENGTH_SHORT).show();
			return false;
		}
		pwd = pwdEditText.getText().toString().trim();
		if(pwd.equals("")){
			Toast.makeText(LoginActivity.this, "用户密码是必填项!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	// 请求服务端 获得响应
	private boolean queryForHttpPost(String account, String password, String  ip_address){
		DefaultHttpClient mHttpClient = new DefaultHttpClient();
		HttpPost mPost = new HttpPost(Utils.primitiveUrl+"login.php");
		List<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
		pairs.add(new BasicNameValuePair("username", account));
		pairs.add(new BasicNameValuePair("password", password));

		try {
			mPost.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			HttpResponse response = mHttpClient.execute(mPost);
			int res = response.getStatusLine().getStatusCode();

			if (res == 200) {
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					String info = EntityUtils.toString(entity);
					System.out.println("服务端返回数据info-----------"+info);
					//以下主要是对服务器端返回的数据进行解析
					JSONObject jsonObject=null;
					//flag为登录成功与否的标记,从服务器端返回的数据
					String flag="";
					String name="";
					String userid="";
					try {
						jsonObject = new JSONObject(info);
						flag = jsonObject.getString("flag");
						name = jsonObject.getString("name");
						userid = jsonObject.getString("userid");

						System.out.println("flag:" + flag);
						System.out.println("name:" + name);
						System.out.println("userid:" + userid);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					//根据服务器端返回的标记,判断服务端端验证是否成功
					if(flag.equals("success")){
						return true;
					}
					else{
						return false;
					}
				}
				else{
					return false;
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	// 保存用户名密码
	private void saveUserMsg(String username, String pwd){
		String id = username;
		String name = pwd;
		// 共享信息
		SharedPreferences pre = getSharedPreferences("user_msg", MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = pre.edit();
		editor.putString("id", id);
		editor.putString("name", name);
		editor.commit();
	}
}

//
//import java.util.HashMap;
//import java.util.concurrent.Callable;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.ToggleButton;
//
//import com.aily.northeastelecstore.R;
//import com.aily.northeastelecstore.bean.Constants;
//import com.aily.northeastelecstore.task.Callback;
//import com.aily.northeastelecstore.ui.base.BaseActivity;
//
//public class LoginActivity extends BaseActivity implements OnClickListener {
//
//private static final String Tag="LoginActivity";
//private LoginActivity loginActivity=null;
//	private ImageView loginLogo,login_more;
//	private EditText loginaccount,loginpassword;
//	private ToggleButton isShowPassword;
//	private boolean isDisplayflag=false;//是否显示密码
//	private String getpassword;
//	private Button loginBtn,register;
//	private Intent mIntent;
//	private String serverAddress="http://mdemo.e-cology.cn/login.do";
//	public static String MOBILE_SERVERS_URL="http://mserver.e-cology.cn/servers.do";
//	 String username;
//	 String password;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_login);
//
//		loginActivity=LoginActivity.this;
//		findViewById();
//		initView();
//	}
//
//	@Override
//	protected void findViewById() {
//		loginLogo=(ImageView)this.findViewById(R.id.logo);
//		login_more=(ImageView)this.findViewById(R.id.login_more);
//		loginaccount=(EditText)this.findViewById(R.id.loginaccount);
//		loginpassword=(EditText)this.findViewById(R.id.loginpassword);
//
//		isShowPassword=(ToggleButton)this.findViewById(R.id.isShowPassword);
//		loginBtn=(Button)this.findViewById(R.id.login);
//		register=(Button)this.findViewById(R.id.register);
//
//		getpassword=loginpassword.getText().toString();
//	}
//
//
//
//
//	@Override
//	protected void initView() {
//
//		//显示密码的togglebutton点击事件,动态显示隐藏密码--->点击前先判定
////		isShowPassword.setOnClickListener(new OnClickListener() {
////
////			@Override
////			public void onClick(View v) {
////
////				if(getpassword.equals("")||getpassword.length()<=0){
////					DisPlay("密码不能为空");
////				}
////
////				if(!isDisplayflag){
////					//隐藏密码
////					//loginpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
////					//loginpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
////					loginpassword.setInputType(0x90);
////
////				}else{
////					//loginpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
////					//loginpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
////					loginpassword.setInputType(0x81);
////				}
////				//isDisplayflag=!isDisplayflag;
////				loginpassword.postInvalidate();
////			}
////		});
//
//
//		register.setOnClickListener(this);
//
//		isShowPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//				Log.i(Tag, "开关按钮状态="+isChecked);
//
////				if(getpassword.equals("")||getpassword.length()<=0){
////					DisPlay("密码不能为空");
////				}
//
//
//				if(isChecked){
//					//隐藏
//					loginpassword.setInputType(0x90);
//					//loginpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
//				}else{
//					//明文显示
//					loginpassword.setInputType(0x81);
//					//loginpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
//				}
//				Log.i("togglebutton", ""+isChecked);
//				//loginpassword.postInvalidate();
//			}
//		});
//
//
//		loginBtn.setOnClickListener(this);
//
//	}
//
//	@Override
//	public void onClick(View v) {
//	switch (v.getId()) {
//	case R.id.register:
//		mIntent=new Intent(LoginActivity.this, RegisterActivity.class);
//		startActivity(mIntent);
//
//
//		break;
//
//
//	case R.id.login:
//
////		doLogin();
//		userlogin();
//
//		break;
//
//	default:
//		break;
//	}
//
//	}
//
//	//之前的方式太繁瑣了
//	private void userlogin() {
//		 username=loginaccount.getText().toString().trim();
//		 password=loginpassword.getText().toString().trim();
//		String serverAdd = serverAddress;
//
//		if(username.equals("")){
//			DisplayToast("用户名不能为空!");
//		}
//		if(password.equals("")){
//			DisplayToast("密码不能为空!");
//		}
//
//		if(username.equals("test")&&password.equals("123")){
//			DisplayToast("登錄成功!");
//			Intent data=new Intent();
//            data.putExtra("name", username);
////            data.putExtra("values", 100);
//            //请求代码可以自己设置，这里设置成20
//            setResult(20, data);
//
//			LoginActivity.this.finish();
//		}
//
////		new LoginTask().execute(username, password);
//
//	}
//
//	//登录系统
//	private void doLogin(){
//
//		final String uaername=loginaccount.getText().toString().trim();
//		final String password=loginpassword.getText().toString().trim();
//		String serverAdd = serverAddress;
//
//		if(uaername.equals("")){
//			DisplayToast("用户名不能为空!");
//		}
//		if(password.equals("")){
//			DisplayToast("密码不能为空!");
//		}
//
//		loginActivity.doAsync(new Callable<Boolean>() {
//
//			@Override
//			public Boolean call() throws Exception {
//
//				String clientVersion = getVersionName();
//				String deviceid = getDeviceId();
//				String token = getToken();
//				String clientOs = getClientOs();
//				String clientOsVer = getClientOsVer();
//				String language = getLanguage();
//				String country = getCountry();
//
//				Constants.clientVersion = clientVersion;
//				Constants.deviceid = deviceid;
//				Constants.token = token;
//				Constants.clientOs = clientOs;
//				Constants.clientOsVer = clientOsVer;
//				Constants.language = language;
//				Constants.country = country;
//				Constants.user = uaername;
//				Constants.pass = password;
//
//				return true;
//			}
//
//		}, new Callback<Boolean>() {
//
//			@Override
//			public void onCallback(Boolean pCallbackValue) {
//				// TODO Auto-generated method stub
//
//			}
//		}, new Callback<Exception>() {
//
//			@Override
//			public void onCallback(Exception pCallbackValue) {
//				// TODO Auto-generated method stub
//
//			}
//		}, true, getResources().getString(R.string.login_loading));
//
//	}
//
//	class LoginTask extends AsyncTask<String, Void, JSONObject>{
//
//
//		@Override
//		protected JSONObject doInBackground(String... params) {
//			HashMap<String, String> map=new HashMap<String, String>();
//
//			map.put("name", username);
//			map.put("pass", password);
//			map.put("server", serverAddress);
//
//
//			try {
//				return new JSONObject(new String("{a:1,b:2}"));
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return null;
//		}
//	}
//
//}
