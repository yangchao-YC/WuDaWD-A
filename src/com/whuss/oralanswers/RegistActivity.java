package com.whuss.oralanswers;
/**
 * @author guan
 * ע�����
 * 1. �����û��������룬���䡣ע��
 * 2. ע��֮�󷵻�֮ǰ��ת����ҳ��
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

	private Button registButton,regisButton2;//ע��İ�ť
    private TextView nameTextView, pwdTextView , re_pwd_TextView ,type_emailTextView ,emailTextView,workTextView ;
    private ImageView regist_exit_ImageView; //������ҳ
    Typeface typeface;
    private LinearLayout layout1,layout2;
    private ProgressDialog progressDialog;
    private String urlRegist = LoginActivity.URL + "index.php?option=com_content&registered=on"; //ע�����û�
    private String urlSex = LoginActivity.URL + "index.php?option=com_content&ttrue=4&sex=";
    //private String urlCheckUser;//����û����Ƿ��Ѿ���ע��
    private String sign_type = null; //�Ǵ��ĸ�ҳ����ת��,1�������ҳ����ת��2����ֱ�ӵ�¼��3��������̳�
    private String fraction = null;//���շ���
    private Toast toast = null;
    private EditText sexEdit,ageEdit;
    
    private Spinner spinner = null;
    String spinnerString = "��ѡ��";
    private static final String [] langurage ={"Сѧ��","������","��ѧ��","�о���","����ҵ","��ҵ","����Ա/��ҵ��λ","ҽ������","����","����"};
    private ArrayAdapter<String> adapter = null;
    
    
    private Spinner sexSpinner = null;
    String sexSpinnerString = "��ѡ��";
    private static final String [] sexlangurage ={"��","Ů"};
    private ArrayAdapter<String> sexadapter = null;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
          // TODO Auto-generated method stub
             super.onCreate(savedInstanceState);
             setContentView( R.layout.activity_regist);
         
		     typeface = Typeface. createFromAsset(getAssets(), "hkhbt.ttf" );//�����Զ�������
		     //�Ǵ��ĸ�ҳ����ת��,1�������ҳ����ת��2����ֱ�ӵ�¼��3��������̳�
		     sign_type =  getIntent().getExtras().getString("sign_type");
		     if (sign_type.equals("1")) {
					fraction = getIntent().getExtras().getString("fraction");//���շ���
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
				//���������б���  
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
				//����������ӵ�spinner��ȥ  
				spinner.setAdapter(adapter);  
				spinner.setVisibility(View.VISIBLE);//����Ĭ����ʾ




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
					//���������б���  
				 sexadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
					//����������ӵ�spinner��ȥ  
				 sexSpinner.setAdapter(sexadapter);  
				 sexSpinner.setVisibility(View.VISIBLE);//����Ĭ����ʾ




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
    	
			if (sexSpinnerString.equals("��")) {
				sex = "1";
				if (age.length()!=0) {
					if (spinnerString.equals("��ѡ��")) {
						Toast.makeText(getApplicationContext(), "��ѡ��ְҵ", Toast.LENGTH_SHORT).show();
					}
					else {
						messageUrlRegist(age,sex);
					}
				}
				else {
					Toast.makeText(getApplicationContext(), "����������", Toast.LENGTH_SHORT).show();
				}
			}
			else if (sexSpinnerString.equals("Ů")) {
				sex = "2";
				if (age.length()!=0) {
					if (spinnerString.equals("��ѡ��")) {
						Toast.makeText(getApplicationContext(), "��ѡ��ְҵ", Toast.LENGTH_SHORT).show();
					}
					else {
						messageUrlRegist(age,sex);
					}
				}
				else {
					Toast.makeText(getApplicationContext(), "����������", Toast.LENGTH_SHORT).show();
				}
			}
			else {
				Toast.makeText(getApplicationContext(), "��ѡ���Ա��л�Ů", Toast.LENGTH_SHORT).show();
			}
		

    }
    
    
    
    /**
     * ע����Ϣ
     */
	private void messageUrlRegist(final String age,final String sex) {
		// TODO Auto-generated method stub

		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					//�ϴ��û����������Ƿ��Ѿ����û�ע��
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
     * ��ת����ͬҳ�棬 1������ҳ��2�������ҳ�棬3��������̳�ҳ��
     */
    private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Intent intent;
			switch (msg.what) {
			case 1:
				//������ҳ		
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
				//������ҳ		
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
     * ���������Ϣ�Ƿ���ȷ
     */
    private void checkRegistInfo() {
		// TODO Auto-generated method stub
        Normal normal = new Normal(this);// �ж��Ƿ�����������
    	String username = nameTextView.getText().toString();
    	
    	if (!normal.note_Intent(getApplicationContext())) {
    		showTextToast(getString(R.string.intenet_no));
		}else if(username.equals("")||pwdTextView.getText().toString().equals("")||re_pwd_TextView.getText().toString().equals("")||emailTextView.getText().toString().equals("")){
			//������ϢΪ��
    		showTextToast(getString(R.string.redeem_info_null));
		}else if (!pwdTextView.getText().toString().equals(re_pwd_TextView.getText().toString())) {
			//�����������벻һ��
			pwdTextView.setText("");
			re_pwd_TextView.setText("");
			showTextToast(getString(R.string.regist_pwd_wrong));
    	}else if(!(emailTextView.getText().toString().matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9]+\\.+[a-z]+") && emailTextView.getText().toString().length() > 0)){
			//Toast.makeText(getApplicationContext(), getResources().getString(R.string.intenet_no), Toast.LENGTH_SHORT).show();
    		showTextToast(getString(R.string.regist_email_wrong));
    	}else{
			//ע��
			
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
     * ����û����Ƿ��Ѿ���ע��
     */
	private void checkUserRegist() {
		// TODO Auto-generated method stub
		progressDialog = ProgressDialog.show(RegistActivity.this, "", getResources().getString(R.string.regist_loading), true, false);

		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					System.out
							.println("RegistActivity.checkUserRegist()    ע���ַ:"+urlRegist);
					data = DataManeger.getTestData_TianQi(urlRegist+"&lang=en");
					Test_Model_TianQi  test_Model_User = data.getData();	
					 if (test_Model_User.getErrorid().equals("2")) {
						 handler.sendEmptyMessage(4);
						
					} else if(test_Model_User.getErrorid().equals("1")){				
						//ע��ɹ� ���Ϊ��¼
					   
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
	 * ע��ɹ������Ϊ�ѵ�¼
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
	 * toast��ʾ��Ϣ����
	 * @param msg ������Ϣ
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
