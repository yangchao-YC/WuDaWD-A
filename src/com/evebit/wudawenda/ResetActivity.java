package com.evebit.wudawenda;

/**
 * @author guan
 * 重置密码
 * 1.输入用户名，密码，再次输入密码，邮箱，若用户名和邮箱正确则向服务器重置密码
 * 2.重置完密码后可返回登录页面去登录
 */
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.evebit.json.DataManeger;
import com.evebit.json.Normal;
import com.evebit.json.Test_Bean_TianQi;
import com.evebit.json.Test_Model_TianQi;
import com.evebit.json.Y_Exception;
import com.umeng.analytics.MobclickAgent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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

public class ResetActivity extends Activity {	
	
	private Button resetButton; //重置密码按钮
	private TextView nameTextView, pwdTextView,repwdTextView,emailTextView;//用户名,密码,邮箱
	private ImageView sign_exit_ImageView; //返回首页按钮	
	private String fraction = null;//接收分数
	private String sign_type = null; //是从哪个页面跳转的,1代表积分页面跳转，2代表直接登录，3代表积分商城
	Typeface typeface;
	private ProgressDialog progressDialog;
	//输入姓名转码utf-8
	private String name;//用来存储转换成utf-8的姓名信息
	private Toast toast = null;
	//忘记密码
	private String urlRestPwd = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&registered=modify";
    private String reset_info_wrong="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_pwd);
		
		typeface = Typeface.createFromAsset(getAssets(), "hkhbt.ttf");//设置自定义字体	
		
		nameTextView = (TextView)findViewById(R.id.reset_name);
        pwdTextView = (TextView)findViewById(R.id.reset_pwd);
        repwdTextView = (TextView)findViewById(R.id.reset_re_pwd);
		emailTextView = (TextView)findViewById(R.id.reset_email);
		resetButton = (Button)findViewById(R.id.reset_button);
		sign_exit_ImageView = (ImageView)findViewById(R.id.reset_exit);
		
		nameTextView.setTypeface(typeface);
		pwdTextView.setTypeface(typeface);
		repwdTextView.setTypeface(typeface);
		emailTextView.setTypeface(typeface);
		resetButton.setTypeface(typeface);
		
		//是从哪个页面跳转的,1代表积分页面跳转，2代表直接登录，3代表积分商城
		sign_type =  getIntent().getExtras().getString("sign_type");
				
		if (sign_type.equals("1")) {
			fraction = getIntent().getExtras().getString("fraction");//接收分数
		}
		
		//重置密码按钮
		resetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkInput();
			}			
		});
		
		//返回至首页
		 sign_exit_ImageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub				
					Intent intent = new Intent(ResetActivity.this, LoginActivity.class);
					intent.putExtra("fraction", String.valueOf(fraction));
					intent.putExtra("sign_type", sign_type);
					startActivity(intent);
					finish();
				}
			});	
	}

	/**
	 * 检验输入信息是否正确
	 */
	private void checkInput() {
		// TODO Auto-generated method stub
	    	Normal normal = new Normal(this);// 判断是否有网络连接	    		    	
		  if (nameTextView.getText().toString().equals("")||pwdTextView.getText().toString().equals("")||repwdTextView.getText().toString().equals("")||emailTextView.getText().toString().equals("")) {
			  //如果输入信息为空
			  showTextToast(getString(R.string.redeem_info_null));
		  } else if(!pwdTextView.getText().toString().equals(repwdTextView.getText().toString())){
			//两次密码输入不一致	
			pwdTextView.setText("");
			repwdTextView.setText("");
			showTextToast(getString(R.string.regist_pwd_wrong));
		  }else if(!normal.note_Intent(getApplicationContext())){
			//没有连网
			 showTextToast(getString(R.string.intenet_no)); 
		}else  {
			//检查输入信息和数据库是否对应
			try {
				 name = URLEncoder.encode(nameTextView.getText().toString(),"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			urlRestPwd = urlRestPwd + "&username="+ name +"&password=" + pwdTextView.getText().toString() +"&email="+emailTextView.getText().toString();
			checkInformation();
		}	
	}
	
	/**
	 * 检测用户输入用户名和邮箱信息是否正确
	 */
	private void checkInformation() {
		// TODO Auto-generated method stub
		progressDialog = ProgressDialog.show(ResetActivity.this, "", getResources().getString(R.string.reset_loading), true, false);

		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					data = DataManeger.getTestData_TianQi(urlRestPwd);
					Test_Model_TianQi  test_Model_User = data.getData();					
					if (test_Model_User.getErrorid().equals("1")) {				
						//输入信息正确，重置密码，之后去登录界面
							goSignPage();						
					}else{
						//如果输入用户名和邮箱信息不对，提示用户
						handler.sendEmptyMessage(1);
						reset_info_wrong = test_Model_User.getError();
					}												
				} catch (Y_Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}				
		}.start();
	}
	
	/**
	 * 通过handler告知用户输入信息是否正确
	 */
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				showTextToast(reset_info_wrong);
				nameTextView.setText("");
				emailTextView.setText("");
				progressDialog.dismiss();
				break;	
			default:
				break;
			}
		}		
	};
	
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
	 * 重置密码成功以后去登录界面
	 */
	private void goSignPage() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(ResetActivity.this, SignActivity.class);
		intent.putExtra("fraction", String.valueOf(fraction));
		intent.putExtra("sign_type", sign_type);
     	startActivity(intent);
     	progressDialog.dismiss();
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		return super.onCreateOptionsMenu(menu);
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
}
