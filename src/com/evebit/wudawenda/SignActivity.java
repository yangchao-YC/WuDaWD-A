package com.evebit.wudawenda;

/**
 * 初始页面

 * @author guanliping
 *SignActivity   登录页面
 *功能二
 *1. 登录功能，输入用户名，密码。登录成功后返回倒转来之前的页面
 *2. 可以跳转到注册页面，忘记密码页面
 */

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.evebit.json.DataManeger;
import com.evebit.json.Normal;
import com.evebit.json.Test_Bean_TianQi;
import com.evebit.json.Test_Model_TianQi;
import com.evebit.json.Y_Exception;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SignActivity extends Activity implements android.view.View.OnClickListener {
	
	private Button signButton,registButton; //登录按钮，注册按钮
	private TextView nameTextView, pwdTextView;//用户名，密码
	private TextView forget_pwd_TextView; //忘记密码
	private ImageView sign_exit_ImageView; //返回首页按钮
	private ProgressDialog progressDialog;
	
	private String fraction = null;//接收分数
	private String sign_type = null; //是从哪个页面跳转的,1代表积分页面跳转，2代表直接登录，3代表积分商城
	Typeface typeface;
	private Toast toast = null;
	
	//验证用户登录是否成功，传递用户名和密码，得到登录反馈信息用于判断
	private String urlLoginCheck = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&login=on";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign);
		

		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		
		MobclickAgent.setDebugMode( true );
		
		typeface = Typeface.createFromAsset(getAssets(), "hkhbt.ttf");//设置自定义字体		
		//是从哪个页面跳转的,1代表积分页面跳转，2代表直接登录，3代表积分商城
		sign_type =  getIntent().getExtras().getString("sign_type");
		
		if (sign_type.equals("1")) {
			fraction = getIntent().getExtras().getString("fraction");//接收分数
		}
		
		
	
		signButton = (Button)findViewById(R.id.Sign_button_login);
		registButton = (Button)findViewById(R.id.Sign_button_regist);
		sign_exit_ImageView = (ImageView)findViewById(R.id.sign_exit);
		
        nameTextView = (TextView)findViewById(R.id.Sign_edit_name);
        pwdTextView = (TextView)findViewById(R.id.Sign_edit_pwd);
        forget_pwd_TextView = (TextView)findViewById(R.id.Sign_forget_psd);
        forget_pwd_TextView.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );//底部加横线
        
        signButton.setTypeface(typeface);
        registButton.setTypeface(typeface);
        nameTextView.setTypeface(typeface);
        pwdTextView.setTypeface(typeface);
        forget_pwd_TextView.setTypeface(typeface);
        
        signButton.setOnClickListener(this);
        registButton.setOnClickListener(this);
        
    
        forget_pwd_TextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//忘记密码，跳转到找回密码界面
				Intent intent = new Intent(SignActivity.this, ResetActivity.class);
				intent.putExtra("fraction", String.valueOf(fraction));
				intent.putExtra("sign_type", sign_type);
				startActivity(intent);
				finish();
			}
		});
        
        sign_exit_ImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(5);
			}
		});
		
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	/**
	 * 屏蔽返回按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
	    return false;		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		Normal normal = new Normal(this);// 判断是否有网络连接

		switch (v.getId()) {
		case R.id.Sign_button_login: //登录按钮
			if(nameTextView.getText().toString().equals("")||pwdTextView.getText().toString().equals("")){
				 showTextToast(getString(R.string.redeem_info_null));	    
			}else if(!normal.note_Intent(getApplicationContext())){
				 showTextToast(getString(R.string.intenet_no));
			}else{
				SignAccount();
			}
			
			break;
		case R.id.Sign_button_regist: //注册用户
			intent = new Intent(SignActivity.this, RegistActivity.class);
			intent.putExtra("sign_type", sign_type);
			if (sign_type.equals("1")) {
				intent.putExtra("fraction", fraction);
			}
			startActivity(intent);
			finish();
			break;				
		default:
			break;
		}

	}
	
	/**
	 * 用户登录
	 * @throws UnsupportedEncodingException 
	 */
	private void SignAccount()  {
		// TODO Auto-generated method stub
		String username = nameTextView.getText().toString();
		String password = pwdTextView.getText().toString();	
	
		try {
			username = URLEncoder.encode(username,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    urlLoginCheck = urlLoginCheck + "&username=" + username + "&password="+password;
	
		loginThread();
	}
	
	/**
	 * 判断从来个界面来，是否登录成功
	 */
	private void loginThread() {
		// TODO Auto-generated method stub
		progressDialog = ProgressDialog.show(SignActivity.this, "", getResources().getString(R.string.sign_loading), true, false);

		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					data = DataManeger.getTestData_TianQi(urlLoginCheck);
					Test_Model_TianQi  test_Model_User = data.getData();	
					
					if (test_Model_User.getErrorid().equals("1")) {				
						changeLoginStatus(nameTextView.getText().toString());
						if (sign_type.equals("1")) {
							//积分页面来，登陆成功
							handler.sendEmptyMessage(1);	
						}else if(sign_type.equals("2")){
							//首页来，登陆成功
							handler.sendEmptyMessage(2);
						}else if (sign_type.equals("3")) {
							//积分商城来，登录成功
							handler.sendEmptyMessage(3);
						}	
						return;
					}else{
						//登录信息错误
						handler.sendEmptyMessage(4);
					}							
									
				} catch (Y_Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
	
		}.start();
	}
	
	/**
	 * toast提示信息功能
	 * @param msg 传递信息
	 */
	private void showTextToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
	
	/**
	 * 登陆成功，改变登录状态
	 */
	private void changeLoginStatus(String username) {
		// TODO Auto-generated method stub
		SharedPreferences settings = this.getSharedPreferences("CheckLoginXML", 0);
		SharedPreferences.Editor localEditor = settings.edit();
		localEditor.putString("CheckLogin",username);
		localEditor.commit();    
	}		
	
	/**
	 * 根据判断结果返回不同的界面
	   sign_type代表从哪个页面跳转，1代表积分页面跳转，2代表直接登录
	 */
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Intent intent;
			switch (msg.what) {
			case 1:
				//从积分来登录，登录成功，返回积分页面		
				intent = new Intent(SignActivity.this, IntegralActivity.class);
				intent.putExtra("fraction", String.valueOf(fraction));
				intent.putExtra("sign_type", sign_type);
				startActivity(intent);
				showTextToast(getString(R.string.sign_sucess));
				progressDialog.dismiss();
				finish();
				break;
			case 2:
				//从首页来登录，登陆成功，返回首页
				intent = new Intent(SignActivity.this, LoginActivity.class);
				startActivity(intent);
				showTextToast(getString(R.string.sign_sucess));
				progressDialog.dismiss();
				finish();
				break;
			case 3:
				//积分商城来，登陆成功，返回积分商城
				intent = new Intent(SignActivity.this, StoreActivity.class);
				intent.putExtra("redeem_type", "3");
				startActivity(intent);
				showTextToast(getString(R.string.sign_sucess));
				progressDialog.dismiss();
				finish();
				break;
			case 4:
				//登录失败
				nameTextView.setText("");
				pwdTextView.setText("");
				showTextToast(getString(R.string.sign_wrong));
				progressDialog.dismiss();
				break;
			case 5:
				//返回首页
				intent = new Intent(SignActivity.this, LoginActivity.class);
				startActivity(intent);				
				finish();			
			default:
				break;
			}
		}		
	};

}
