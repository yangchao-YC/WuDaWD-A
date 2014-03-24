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
 * ��ʼҳ��
 * @author yangchao
 *AnswerActivity   �ʴ�ҳ��
 *IntegralActivity ����ҳ��
 *RegistActivity   ע��ҳ��
 *ResetActivity    ����ҳ��
 *SignActivity     ��¼ҳ��
 *StoreActivity    �����̳�ҳ��
 */


public class LoginActivity extends Activity implements android.view.View.OnClickListener{

	//������ť��integralButtonȥ�����̳�ҳ�棬answerButtonȥ����ҳ�棬siteButtonȥ��¼����
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
	  * ��ʼ���SharedPreferences�б��������
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
	 * ���β˵���
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
		case R.id.Login_Button_Answer://�������ҳ��
			intent = new Intent(LoginActivity.this, AnswerActivity.class);
			startActivity(intent);		
			break;
		case R.id.Login_Button_Integral://��������̳�
			intent = new Intent(LoginActivity.this, StoreActivity.class);
			intent.putExtra("redeem_type", "2");//������ת����, 1�������ҳ����ת��2����ֱ��ȥ�����̳�
			startActivity(intent);
			break;
		case R.id.Login_Button_Site://�����¼ҳ��--
			intent = new Intent(LoginActivity.this, SignActivity.class);
			intent.putExtra("sign_type", "2");//������ת����, 1�������ҳ����ת��2����ֱ�ӵ�¼
			startActivity(intent);
			break;			
		default:
			break;
		}
	}

	
	
   /**
    * ������ؼ���������ݣ��˳�����
    */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub
		 if (event.getKeyCode() == KeyEvent.KEYCODE_BACK&& event.getAction() == KeyEvent.ACTION_DOWN&& event.getRepeatCount() == 0) {            
	            //����Ĳ�������
	            new AlertDialog.Builder(this).setTitle("ȷ���˳�����ô").setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                    }
	        
	            }).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

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
