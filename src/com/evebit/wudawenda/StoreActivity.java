package com.evebit.wudawenda;

/**
 * 积分商城页面

 * @author guanliping
 *StoreActivity   积分商城页面
 *功能
 *1. 显示商品列表，可以选择返回首页
 *2. 点击列表内的商品，点击兑换，分数不够弹出不够的弹框，分数足够弹出兑换页面
 *3. 兑换页面，显示准备兑换的商品，以及可填入用户信息：手机号，姓名，地址
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

	private int fraction = 0;//记录分数
	private TextView textView1;//您目前所得积分
	private TextView textView2;//您目前所得积分 具体分数
	private ListView listView; //商品列表
	private Button button; //返回按钮
	
	
	MyDialog dialog; // 积分是否满足兑换 弹窗
	RedeemDialog redeemDialog; // 积分足够兑换，是否确定兑换 弹窗
	RedeemDialog redeemSucessDialog;//积分成功的弹窗
	Typeface typeface;
	
	private String totalFraction = "0";//接收分数
	private String redeem_type = null;//是从哪个页面跳转的,1代表积分页面跳转，2代表直接登录
	private String user_name_string; //用户名
	
	private String selfScore ="0";
	
	//商品列表的url
	String urlStoreList = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&list=on";
	
	//自己的积分排名url
	String urlScoreSelf = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&rankings=on";
	
	//商品的列表
	private ArrayList<HashMap<String, Object>> list= new ArrayList<HashMap<String,Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store);
			
		/**
		 * 绑定控件
		 */
		textView1 = (TextView)findViewById(R.id.Store_view_getscore1); 
		textView2 = (TextView)findViewById(R.id.Store_view_getscore2);	
		listView= (ListView)findViewById(R.id.Store_view_list);
		button=(Button)findViewById(R.id.Store_button_back);
	
		
		/**
		 * 控件属性设定与初始值设定
		 */
		button.setOnClickListener(this);	
		typeface = Typeface.createFromAsset(getAssets(), "hkhbt.ttf");
		textView1.setTypeface(typeface);
		textView2.setTypeface(typeface);
		button.setTypeface(typeface);
		
		//检测用户是否登录
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
			//得到用户的积分
			
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
	 * 上传用户名，得到用户的积分
	 * @param username 用户名
	 */
	private void getUserScore(final String username) {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					//得到自己的积分
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
	 * 未登录，提示用户登录
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
					intent.putExtra("sign_type", "3");//传递跳转类型, 1代表积分页面跳转，2代表直接登录，3代表从积分商城跳转
					startActivity(intent);
					finish();
				}
			});
			dialog.show();
	}

	/**
	 * 通过handler告知商品列表信息是否已获取
	 */
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				//商品列表信息已获取
				ListView();
				break;
			case 1:
				//更新UI，显示自己的总积分
				textView2.setText(selfScore);
				dataThread();
				break;
			default:
				break;
			}
		}

		
		
	};
	
	/**
	 * 把从json中获取的数据存入listview中
	 */
	private void ListView() {
		// TODO Auto-generated method stub    
		
		ListViewAdapter listViewAdapter = new ListViewAdapter(this, list, selfScore,user_name_string);    	    
		listView.setAdapter(listViewAdapter);		
	}
	
	/**
	 * 添加jason中读取的商品列表信息
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
	 * 屏蔽菜单键
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
	 * 屏蔽返回按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
           return false;
	    
	}
	
	private void pushIntegarl()
	{
		Intent intent =new Intent(StoreActivity.this, IntegralActivity.class);
		intent.putExtra("fraction", String.valueOf(fraction));//传递分数
		startActivity(intent);
		finish();
	}

	@Override
	public void onClick(View v) {
			
		// TODO Auto-generated method stub
		switch (v.getId()) {
		    case R.id.Store_button_back:
			backFirstPage(); //点击 返回按钮 返回到首页
			break;

		default:
			break;
		}
	}
	
	public void setSelfScore(String score){
		textView2.setText(score);
	}
	
	/**
	 * 点击 返回按钮 返回到首页
	 */
	private void backFirstPage() {
		// TODO Auto-generated method stub
		Intent intent =new Intent(StoreActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

}


