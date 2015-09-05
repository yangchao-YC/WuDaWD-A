package com.whuss.oralanswers;
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
import com.whuss.oralanswers.R;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class RegistActivity extends Activity implements OnClickListener {

	private Button registButton,regisButton2;//注册的按钮
    private TextView nameTextView, pwdTextView , re_pwd_TextView ,type_emailTextView ,emailTextView,workTextView ;
    private ImageView regist_exit_ImageView; //返回首页
    Typeface typeface;
    private LinearLayout layout1,layout2;
    private ProgressDialog progressDialog;
    private String urlRegist = LoginActivity.URL + "index.php?option=com_content&registered=on"; //注册新用户
    private String urlSex = LoginActivity.URL + "index.php?option=com_content&ttrue=4&sex=";
    //private String urlCheckUser;//检测用户名是否已经被注册
    private String sign_type = null; //是从哪个页面跳转的,1代表积分页面跳转，2代表直接登录，3代表积分商城
    private String fraction = null;//接收分数
    private Toast toast = null;
    private EditText sexEdit,ageEdit;
    
    private Spinner spinner = null;
    String spinnerString = "请选择";
    private static final String [] langurage ={"小学生","初中生","大学生","研究生","服务业","企业","公务员/事业单位","医疗卫生","教育","其他"};
    private ArrayAdapter<String> adapter = null;
    
    
    private Spinner sexSpinner = null;
    String sexSpinnerString = "请选择";
    private static final String [] sexlangurage ={"男","女"};
    private ArrayAdapter<String> sexadapter = null;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
          // TODO Auto-generated method stub
             super.onCreate(savedInstanceState);
             setContentView( R.layout.activity_regist);
         
		     typeface = Typeface. createFromAsset(getAssets(), "hkhbt.ttf" );//设置自定义字体
		     //是从哪个页面跳转的,1代表积分页面跳转，2代表直接登录，3代表积分商城
		     sign_type =  getIntent().getExtras().getString("sign_type");
		     if (sign_type.equals("1")) {
					fraction = getIntent().getExtras().getString("fraction");//接收分数
			 }
		     
		     registButton = (Button)findViewById( R.id.Regist_button);	
		     regisButton2 = (Button)findViewById(R.id.Regist_button2);
		     nameTextView = (TextView)findViewById( R.id.Regist_edit_name);
		     pwdTextView = (TextView)findViewById( R.id.Regist_edit_pwd);
		     re_pwd_TextView = (TextView)findViewById(R.id.Regist_edit_repwd);
		     type_emailTextView = (TextView)findViewById(R.id.Regist_type_email);
		     emailTextView = (TextView)findViewById( R.id.Regist_edit_email);
		     regist_exit_ImageView = (ImageView)findViewById(R.id.regist_exit);
		     layout1 = (LinearLayout)findViewById(R.id.regist_Layout1);    
		     layout2 = (LinearLayout)findViewById(R.id.regist_Layout2);  
		     workTextView = (TextView)findViewById(R.id.Regist_TextView_word);
		     
		     ageEdit = (EditText)findViewById(R.id.Regist_edit_age);
		     sexEdit = (EditText)findViewById(R.id.Regist_edit_sex);
		     spinner = (Spinner)findViewById(R.id.Regist_Spinner_word);
		     sexSpinner = (Spinner)findViewById(R.id.Regist_Spinner_sex);
		     workTextView = (TextView)findViewById(R.id.Regist_TextView_word);
		     
		     nameTextView.setTypeface( typeface);
		     pwdTextView.setTypeface( typeface);
		     re_pwd_TextView.setTypeface( typeface);
		     type_emailTextView.setTypeface( typeface);
		     emailTextView.setTypeface( typeface);
		     registButton.setTypeface( typeface);
		     regisButton2.setTypeface( typeface);
		     ageEdit.setTypeface(typeface);
		     sexEdit.setTypeface(typeface);
		     workTextView.setTypeface(typeface);
		     
		     registButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					checkRegistInfo();
				}
		
			});
		     
		     regisButton2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						messageRegist();
					}
			
				});
		     
		     
		     regist_exit_ImageView. setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					handler.sendEmptyMessage(1);
				}
			});
		     
		     
		     adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,langurage);  
				//设置下拉列表风格  
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
				//将适配器添加到spinner中去  
				spinner.setAdapter(adapter);  
				spinner.setVisibility(View.VISIBLE);//设置默认显示




				spinner.setOnItemSelectedListener(new OnItemSelectedListener() {  
					@Override  
					public void onItemSelected(AdapterView<?> arg0, View arg1,  
							int arg2, long arg3) {  
						// TODO Auto-generated method stub  

						spinnerString = langurage[arg2];

					}  
					@Override  
					public void onNothingSelected(AdapterView<?> arg0) {  
						// TODO Auto-generated method stub  

					}  
				});
		     
				
				 sexadapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sexlangurage);  
					//设置下拉列表风格  
				 sexadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
					//将适配器添加到spinner中去  
				 sexSpinner.setAdapter(sexadapter);  
				 sexSpinner.setVisibility(View.VISIBLE);//设置默认显示




				 sexSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {  
						@Override  
						public void onItemSelected(AdapterView<?> arg0, View arg1,  
								int arg2, long arg3) {  
							// TODO Auto-generated method stub  

							sexSpinnerString = sexlangurage[arg2];

						}  
						@Override  
						public void onNothingSelected(AdapterView<?> arg0) {  
							// TODO Auto-generated method stub  

						}  
					});
				
		     
   }
    
 
    private void messageRegist() {
    	String age = ageEdit.getText().toString();
    	String sex = sexEdit.getText().toString();
    	
			if (sexSpinnerString.equals("男")) {
				sex = "1";
				if (age.length()!=0) {
					if (spinnerString.equals("请选择")) {
						Toast.makeText(getApplicationContext(), "请选择职业", Toast.LENGTH_SHORT).show();
					}
					else {
						messageUrlRegist(age,sex);
					}
				}
				else {
					Toast.makeText(getApplicationContext(), "请输入年龄", Toast.LENGTH_SHORT).show();
				}
			}
			else if (sexSpinnerString.equals("女")) {
				sex = "2";
				if (age.length()!=0) {
					if (spinnerString.equals("请选择")) {
						Toast.makeText(getApplicationContext(), "请选择职业", Toast.LENGTH_SHORT).show();
					}
					else {
						messageUrlRegist(age,sex);
					}
				}
				else {
					Toast.makeText(getApplicationContext(), "请输入年龄", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(getApplicationContext(), "请选择性别：男或女", Toast.LENGTH_SHORT).show();
			}
		

    }
    
    
    
    /**
     * 注册信息
     */
	private void messageUrlRegist(final String age,final String sex) {
		// TODO Auto-generated method stub

		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					//上传用户名，返回是否已经有用户注册
					String url = urlSex + sex + "&old="+age+"&position="+spinnerString +"&lang=en";
					data = DataManeger.getTestData_TianQi(url);
			
					handler2.sendEmptyMessage(1);		
									
				} catch (Y_Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
	
		}.start();
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
	         	
				finish();
				break;
			case 3:
				intent = new Intent(RegistActivity.this, StoreActivity.class);
				startActivity(intent);
				showTextToast(getString(R.string.regist_success));
				
				finish();
				break;
			case 4:
				 nameTextView.setText("");
				 showTextToast(getString(R.string.regist_name_have));	
				 progressDialog.dismiss();
				 break;
			case 5:
				//返回首页		
				intent = new Intent(RegistActivity.this, LoginActivity.class);
				startActivity(intent);
				showTextToast(getString(R.string.regist_success));
				
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
    	}else if(!(emailTextView.getText().toString().matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.+[a-z]+") && emailTextView.getText().toString().length() > 0)){
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
					System.out
							.println("RegistActivity.checkUserRegist()    注册地址:"+urlRegist);
					data = DataManeger.getTestData_TianQi(urlRegist+"&lang=en");
					Test_Model_TianQi  test_Model_User = data.getData();	
					 if (test_Model_User.getErrorid().equals("2")) {
						 handler.sendEmptyMessage(4);
						
					} else if(test_Model_User.getErrorid().equals("1")){				
						//注册成功 标记为登录
					   
						 //registFinish();
						 handler2.sendEmptyMessage(0);
					}			
									
				} catch (Y_Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
	
		}.start();
	}

	
	private Handler handler2 = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				layout1.setVisibility(View.GONE);
				layout2.setVisibility(View.VISIBLE);
				progressDialog.dismiss();
				break;
			case 1:
				registFinish();
				break;
			default:
				break;
			}
			
		}
		
	};
	
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
