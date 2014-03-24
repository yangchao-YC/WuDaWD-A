package com.evebit.wudawenda;

/**
 * @author guan
 * ��������
 * 1.�����û��������룬�ٴ��������룬���䣬���û�����������ȷ�����������������
 * 2.�����������ɷ��ص�¼ҳ��ȥ��¼
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
	
	private Button resetButton; //�������밴ť
	private TextView nameTextView, pwdTextView,repwdTextView,emailTextView;//�û���,����,����
	private ImageView sign_exit_ImageView; //������ҳ��ť	
	private String fraction = null;//���շ���
	private String sign_type = null; //�Ǵ��ĸ�ҳ����ת��,1�������ҳ����ת��2����ֱ�ӵ�¼��3��������̳�
	Typeface typeface;
	private ProgressDialog progressDialog;
	//��������ת��utf-8
	private String name;//�����洢ת����utf-8��������Ϣ
	private Toast toast = null;
	//��������
	private String urlRestPwd = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&registered=modify";
    private String reset_info_wrong="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reset_pwd);
		
		typeface = Typeface.createFromAsset(getAssets(), "hkhbt.ttf");//�����Զ�������	
		
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
		
		//�Ǵ��ĸ�ҳ����ת��,1�������ҳ����ת��2����ֱ�ӵ�¼��3��������̳�
		sign_type =  getIntent().getExtras().getString("sign_type");
				
		if (sign_type.equals("1")) {
			fraction = getIntent().getExtras().getString("fraction");//���շ���
		}
		
		//�������밴ť
		resetButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkInput();
			}			
		});
		
		//��������ҳ
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
	 * ����������Ϣ�Ƿ���ȷ
	 */
	private void checkInput() {
		// TODO Auto-generated method stub
	    	Normal normal = new Normal(this);// �ж��Ƿ�����������	    		    	
		  if (nameTextView.getText().toString().equals("")||pwdTextView.getText().toString().equals("")||repwdTextView.getText().toString().equals("")||emailTextView.getText().toString().equals("")) {
			  //���������ϢΪ��
			  showTextToast(getString(R.string.redeem_info_null));
		  } else if(!pwdTextView.getText().toString().equals(repwdTextView.getText().toString())){
			//�����������벻һ��	
			pwdTextView.setText("");
			repwdTextView.setText("");
			showTextToast(getString(R.string.regist_pwd_wrong));
		  }else if(!normal.note_Intent(getApplicationContext())){
			//û������
			 showTextToast(getString(R.string.intenet_no)); 
		}else  {
			//���������Ϣ�����ݿ��Ƿ��Ӧ
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
	 * ����û������û�����������Ϣ�Ƿ���ȷ
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
						//������Ϣ��ȷ���������룬֮��ȥ��¼����
							goSignPage();						
					}else{
						//��������û�����������Ϣ���ԣ���ʾ�û�
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
	 * ͨ��handler��֪�û�������Ϣ�Ƿ���ȷ
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
	 * ��������ɹ��Ժ�ȥ��¼����
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
