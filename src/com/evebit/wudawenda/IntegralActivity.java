package com.evebit.wudawenda;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import com.evebit.adapter.IntegralPagerAdapter;
import com.evebit.adapter.RankViewAdapter;
import com.evebit.json.DataManeger;
import com.evebit.json.Test_Bean;
import com.evebit.json.Test_Bean_TianQi;
import com.evebit.json.Test_Model;
import com.evebit.json.Test_Model_TianQi;
import com.evebit.json.Y_Exception;
import com.evebit.ui.MyDialog;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.UMSsoHandler;
import com.umeng.socialize.controller.UMWXHandler;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 积分页面
 * 设计逻辑
 * 1. 答题页面结束后 跳转到积分页面
 * 2. 积分页面包含两个页面，显示积分页面和积分排行页面
 * 3. 包含三个按钮，积分分享，再答一次，去积分兑换页面
 */


/**
 * 积分页面
 * @author yangchao,guanliping
 *
 */
public class IntegralActivity extends Activity implements android.view.View.OnClickListener{

	private String fraction = null;//接收分数
	private TextView fractionTextView;//分数text
	private Button shareButton,redeemButton,againButton; 
	private TextView rank_TextView,rank_temple,rank_data,rank_temple2,rank_fighting;//显示排名里的信息
	private ListView listView;
	private LinearLayout integral_rank_you; //显示第几名，登录显示排名和未登录不显示信息
	private ImageView left_dot, right_dot; //滑动页面的下面的两个点
	
	Typeface typeface;
	MyDialog dialog;
	private String user_name_string; //用户名
	private String user_name; //用户名用户来转码
	
	private ViewPager viewPager; //viewpager
	private ArrayList<View> pageViews; //存放页卡的组合
	private ViewGroup integralGroup; // 存放页卡的组合对应的总页面
	private View integral_View_Score, integral_View_Rank; //需要滑动的页卡,显示分数，显示排名
	
	//上传自己的积分
	private String urlUploadScore = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&postintegral=on"; 
	//积分排行，前7名数据
	private String urlRankTotal = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&getIntegralList=on"; 
	//自己的排名
	private String urlRankSelf = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&rankings=on"; 
	
	Bitmap bitmap;//分享时屏幕截图
	private String selfRank;

	//分享功能
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
	
	private ArrayList<HashMap<String, Object>> rank_list = new ArrayList<HashMap<String,Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * 设置分页滑动
		 */		
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		integral_View_Score = inflater.inflate(R.layout.activity_integral_score, null);
		integral_View_Rank = inflater.inflate(R.layout.activity_integral_rank, null);
		pageViews.add(integral_View_Score);
		pageViews.add(integral_View_Rank);
		integralGroup = (ViewGroup)inflater.inflate(R.layout.activity_integral, null);	
		viewPager = (ViewPager)integralGroup.findViewById(R.id.activity_integral_container);
		 		
		IntegralPagerAdapter adapter = new IntegralPagerAdapter(pageViews);
		viewPager.setAdapter(adapter); 
	    viewPager.setOnPageChangeListener(new IntegralPagerChangeListener());
		
		setContentView(integralGroup);
		typeface = Typeface.createFromAsset(getAssets(), "hkhbt.ttf");//设置字体
		
		/**
		 * 绑定控件，积分页面
		 */
		fraction = getIntent().getExtras().getString("fraction");//接收分数
		fractionTextView = (TextView)integral_View_Score.findViewById(R.id.Integral_Text_Fraction);
		shareButton = (Button)findViewById(R.id.Integral_Button_share);
		redeemButton = (Button)findViewById(R.id.Integral_Button_back);
		againButton = (Button)findViewById(R.id.Integral_Button_again);
		left_dot = (ImageView)findViewById(R.id.Integral_dot_left);
		right_dot = (ImageView)findViewById(R.id.Integral_dot_right);
		
		/**
		 * 设置控件字体,积分页面
		 */
		fractionTextView.setTypeface(typeface);
		shareButton.setTypeface(typeface);
		redeemButton.setTypeface(typeface);
		againButton.setTypeface(typeface);
		
		/**
		 * 绑定控件，排行页面
		 */
		
		integral_rank_you = (LinearLayout)integral_View_Rank.findViewById(R.id.integral_rank_you);
		rank_TextView = (TextView)integral_View_Rank.findViewById(R.id.integral_rank);
		rank_temple = (TextView)integral_View_Rank.findViewById(R.id.integral_rank_temple);
		rank_fighting = (TextView)integral_View_Rank.findViewById(R.id.integral_rank_fighting);
		listView = (ListView)integral_View_Rank.findViewById(R.id.integral_rank_list);
		
		
		/**
		 * 设置控件字体,积分页面
		 */
		rank_TextView.setTypeface(typeface);
		rank_temple.setTypeface(typeface);
		rank_fighting.setTypeface(typeface);
		
		/**
		 * 设置控件
		 */
		fractionTextView.setText(fraction+getResources().getString(R.string.integral_fraction));
		
		/**
		 * 微信分享
		 
        mController.getConfig().supportQQPlatform(IntegralActivity.this, "http://www.umeng.com/social");  		
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appID = "wx3371792d0858282a";
		// 微信图文分享必须设置一个url 
		String contentUrl = "http://www.umeng.com/social";
		// 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
		UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(IntegralActivity.this,appID, contentUrl);
		//设置分享标题
		wxHandler.setWXTitle("哎呀口腔问答");
		// 支持微信朋友圈
		UMWXHandler circleHandler = mController.getConfig().supportWXCirclePlatform(IntegralActivity.this,appID, contentUrl) ;
		circleHandler.setCircleTitle("哎呀口腔问答");
		
		//qq空间
		mController.getConfig().setSsoHandler(new QZoneSsoHandler(IntegralActivity.this));
		//新浪微博
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		//设置腾讯微博SSO handler
		 * 
		 */
		
		 mController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN,SHARE_MEDIA.RENREN,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.SINA);
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		
		
		shareButton.setOnClickListener(this);
		redeemButton.setOnClickListener(this);
		againButton.setOnClickListener(this);	
		
		//检测用户是否登录
		SharedPreferences settings = this.getSharedPreferences("CheckLoginXML", 0);
		user_name_string = settings.getString("CheckLogin", "");	
		checkUserlogin(user_name_string);
		
	}
	

	/**
	 * sso授权
	 */
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		 /**使用SSO授权必须添加如下代码 */
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }	
	}


	/**
     * 检测用户
     * @param user_name_string shareperfence 中存储的用户名的值
     */
	private void checkUserlogin(String user_name_string) {
		// TODO Auto-generated method stub
		if (user_name_string.equals("")) {	
			rank_temple.setText(R.string.login_not);
			rank_fighting.setVisibility(View.GONE);
			//用户没有登录
			dialog();		
		}else {			
			try {
				user_name = URLEncoder.encode(user_name_string,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//如果登录了，上传用户名，积分
			urlUploadScore = urlUploadScore + "&username=" + user_name + "&integral="+fraction;
			//自己的排名，上传用户名
			urlRankSelf = urlRankSelf + "&username=" + user_name;
			rank_fighting.setVisibility(View.VISIBLE);
			
			//若果为0分
			if(fraction.equals("0")){
				handler.sendEmptyMessage(2);
			}else{
				UploadScoreThread();
			}	
						
		}
			
	}



	class IntegralPagerChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			
		    if (arg0 == 1) {
				
		    	left_dot.setImageResource(R.drawable.circle_green);
		    	right_dot.setImageResource(R.drawable.circle_white);
		    	
			} else if(arg0 == 0){
				
				left_dot.setImageResource(R.drawable.circle_white);
		    	right_dot.setImageResource(R.drawable.circle_green);
			}
			
		}
		
		
	}
	
	/**
	 * 得到前七名数据
	 */
	private void getRankTotal() {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				Test_Bean data;
				try {
					//拉取排名以及前7名的名字和积分
					data = DataManeger.getTestData(urlRankTotal);
					ArrayList<Test_Model> datalist = data.getData();
					for (Test_Model test_Model : datalist) {	
					  				
						 HashMap<String, Object> map=new HashMap<String, Object>();
						 map.put("username", (test_Model.getUsername()==null? "": test_Model.getUsername()));
					     map.put("integral", (test_Model.getIntegral()==null? "": test_Model.getIntegral()));
					     if (test_Model.getUsername().equals(user_name_string)) {
					    	 map.put("state", true);	
						} else {
							 map.put("state", false);	
						}				    		   
					     rank_list.add(map);				  
					}	
					
					handler.sendEmptyMessage(1);	
				} catch (Y_Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	
	/**
	 * 通过handler告知用户排名列表信息是否已获取
	 */
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				//用户排名列表
				RankListView();
				break;
			case 2:
				//上传积分以后得到自己的名次，显示新的排名积分信息
				//拉取前7名数据
				getRankTotal();	
				//得到自己的名次
				getRankSelf();																
			case 3:
				//设置分享内容
				shareResult();
				break;
			case 4:
				//更新UI，显示排名信息
				rank_temple.setText(getResources().getString(R.string.integral_rank_temple)+selfRank+getResources().getString(R.string.integral_rank_temple2));
				break;
			default:
				break;
			}
		}
		
	};
	
	/**
	 * 积分排名列表信息显示
	 */
	private void RankListView() {
		// TODO Auto-generated method stub
		
		if (rank_list.get(0).get("username").equals(user_name_string)) {
			rank_fighting.setText(R.string.integral_rank_first);
		}else {
			rank_fighting.setText(R.string.integral_rank_fighting);
		}
			
		RankViewAdapter rankViewAdapter = new RankViewAdapter(this, rank_list);		
		listView.setAdapter(rankViewAdapter);	
	}	
	
	/**
	 * 分享此页面
	 */
	private void shareResult() {
		// TODO Auto-generated method stub
		mController.setShareContent("哎呀口腔问答采用问答的游戏方式，配以幽默诙谐的背景和卡通元素，以通俗易懂的问答形式向广大用户推介了口腔各种常见病的医疗保健知识。");
		//设置分享图片, 参数2为图片的url地址
		mController.setShareMedia(new UMImage(IntegralActivity.this, bitmap));	
	}
	
	/**
	 * 上传积分
	 */
	private void UploadScoreThread() {
		// TODO Auto-generated method stub	
		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					
					//上传积分，返回积分是否成功，成功之后拉取排名以及前7名
						data = DataManeger.getTestData_TianQi(urlUploadScore);
						Test_Model_TianQi  test_Model_User = data.getData();	
						if (test_Model_User.getErrorid().equals("1")) {									
							handler.sendEmptyMessage(2);
						}	
					
						
				} catch (Y_Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	/**
	 * 得到自己的排名
	 */
	private void getRankSelf() {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					//得到自己的排名
					data = DataManeger.getTestData_TianQi(urlRankSelf);
					Test_Model_TianQi  test_Model_User = data.getData();
					selfRank = test_Model_User.getRankings();
					handler.sendEmptyMessage(4);					
						
				} catch (Y_Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			
		}.start();
	}
	


	/**
	 * 提示用户未登录对话框
	 */
	private void dialog(){	
		 dialog = new MyDialog(IntegralActivity.this);
		
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
					//拉取前七名数据		
					getRankTotal();
				}
			});
			dialog.setOnNoListenenr(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent;
					intent = new Intent(IntegralActivity.this, SignActivity.class);
					intent.putExtra("fraction", String.valueOf(fraction));//传递分数
					intent.putExtra("sign_type", "1");//传递跳转类型, 1代表积分页面跳转，2代表直接登录
					startActivity(intent);
					finish();
				}
			});
			dialog.show();
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

	


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}

	/**
	 * 屏蔽返回按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		 return false;
           	    
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		//分享
		case R.id.Integral_Button_share:		 
			  task(IntegralActivity.this);
			  mController.openShare(IntegralActivity.this, false);
			break;
		//再来一次
		case R.id.Integral_Button_again:		
			intent = new Intent(IntegralActivity.this, AnswerActivity.class);
			startActivity(intent);
			finish();
			break;
		//回到首页
		case R.id.Integral_Button_back:
			intent = new Intent(IntegralActivity.this, LoginActivity.class);
			startActivity(intent);			
			finish();
		    break;
		default:
			break;
		}
	}
	
	/**
	 * 截取当前页面的截图
	 * @param activity
	 */
	@SuppressWarnings("deprecation")
	private void task (Activity activity)
	{		
		
		View view = activity.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		Bitmap b1 = view.getDrawingCache();
		
		Rect frame= new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int sheight = frame.top;	
		int width = activity.getWindowManager().getDefaultDisplay().getWidth();
		int heigth = activity.getWindowManager().getDefaultDisplay().getHeight();
		
	    bitmap = Bitmap.createBitmap(b1,0,sheight,width,heigth - sheight);		
		view.destroyDrawingCache();	
		//分享功能
		handler.sendEmptyMessage(3);
	}
    

}
