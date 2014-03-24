package com.evebit.wudawenda;
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

	private Button registButton;//ע��İ�ť
    private TextView nameTextView, pwdTextView , re_pwd_TextView ,type_emailTextView ,emailTextView ;
    private ImageView regist_exit_ImageView; //������ҳ
    Typeface typeface;
    private ProgressDialog progressDialog;
    private String urlRegist = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&registered=on"; //ע�����û�
    //private String urlCheckUser;//����û����Ƿ��Ѿ���ע��
    private String sign_type = null; //�Ǵ��ĸ�ҳ����ת��,1�������ҳ����ת��2����ֱ�ӵ�¼��3��������̳�
    private String fraction = null;//���շ���
    private Toast toast = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
          // TODO Auto-generated method stub
             super.onCreate(savedInstanceState);
             setContentView( R.layout. activity_regist);
         
		     typeface = Typeface. createFromAsset(getAssets(), "hkhbt.ttf" );//�����Զ�������
		     //�Ǵ��ĸ�ҳ����ת��,1�������ҳ����ת��2����ֱ�ӵ�¼��3��������̳�
		     sign_type =  getIntent().getExtras().getString("sign_type");
		     if (sign_type.equals("1")) {
					fraction = getIntent().getExtras().getString("fraction");//���շ���
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
				//������ҳ		
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
    	}else if(!(emailTextView.getText().toString().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+") && emailTextView.getText().toString().length() > 0)){
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
					//�ϴ��û����������Ƿ��Ѿ����û�ע��
					data = DataManeger.getTestData_TianQi(urlRegist);
					Test_Model_TianQi  test_Model_User = data.getData();	
					//errorid Ϊ1 �û����Ѵ��ڣ�Ϊ2 ע��ɹ�
					 if (test_Model_User.getErrorid().equals("2")) {
						 //�û����Ѵ���
						 handler.sendEmptyMessage(4);
						
					} else if(test_Model_User.getErrorid().equals("1")){				
						//ע��ɹ� ���Ϊ��¼
					   
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
