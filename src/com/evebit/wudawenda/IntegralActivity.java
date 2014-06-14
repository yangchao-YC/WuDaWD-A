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
 * ����ҳ��
 * ����߼�
 * 1. ����ҳ������� ��ת������ҳ��
 * 2. ����ҳ���������ҳ�棬��ʾ����ҳ��ͻ�������ҳ��
 * 3. ����������ť�����ַ����ٴ�һ�Σ�ȥ���ֶһ�ҳ��
 */


/**
 * ����ҳ��
 * @author yangchao,guanliping
 *
 */
public class IntegralActivity extends Activity implements android.view.View.OnClickListener{

	private String fraction = null;//���շ���
	private TextView fractionTextView;//����text
	private Button shareButton,redeemButton,againButton; 
	private TextView rank_TextView,rank_temple,rank_data,rank_temple2,rank_fighting;//��ʾ���������Ϣ
	private ListView listView;
	private LinearLayout integral_rank_you; //��ʾ�ڼ�������¼��ʾ������δ��¼����ʾ��Ϣ
	private ImageView left_dot, right_dot; //����ҳ��������������
	
	Typeface typeface;
	MyDialog dialog;
	private String user_name_string; //�û���
	private String user_name; //�û����û���ת��
	
	private ViewPager viewPager; //viewpager
	private ArrayList<View> pageViews; //���ҳ�������
	private ViewGroup integralGroup; // ���ҳ������϶�Ӧ����ҳ��
	private View integral_View_Score, integral_View_Rank; //��Ҫ������ҳ��,��ʾ��������ʾ����
	
	//�ϴ��Լ��Ļ���
	private String urlUploadScore = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&postintegral=on"; 
	//�������У�ǰ7������
	private String urlRankTotal = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&getIntegralList=on"; 
	//�Լ�������
	private String urlRankSelf = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&rankings=on"; 
	
	Bitmap bitmap;//����ʱ��Ļ��ͼ
	private String selfRank;

	//������
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
	
	private ArrayList<HashMap<String, Object>> rank_list = new ArrayList<HashMap<String,Object>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/**
		 * ���÷�ҳ����
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
		typeface = Typeface.createFromAsset(getAssets(), "hkhbt.ttf");//��������
		
		/**
		 * �󶨿ؼ�������ҳ��
		 */
		fraction = getIntent().getExtras().getString("fraction");//���շ���
		fractionTextView = (TextView)integral_View_Score.findViewById(R.id.Integral_Text_Fraction);
		shareButton = (Button)findViewById(R.id.Integral_Button_share);
		redeemButton = (Button)findViewById(R.id.Integral_Button_back);
		againButton = (Button)findViewById(R.id.Integral_Button_again);
		left_dot = (ImageView)findViewById(R.id.Integral_dot_left);
		right_dot = (ImageView)findViewById(R.id.Integral_dot_right);
		
		/**
		 * ���ÿؼ�����,����ҳ��
		 */
		fractionTextView.setTypeface(typeface);
		shareButton.setTypeface(typeface);
		redeemButton.setTypeface(typeface);
		againButton.setTypeface(typeface);
		
		/**
		 * �󶨿ؼ�������ҳ��
		 */
		
		integral_rank_you = (LinearLayout)integral_View_Rank.findViewById(R.id.integral_rank_you);
		rank_TextView = (TextView)integral_View_Rank.findViewById(R.id.integral_rank);
		rank_temple = (TextView)integral_View_Rank.findViewById(R.id.integral_rank_temple);
		rank_fighting = (TextView)integral_View_Rank.findViewById(R.id.integral_rank_fighting);
		listView = (ListView)integral_View_Rank.findViewById(R.id.integral_rank_list);
		
		
		/**
		 * ���ÿؼ�����,����ҳ��
		 */
		rank_TextView.setTypeface(typeface);
		rank_temple.setTypeface(typeface);
		rank_fighting.setTypeface(typeface);
		
		/**
		 * ���ÿؼ�
		 */
		fractionTextView.setText(fraction+getResources().getString(R.string.integral_fraction));
		
		/**
		 * ΢�ŷ���
		 
        mController.getConfig().supportQQPlatform(IntegralActivity.this, "http://www.umeng.com/social");  		
		// wx967daebe835fbeac������΢�ſ���ƽ̨ע��Ӧ�õ�AppID, ������Ҫ�滻����ע���AppID
		String appID = "wx3371792d0858282a";
		// ΢��ͼ�ķ����������һ��url 
		String contentUrl = "http://www.umeng.com/social";
		// ���΢��ƽ̨������1Ϊ��ǰActivity, ����2Ϊ�û������AppID, ����3Ϊ�������������ת����Ŀ��url
		UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(IntegralActivity.this,appID, contentUrl);
		//���÷������
		wxHandler.setWXTitle("��ѽ��ǻ�ʴ�");
		// ֧��΢������Ȧ
		UMWXHandler circleHandler = mController.getConfig().supportWXCirclePlatform(IntegralActivity.this,appID, contentUrl) ;
		circleHandler.setCircleTitle("��ѽ��ǻ�ʴ�");
		
		//qq�ռ�
		mController.getConfig().setSsoHandler(new QZoneSsoHandler(IntegralActivity.this));
		//����΢��
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		//������Ѷ΢��SSO handler
		 * 
		 */
		
		 mController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN,SHARE_MEDIA.RENREN,SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.SINA);
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
		
		
		shareButton.setOnClickListener(this);
		redeemButton.setOnClickListener(this);
		againButton.setOnClickListener(this);	
		
		//����û��Ƿ��¼
		SharedPreferences settings = this.getSharedPreferences("CheckLoginXML", 0);
		user_name_string = settings.getString("CheckLogin", "");	
		checkUserlogin(user_name_string);
		
	}
	

	/**
	 * sso��Ȩ
	 */
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		 /**ʹ��SSO��Ȩ����������´��� */
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }	
	}


	/**
     * ����û�
     * @param user_name_string shareperfence �д洢���û�����ֵ
     */
	private void checkUserlogin(String user_name_string) {
		// TODO Auto-generated method stub
		if (user_name_string.equals("")) {	
			rank_temple.setText(R.string.login_not);
			rank_fighting.setVisibility(View.GONE);
			//�û�û�е�¼
			dialog();		
		}else {			
			try {
				user_name = URLEncoder.encode(user_name_string,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//�����¼�ˣ��ϴ��û���������
			urlUploadScore = urlUploadScore + "&username=" + user_name + "&integral="+fraction;
			//�Լ����������ϴ��û���
			urlRankSelf = urlRankSelf + "&username=" + user_name;
			rank_fighting.setVisibility(View.VISIBLE);
			
			//����Ϊ0��
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
	 * �õ�ǰ��������
	 */
	private void getRankTotal() {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				Test_Bean data;
				try {
					//��ȡ�����Լ�ǰ7�������ֺͻ���
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
	 * ͨ��handler��֪�û������б���Ϣ�Ƿ��ѻ�ȡ
	 */
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:
				//�û������б�
				RankListView();
				break;
			case 2:
				//�ϴ������Ժ�õ��Լ������Σ���ʾ�µ�����������Ϣ
				//��ȡǰ7������
				getRankTotal();	
				//�õ��Լ�������
				getRankSelf();																
			case 3:
				//���÷�������
				shareResult();
				break;
			case 4:
				//����UI����ʾ������Ϣ
				rank_temple.setText(getResources().getString(R.string.integral_rank_temple)+selfRank+getResources().getString(R.string.integral_rank_temple2));
				break;
			default:
				break;
			}
		}
		
	};
	
	/**
	 * ���������б���Ϣ��ʾ
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
	 * �����ҳ��
	 */
	private void shareResult() {
		// TODO Auto-generated method stub
		mController.setShareContent("��ѽ��ǻ�ʴ�����ʴ����Ϸ��ʽ��������Ĭڶг�ı����Ϳ�ͨԪ�أ���ͨ���׶����ʴ���ʽ�����û��ƽ��˿�ǻ���ֳ�������ҽ�Ʊ���֪ʶ��");
		//���÷���ͼƬ, ����2ΪͼƬ��url��ַ
		mController.setShareMedia(new UMImage(IntegralActivity.this, bitmap));	
	}
	
	/**
	 * �ϴ�����
	 */
	private void UploadScoreThread() {
		// TODO Auto-generated method stub	
		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					
					//�ϴ����֣����ػ����Ƿ�ɹ����ɹ�֮����ȡ�����Լ�ǰ7��
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
	 * �õ��Լ�������
	 */
	private void getRankSelf() {
		// TODO Auto-generated method stub
		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					//�õ��Լ�������
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
	 * ��ʾ�û�δ��¼�Ի���
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
					//��ȡǰ��������		
					getRankTotal();
				}
			});
			dialog.setOnNoListenenr(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent;
					intent = new Intent(IntegralActivity.this, SignActivity.class);
					intent.putExtra("fraction", String.valueOf(fraction));//���ݷ���
					intent.putExtra("sign_type", "1");//������ת����, 1�������ҳ����ת��2����ֱ�ӵ�¼
					startActivity(intent);
					finish();
				}
			});
			dialog.show();
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
	 * ���η��ذ�ť
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
		//����
		case R.id.Integral_Button_share:		 
			  task(IntegralActivity.this);
			  mController.openShare(IntegralActivity.this, false);
			break;
		//����һ��
		case R.id.Integral_Button_again:		
			intent = new Intent(IntegralActivity.this, AnswerActivity.class);
			startActivity(intent);
			finish();
			break;
		//�ص���ҳ
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
	 * ��ȡ��ǰҳ��Ľ�ͼ
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
		//������
		handler.sendEmptyMessage(3);
	}
    

}
