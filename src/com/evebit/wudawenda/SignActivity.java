package com.evebit.wudawenda;

/**
 * ��ʼҳ��

 * @author guanliping
 *SignActivity   ��¼ҳ��
 *���ܶ�
 *1. ��¼���ܣ������û��������롣��¼�ɹ��󷵻ص�ת��֮ǰ��ҳ��
 *2. ������ת��ע��ҳ�棬��������ҳ��
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
	
	private Button signButton,registButton; //��¼��ť��ע�ᰴť
	private TextView nameTextView, pwdTextView;//�û���������
	private TextView forget_pwd_TextView; //��������
	private ImageView sign_exit_ImageView; //������ҳ��ť
	private ProgressDialog progressDialog;
	
	private String fraction = null;//���շ���
	private String sign_type = null; //�Ǵ��ĸ�ҳ����ת��,1�������ҳ����ת��2����ֱ�ӵ�¼��3��������̳�
	Typeface typeface;
	private Toast toast = null;
	
	//��֤�û���¼�Ƿ�ɹ��������û��������룬�õ���¼������Ϣ�����ж�
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
		
		typeface = Typeface.createFromAsset(getAssets(), "hkhbt.ttf");//�����Զ�������		
		//�Ǵ��ĸ�ҳ����ת��,1�������ҳ����ת��2����ֱ�ӵ�¼��3��������̳�
		sign_type =  getIntent().getExtras().getString("sign_type");
		
		if (sign_type.equals("1")) {
			fraction = getIntent().getExtras().getString("fraction");//���շ���
		}
		
		
	
		signButton = (Button)findViewById(R.id.Sign_button_login);
		registButton = (Button)findViewById(R.id.Sign_button_regist);
		sign_exit_ImageView = (ImageView)findViewById(R.id.sign_exit);
		
        nameTextView = (TextView)findViewById(R.id.Sign_edit_name);
        pwdTextView = (TextView)findViewById(R.id.Sign_edit_pwd);
        forget_pwd_TextView = (TextView)findViewById(R.id.Sign_forget_psd);
        forget_pwd_TextView.getPaint().setFlags(Paint. UNDERLINE_TEXT_FLAG );//�ײ��Ӻ���
        
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
				//�������룬��ת���һ��������
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
	 * ���η��ذ�ť
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
		Normal normal = new Normal(this);// �ж��Ƿ�����������

		switch (v.getId()) {
		case R.id.Sign_button_login: //��¼��ť
			if(nameTextView.getText().toString().equals("")||pwdTextView.getText().toString().equals("")){
				 showTextToast(getString(R.string.redeem_info_null));	    
			}else if(!normal.note_Intent(getApplicationContext())){
				 showTextToast(getString(R.string.intenet_no));
			}else{
				SignAccount();
			}
			
			break;
		case R.id.Sign_button_regist: //ע���û�
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
	 * �û���¼
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
	 * �жϴ��������������Ƿ��¼�ɹ�
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
							//����ҳ��������½�ɹ�
							handler.sendEmptyMessage(1);	
						}else if(sign_type.equals("2")){
							//��ҳ������½�ɹ�
							handler.sendEmptyMessage(2);
						}else if (sign_type.equals("3")) {
							//�����̳�������¼�ɹ�
							handler.sendEmptyMessage(3);
						}	
						return;
					}else{
						//��¼��Ϣ����
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
	
	/**
	 * ��½�ɹ����ı��¼״̬
	 */
	private void changeLoginStatus(String username) {
		// TODO Auto-generated method stub
		SharedPreferences settings = this.getSharedPreferences("CheckLoginXML", 0);
		SharedPreferences.Editor localEditor = settings.edit();
		localEditor.putString("CheckLogin",username);
		localEditor.commit();    
	}		
	
	/**
	 * �����жϽ�����ز�ͬ�Ľ���
	   sign_type������ĸ�ҳ����ת��1�������ҳ����ת��2����ֱ�ӵ�¼
	 */
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			Intent intent;
			switch (msg.what) {
			case 1:
				//�ӻ�������¼����¼�ɹ������ػ���ҳ��		
				intent = new Intent(SignActivity.this, IntegralActivity.class);
				intent.putExtra("fraction", String.valueOf(fraction));
				intent.putExtra("sign_type", sign_type);
				startActivity(intent);
				showTextToast(getString(R.string.sign_sucess));
				progressDialog.dismiss();
				finish();
				break;
			case 2:
				//����ҳ����¼����½�ɹ���������ҳ
				intent = new Intent(SignActivity.this, LoginActivity.class);
				startActivity(intent);
				showTextToast(getString(R.string.sign_sucess));
				progressDialog.dismiss();
				finish();
				break;
			case 3:
				//�����̳�������½�ɹ������ػ����̳�
				intent = new Intent(SignActivity.this, StoreActivity.class);
				intent.putExtra("redeem_type", "3");
				startActivity(intent);
				showTextToast(getString(R.string.sign_sucess));
				progressDialog.dismiss();
				finish();
				break;
			case 4:
				//��¼ʧ��
				nameTextView.setText("");
				pwdTextView.setText("");
				showTextToast(getString(R.string.sign_wrong));
				progressDialog.dismiss();
				break;
			case 5:
				//������ҳ
				intent = new Intent(SignActivity.this, LoginActivity.class);
				startActivity(intent);				
				finish();			
			default:
				break;
			}
		}		
	};

}
