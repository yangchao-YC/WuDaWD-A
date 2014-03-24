package com.evebit.wudawenda;

/**
 * �����̳�ҳ��

 * @author guanliping
 *StoreActivity   �����̳�ҳ��
 *����
 *1. ��ʾ��Ʒ�б�����ѡ�񷵻���ҳ
 *2. ����б��ڵ���Ʒ������һ��������������������ĵ��򣬷����㹻�����һ�ҳ��
 *3. �һ�ҳ�棬��ʾ׼���һ�����Ʒ���Լ��������û���Ϣ���ֻ��ţ���������ַ
 */
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import com.evebit.adapter.ListViewAdapter;
import com.evebit.json.Y_Exception;
import com.evebit.ui.MyDialog;
import com.evebit.ui.RedeemDialog;
import com.umeng.analytics.MobclickAgent;

import com.evebit.json.DataManeger;
import com.evebit.json.Test_Bean;
import com.evebit.json.Test_Bean_TianQi;
import com.evebit.json.Test_Model;
import com.evebit.json.Test_Model_TianQi;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


public class StoreActivity extends Activity implements android.view.View.OnClickListener {

	private int fraction = 0;//��¼����
	private TextView textView1;//��Ŀǰ���û���
	private TextView textView2;//��Ŀǰ���û��� �������
	private ListView listView; //��Ʒ�б�
	private Button button; //���ذ�ť
	
	
	MyDialog dialog; // �����Ƿ�����һ� ����
	RedeemDialog redeemDialog; // �����㹻�һ����Ƿ�ȷ���һ� ����
	RedeemDialog redeemSucessDialog;//���ֳɹ��ĵ���
	Typeface typeface;
	
	private String totalFraction = "0";//���շ���
	private String redeem_type = null;//�Ǵ��ĸ�ҳ����ת��,1�������ҳ����ת��2����ֱ�ӵ�¼
	private String user_name_string; //�û���
	
	private String selfScore ="0";
	
	//��Ʒ�б��url
	String urlStoreList = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&list=on";
	
	//�Լ��Ļ�������url
	String urlScoreSelf = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&rankings=on";
	
	//��Ʒ���б�
	private ArrayList<HashMap<String, Object>> list= new ArrayList<HashMap<String,Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
			
		/**
		 * �󶨿ؼ�
		 */
		textView1 = (TextView)findViewById(R.id.Store_view_getscore1); 
		textView2 = (TextView)findViewById(R.id.Store_view_getscore2);	
		listView= (ListView)findViewById(R.id.Store_view_list);
		button=(Button)findViewById(R.id.Store_button_back);
	
		
		/**
		 * �ؼ������趨���ʼֵ�趨
		 */
		button.setOnClickListener(this);	
		typeface = Typeface.createFromAsset(getAssets(), "hkhbt.ttf");
		textView1.setTypeface(typeface);
		textView2.setTypeface(typeface);
		button.setTypeface(typeface);
		
		//����û��Ƿ��¼
		SharedPreferences settings = this.getSharedPreferences("CheckLoginXML", 0);
		user_name_string = settings.getString("CheckLogin", "");
		
		if (user_name_string.equals("")) {
			dialog();
			textView1.setText(R.string.store_not_login);
			textView2.setVisibility(View.GONE);
			dataThread();
		}else{
			textView1.setText(R.string.store_score);
			textView2.setVisibility(View.VISIBLE);
			//redeem_type =  getIntent().getExtras().getString("redeem_type");
			//�õ��û��Ļ���
			
			try {
				user_name_string = URLEncoder.encode(user_name_string,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			getUserScore(user_name_string);
		}
	
	
	}
	

	/**
	 * �ϴ��û������õ��û��Ļ���
	 * @param username �û���
	 */
	private void getUserScore(final String username) {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					//�õ��Լ��Ļ���
					urlScoreSelf = urlScoreSelf + "&username="+username;
					data = DataManeger.getTestData_TianQi(urlScoreSelf);
					Test_Model_TianQi  test_Model_User = data.getData();
					selfScore = test_Model_User.getIntegral();				
				    handler.sendEmptyMessage(1);
				} catch (Y_Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		
		}.start();				
	}

    
	
	/**
	 * δ��¼����ʾ�û���¼
	 */
	private void dialog() {
		// TODO Auto-generated method stub
		dialog = new MyDialog(StoreActivity.this);
		
		 TextView  title =(TextView)dialog.getTitle();
		 TextView  content =(TextView)dialog.getConten();
		 Button ok = (Button)dialog.getOK();
		 Button no = (Button)dialog.getNo();
		 
		 title.setPadding(0, 30, 0, 0);
		 title.setText(getResources().getString(R.string.login_not));
		 content.setVisibility(View.GONE);
	     no.setText(getResources().getString(R.string.login));
	     ok.setText(getResources().getString(R.string.cancle));
	     
	     dialog.setOnOkListenenr(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					
				}
			});
			dialog.setOnNoListenenr(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent;
					intent = new Intent(StoreActivity.this, SignActivity.class);
					intent.putExtra("sign_type", "3");//������ת����, 1�������ҳ����ת��2����ֱ�ӵ�¼��3����ӻ����̳���ת
					startActivity(intent);
					finish();
				}
			});
			dialog.show();
	}

	/**
	 * ͨ��handler��֪��Ʒ�б���Ϣ�Ƿ��ѻ�ȡ
	 */
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				//��Ʒ�б���Ϣ�ѻ�ȡ
				ListView();
				break;
			case 1:
				//����UI����ʾ�Լ����ܻ���
				textView2.setText(selfScore);
				dataThread();
				break;
			default:
				break;
			}
		}

		
		
	};
	
	/**
	 * �Ѵ�json�л�ȡ�����ݴ���listview��
	 */
	private void ListView() {
		// TODO Auto-generated method stub    
		
		ListViewAdapter listViewAdapter = new ListViewAdapter(this, list, selfScore,user_name_string);    	    
		listView.setAdapter(listViewAdapter);		
	}
	
	/**
	 * ���jason�ж�ȡ����Ʒ�б���Ϣ
	 */
	private void dataThread() {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				Test_Bean data;
				try {
					data = DataManeger.getTestData(urlStoreList);
					ArrayList<Test_Model> datalist = data.getData();
					for (Test_Model test_Model : datalist) {	
						 HashMap<String, Object> map=new HashMap<String, Object>();
						 map.put("image", (test_Model.getImgUrl()==null? "": test_Model.getImgUrl()));
					     map.put("title", (test_Model.getTitle()==null? "": test_Model.getTitle()));
						 map.put("score", (test_Model.getIntegral()==null? "": test_Model.getIntegral()));
						 map.put("introtext", (test_Model.getIntrotext()==null? "": test_Model.getIntrotext()));
						 map.put("id", (test_Model.getId()==null? "": test_Model.getId()));
						 list.add(map);
					}										
					handler.sendEmptyMessage(0);					
				} catch (Y_Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}			
		}.start();
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	
	
	/**
	 * ���η��ذ�ť
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
           return false;
	    
	}
	
	private void pushIntegarl()
	{
		Intent intent =new Intent(StoreActivity.this, IntegralActivity.class);
		intent.putExtra("fraction", String.valueOf(fraction));//���ݷ���
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
			
		// TODO Auto-generated method stub
		switch (v.getId()) {
		    case R.id.Store_button_back:
			backFirstPage(); //��� ���ذ�ť ���ص���ҳ
			break;

		default:
			break;
		}
	}
	
	public void setSelfScore(String score){
		textView2.setText(score);
	}
	
	/**
	 * ��� ���ذ�ť ���ص���ҳ
	 */
	private void backFirstPage() {
		// TODO Auto-generated method stub
		Intent intent =new Intent(StoreActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

}


