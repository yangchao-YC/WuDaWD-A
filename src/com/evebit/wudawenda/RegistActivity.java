package com.evebit.wudawenda;
/**
 * @author guan
 * 注册界面
 * 1. 输入用户名，密码，邮箱。注册
 * 2. 注册之后返回之前跳转来的页面
 */
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.evebit.json.DataManeger;
import com.evebit.json.Normal;
import com.evebit.json.Test_Bean_TianQi;
import com.evebit.json.Test_Model_TianQi;
import com.evebit.json.Y_Exception;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegistActivity extends Activity implements OnClickListener {

	private Button registButton;//注册的按钮
    private TextView nameTextView, pwdTextView , re_pwd_TextView ,type_emailTextView ,emailTextView ;
    private ImageView regist_exit_ImageView; //返回首页
    Typeface typeface;
    private ProgressDialog progressDialog;
    private String urlRegist = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&registered=on"; //注册新用户
    //private String urlCheckUser;//检测用户名是否已经被注册
    private String sign_type = null; //是从哪个页面跳转的,1代表积分页面跳转，2代表直接登录，3代表积分商城
    private String fraction = null;//接收分数
    private Toast toast = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
          // TODO Auto-generated method stub
             super.onCreate(savedInstanceState);
             setContentView( R.layout. activity_regist);
         
		     typeface = Typeface. createFromAsset(getAssets(), "hkhbt.ttf" );//设置自定义字体
		     //是从哪个页面跳转的,1代表积分页面跳转，2代表直接登录，3代表积分商城
		     sign_type =  getIntent().getExtras().getString("sign_type");
		     if (sign_type.equals("1")) {
					fraction = getIntent().getExtras().getString("fraction");//接收分数
			 }
		     
		     registButton = (Button)findViewById( R.id.Regist_button);		         
		     nameTextView = (TextView)findViewById( R.id.Regist_edit_name);
		     pwdTextView = (TextView)findViewById( R.id.Regist_edit_pwd);
		     re_pwd_TextView = (TextView)findViewById(R.id.Regist_edit_repwd);
		     type_emailTextView = (TextView)findViewById(R.id.Regist_type_email);
		     emailTextView = (TextView)findViewById( R.id.Regist_edit_email);
		     regist_exit_ImageView = (ImageView)findViewById(R.id.regist_exit);
		         
		     nameTextView.setTypeface( typeface);
		     pwdTextView.setTypeface( typeface);
		     re_pwd_TextView.setTypeface( typeface);
		     type_emailTextView.setTypeface( typeface);
		     emailTextView.setTypeface( typeface);
		     registButton.setTypeface( typeface);
		   
		     registButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					checkRegistInfo();
				}
		
			});
		     
		     regist_exit_ImageView. setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					handler.sendEmptyMessage(1);
				}
			});
		     
		     
   }
    
 
   
    /**
     * 跳转到不同页面， 1代表首页，2代表积分页面，3代表积分商城页面
     */
    private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Intent intent;
			switch (msg.what) {
			case 1:
				//返回首页		
				intent = new Intent(RegistActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
				break;
			case 2:
				intent = new Intent(RegistActivity.this, IntegralActivity.class);
				intent.putExtra("fraction", fraction);
				startActivity(intent);
	         	showTextToast(getString(R.string.regist_success));
	         	progressDialog.dismiss();
				finish();
				break;
			case 3:
				intent = new Intent(RegistActivity.this, StoreActivity.class);
				startActivity(intent);
				showTextToast(getString(R.string.regist_success));
				progressDialog.dismiss();
				finish();
				break;
			case 4:
				 nameTextView.setText("");
				 showTextToast(getString(R.string.regist_name_have));				 
				 break;
			case 5:
				//返回首页		
				intent = new Intent(RegistActivity.this, LoginActivity.class);
				startActivity(intent);
				showTextToast(getString(R.string.regist_success));
				progressDialog.dismiss();
				finish();
				break;
			default:
				break;
			}
		}		
	};
   

    @Override
    protected void onPause() {
          // TODO Auto-generated method stub
          super.onPause();
         
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
   }
    
    /**
     * 检测输入信息是否正确
     */
    private void checkRegistInfo() {
		// TODO Auto-generated method stub
        Normal normal = new Normal(this);// 判断是否有网络连接
    	String username = nameTextView.getText().toString();
    	
    	if (!normal.note_Intent(getApplicationContext())) {
    		showTextToast(getString(R.string.intenet_no));
		}else if(username.equals("")||pwdTextView.getText().toString().equals("")||re_pwd_TextView.getText().toString().equals("")||emailTextView.getText().toString().equals("")){
			//输入信息为空
    		showTextToast(getString(R.string.redeem_info_null));
		}else if (!pwdTextView.getText().toString().equals(re_pwd_TextView.getText().toString())) {
			//两次密码输入不一致
			pwdTextView.setText("");
			re_pwd_TextView.setText("");
			showTextToast(getString(R.string.regist_pwd_wrong));
    	}else if(!(emailTextView.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && emailTextView.getText().toString().length() > 0)){
			//Toast.makeText(getApplicationContext(), getResources().getString(R.string.intenet_no), Toast.LENGTH_SHORT).show();
    		showTextToast(getString(R.string.regist_email_wrong));
    	}else{
			//注册
			
			try {
				username = URLEncoder.encode(username,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			urlRegist = urlRegist + "&username="+username+"&password="+pwdTextView.getText().toString()+"&email="+emailTextView.getText().toString();
			checkUserRegist();
		}   	
	}


	/**
     * 检测用户名是否已经被注册
     */
	private void checkUserRegist() {
		// TODO Auto-generated method stub
		progressDialog = ProgressDialog.show(RegistActivity.this, "", getResources().getString(R.string.regist_loading), true, false);

		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					//上传用户名，返回是否已经有用户注册
					data = DataManeger.getTestData_TianQi(urlRegist);
					Test_Model_TianQi  test_Model_User = data.getData();	
					//errorid 为1 用户名已存在，为2 注册成功
					 if (test_Model_User.getErrorid().equals("2")) {
						 //用户名已存在
						 handler.sendEmptyMessage(4);
						
					} else if(test_Model_User.getErrorid().equals("1")){				
						//注册成功 标记为登录
					   
						 registFinish();
						 
					}			
									
				} catch (Y_Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
	
		}.start();
	}

	
	/**
	 * 注册成功，标记为已登录
	 */
	private void registFinish() {
		// TODO Auto-generated method stub
		  SharedPreferences settings = this.getSharedPreferences("CheckLoginXML", 0);
		  SharedPreferences.Editor localEditor = settings.edit();
		  localEditor.putString("CheckLogin",nameTextView.getText().toString());
		  localEditor.commit(); 
		 	
		  if (sign_type.equals("1")) {
				 handler.sendEmptyMessage(2);
			}else if (sign_type.equals("2")){
				handler.sendEmptyMessage(5);
			}else if (sign_type.equals("3")){
				handler.sendEmptyMessage(3);
			}
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


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}


}
