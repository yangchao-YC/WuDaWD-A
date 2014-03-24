package com.evebit.wudawenda;


import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

/**
 * 初始页面
 * @author yangchao
 *AnswerActivity   问答页面
 *IntegralActivity 积分页面
 *RegistActivity   注册页面
 *ResetActivity    重置页面
 *SignActivity     登录页面
 *StoreActivity    积分商城页面
 */


public class LoginActivity extends Activity implements android.view.View.OnClickListener{

	//三个按钮，integralButton去积分商城页面，answerButton去答题页面，siteButton去登录界面
	private Button integralButton,answerButton,siteButton; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateAutoPopup(true);
		UmengUpdateAgent.setUpdateOnlyWifi(false);	
		MobclickAgent.setDebugMode( true );
		
		integralButton = (Button)findViewById(R.id.Login_Button_Integral);
		answerButton = (Button)findViewById(R.id.Login_Button_Answer);
		siteButton = (Button)findViewById(R.id.Login_Button_Site);
				
		Typeface typeface = Typeface.createFromAsset(getAssets(), "hkhbt.ttf");
		answerButton.setTypeface(typeface);
		integralButton.setTypeface(typeface);
		siteButton.setTypeface(typeface);
		
		integralButton.setOnClickListener(this);
		answerButton.setOnClickListener(this);
		siteButton.setOnClickListener(this);			
	}
	
	 /** 
	  * 开始清除SharedPreferences中保存的内容
	 **/	
	private void clear() {	     	
		SharedPreferences settings = this.getSharedPreferences("CheckLoginXML", 0);
		SharedPreferences.Editor localEditor = settings.edit();
		localEditor.remove("CheckLogin");
		localEditor.commit();   
	   }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/**
	 * 屏蔽菜单键
	 */
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.Login_Button_Answer://进入答题页面
			intent = new Intent(LoginActivity.this, AnswerActivity.class);
			startActivity(intent);		
			break;
		case R.id.Login_Button_Integral://进入积分商城
			intent = new Intent(LoginActivity.this, StoreActivity.class);
			intent.putExtra("redeem_type", "2");//传递跳转类型, 1代表积分页面跳转，2代表直接去积分商城
			startActivity(intent);
			break;
		case R.id.Login_Button_Site://进入登录页面--
			intent = new Intent(LoginActivity.this, SignActivity.class);
			intent.putExtra("sign_type", "2");//传递跳转类型, 1代表积分页面跳转，2代表直接登录
			startActivity(intent);
			break;			
		default:
			break;
		}
	}

	
	
   /**
    * 点击返回键，清除数据，退出程序
    */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		 if (event.getKeyCode() == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN&& event.getRepeatCount() == 0) {            
	            //具体的操作代码
	            new AlertDialog.Builder(this).setTitle("确定退出程序么").setNegativeButton("取消", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                    }
	        
	            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {

	            public void onClick(DialogInterface dialog, int whichButton) {
	                clear();	
	                finish();
	                Intent startMain = new Intent(Intent.ACTION_MAIN);   
	                startMain.addCategory(Intent.CATEGORY_HOME);   
	                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
	                startActivity(startMain);   
	                System.exit(0); 
	            }
	           }).show();
	        return true;
	        }
	        return super.dispatchKeyEvent(event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	

}
