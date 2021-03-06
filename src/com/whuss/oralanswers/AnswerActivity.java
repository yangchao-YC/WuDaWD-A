package com.whuss.oralanswers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.evebit.json.DataManeger;
import com.evebit.json.Test_Bean_TianQi;
import com.evebit.json.Test_Model;
import com.evebit.json.Y_Exception;
import com.evebit.ui.DBManager;
import com.evebit.ui.MyDialog;
import com.whuss.oralanswers.R;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 问答页面
 * @author yangchao
 *
 *设计逻辑：
 *1：随机生成20个不同的数字存入数组 random1 ，第一次随机数在1-sum（165）之间，进入第二次前会将sum+=166；  第二次在 (sum-166)-sum之间
  2：每次根据random1[id]取出一个题目与他的答案，id自增，等于20时重新赋值为0
  3：答案a单独存放入变量keyYES内
  4：其他答案放入临时数组keyStrings内
  5：随机生成0-4之间的3个数字放入keyRandom内，用于打乱错误答案的顺序与随机抽取需要显示的3个答案
  6：生成一个0-3的之间的随机数放入变量r内，根据这个数字将答案a放入对应的textview中，作用于确定选择时与选择的变量keySelect进行匹配
  7：变量keySelect初始值为5，每次点击确定后都会进行附加对应选择的值，点击确定后会将keySelect与正确答案的变量r进行匹配，如果正确则将计算分数的变量fraction加1
  8：点击确定按钮会将keySelect重新赋值为5，并且调用Layout方法进行重新刷新图片
  9:点击退出按钮或者确定按钮到100题后跳入显示得分页面，并且传递分数变量fraction，
 */

/**
 * 
 * @author yangchao
 *方法二：
 *1：使用方法Randoms随机产生随机数，第一次在1-8之间（变量exitsum代表8，），之后每次范围在（exitsum-add）-exitsum间（每次点击时会将exitsum增加add）
 *2：此方法在最后一次产生时可能会产生随机数831，如为831则手动修改成835
 *此后动作从方法一的第3步开始
 */

public class AnswerActivity extends Activity implements android.view.View.OnClickListener{

	private Button oKButton,exitButton;
	public DBManager dbHelper;
	private SQLiteDatabase database;
	private Toast toast = null;

	private TextView titleTextView,answerTextView;//标题数目，问题
	private TextView A_TextView,B_TextView,C_TextView,D_TextView;//4个答案文字
	private ImageView A_ImageView,B_ImageView,C_ImageView,D_ImageView;//4个答案标识图片
	private LinearLayout A_Layout,B_Layout,C_Layout,D_Layout;//4个答案包含控件，做点击事件用


	MyDialog dialog;

	private int count = 1;
	private int id = 0;//当前题号
	private int r ;//正确答案题号
	private int keySelect = 5;//存放选择的答案题号
	private int fraction = 0;//记录分数
	private int sum = 82;//计算题目数
	private int size = 10;//数组长度
	private int sumAdd= 83;//增加范围

	private int exitSum = 7;//方法二结束范围
	private int add = 8;//方法二增长值
	private int Id ;//方法二查询数据库值
	private String duide= "" ;
	private int[] random1=new int[size];//定义一个n位长的int数组 用来存储0打n之间的随机数
	private int [] images_ok = {R.drawable.a_ok,R.drawable.b_ok,R.drawable.c_ok,R.drawable.d_ok};
	private int [] images_no = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d};

	/**
	 * 答题显示所需
	 */
	private String [] keyStrings = new String[5];//存放5个错误答案
	private String keyYES = null;//存放正确答案

	private int [] keyRandom = new int[3];//存放错误答案随机数
	private String quizString = null; //存放问题



	private ArrayList<ImageView> imagsArray = new ArrayList<ImageView>();
	private ArrayList<TextView> textArray = new ArrayList<TextView>();
	Typeface typeface;
	List<Integer> Sumlist = new ArrayList<Integer>();

	//分享功能
	//final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
	
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_answe);

		typeface = Typeface.createFromAsset(getAssets(), "hkhbt.ttf");//设置自定义字体
		oKButton = (Button)findViewById(R.id.Answer_Button_OK);

		titleTextView = (TextView)findViewById(R.id.Answer_Text_Top_Title);
		answerTextView = (TextView)findViewById(R.id.Answer_Text_Answer);
		exitButton = (Button)findViewById(R.id.Answer_Button_exit);

		A_TextView = (TextView)findViewById(R.id.Answer_TextView_A);
		B_TextView = (TextView)findViewById(R.id.Answer_TextView_B);
		C_TextView = (TextView)findViewById(R.id.Answer_TextView_C);
		D_TextView = (TextView)findViewById(R.id.Answer_TextView_D);

		A_ImageView = (ImageView)findViewById(R.id.Answer_image_A);
		B_ImageView = (ImageView)findViewById(R.id.Answer_image_B);
		C_ImageView = (ImageView)findViewById(R.id.Answer_image_C);
		D_ImageView = (ImageView)findViewById(R.id.Answer_image_D);

		imagsArray.add(A_ImageView);
		imagsArray.add(B_ImageView);
		imagsArray.add(C_ImageView);
		imagsArray.add(D_ImageView);

		textArray.add(A_TextView);
		textArray.add(B_TextView);
		textArray.add(C_TextView);
		textArray.add(D_TextView);

		A_Layout =(LinearLayout)findViewById(R.id.Answer_Layout_A);
		B_Layout =(LinearLayout)findViewById(R.id.Answer_Layout_B);
		C_Layout =(LinearLayout)findViewById(R.id.Answer_Layout_C);
		D_Layout =(LinearLayout)findViewById(R.id.Answer_Layout_D);

		A_Layout.setOnClickListener(this);
		B_Layout.setOnClickListener(this);
		C_Layout.setOnClickListener(this);
		D_Layout.setOnClickListener(this);
		oKButton.setOnClickListener(this);
		exitButton.setOnClickListener(this);

		oKButton.setTypeface(typeface);
		titleTextView.setTypeface(typeface);
		answerTextView.setTypeface(typeface);	
		A_TextView.setTypeface(typeface);
		B_TextView.setTypeface(typeface);
		C_TextView.setTypeface(typeface);
		D_TextView.setTypeface(typeface);


/*
String appID = "wx3371792d0858282a";
		mController.getConfig().supportQQPlatform(AnswerActivity.this, "http://www.umeng.com/social");  		
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appID = "wx3371792d0858282a";
		// 微信图文分享必须设置一个url 
		String contentUrl = "http://www.umeng.com/social";
		// 添加微信平台，参数1为当前Activity, 参数2为用户申请的AppID, 参数3为点击分享内容跳转到的目标url
	//	UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(AnswerActivity.this,appID, contentUrl);
		//设置分享标题
	//	wxHandler.setWXTitle("口腔知识问答");
		// 支持微信朋友圈
	//	UMWXHandler circleHandler = mController.getConfig().supportWXCirclePlatform(AnswerActivity.this,appID, contentUrl) ;
	//	circleHandler.setCircleTitle("口腔知识问答");

		//qq空间
	//	mController.getConfig().setSsoHandler(new QZoneSsoHandler(AnswerActivity.this));
	//	//新浪微博
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		//设置腾讯微博SSO handler


		
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
*/

		String appID = "wx4f9b4f89d241741a";
		String appSecret = "0fb57f6d2b9547d1396cf8cfcddb87f2";
		
		UMWXHandler wxHandler = new UMWXHandler(AnswerActivity.this,appID,appSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(AnswerActivity.this,appID,appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
		
		
		
		mController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN,SHARE_MEDIA.RENREN,SHARE_MEDIA.SMS);
		
		
		for (int i = 1; i <= 183; i++) {
			Sumlist.add(i);
		}

		Randoms();


	}

	/**
	 * sso授权
	 */
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		 /**使用SSO授权必须添加如下代码*/
	    UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode) ;
	    if(ssoHandler != null){
	       ssoHandler.authorizeCallBack(requestCode, resultCode, data);
	    }	 
	}
	
	
	
	/**
	 * 取值
	 * @param stare 起始范围
	 * @param sum   结束范围
	 */
	private void Randoms ()
	{
		/*
		if (sum == 831) {
			sum = 835;
		}
		Id = stare +(int)Math.round(Math.random()*8);
		handler.sendEmptyMessage(3);
		 */
		int key = 0;
		key = 1 + (int)Math.round(Math.random()*(Sumlist.size()-1));
		System.out.println("AnswerActivity.Randoms()   key:  "+key);
		Id =Sumlist.get(key);

		Sumlist.remove(key);


		handler.sendEmptyMessage(3);

	}

	/**
	 * 答对
	 * @param phone
	 */
	private void okThread(final String id)
	{
		new Thread()
		{
			@Override
			public void run() {
				// TODO Auto-generated method stub

				String urlString = LoginActivity.URL + "index.php?option=com_content&id="+id+"&ttrue=1&lang=en";
				System.out.println("AnswerActivity.okThread()   "   + urlString);

				Test_Bean_TianQi data;
				try {
					data = DataManeger.getTestData_TianQi(urlString);
				} catch (Y_Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

		}.start();
	}
	private void noThread(final String id)
	{
		new Thread()
		{
			@Override
			public void run() {
				// TODO Auto-generated method stub

				String urlString = LoginActivity.URL + "index.php?option=com_content&id="+id+"&ttrue=2&lang=en";
				System.out.println("AnswerActivity.okThread()   "   + urlString);
				Test_Bean_TianQi data;
				try {
					data = DataManeger.getTestData_TianQi(urlString);
				} catch (Y_Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

		}.start();
	}

	/**
	 * 
	 * @param n			数组长度
	 * @param randoms	存入的数组
	 * @param stare		起始范围
	 * @param sum		结束范围
	 * @param what		通知值
	 */
	private void getRandom(final int n,final int[] randoms,final int stare, final int sum,final int what){

		new Thread()
		{

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					List<Integer> list = new ArrayList<Integer>();

					for (int i = stare; i <=sum ; i++) {
						list.add(i);
					}

					for (int j = 0; j < n; j++) {
						int random =(int)(Math.random()*list.size()) ;
						randoms[j] = list.get(random);
						list.remove(random);
					}				
					handler.sendEmptyMessage(what);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}				
			}

		}.start();

	}

	/**
	 * what值为0：查询数据库
	 * 为1：随机产生3个0-5之间的数放入keyRandom数组，产生一个0-3之间的随机数r做放入正确答案的标识，通知方法UI进行更新，传递参数r
	 */
	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				DB(random1[id]);
				break;
			case 1:
				getRandom(3,keyRandom,0,4,2);
				break;
			case 2:
				r =(int)Math.round(Math.random()*3);
				UI(r);
				break;
			case 3:
				DB(Id);
				break;
			default:
				break;
			}
		}

	};
	/**
	 * 更新UI
	 * @param i  正确答案题号
	 * 根据正确答案题号将正确答案放入对应的text中
	 * 然后将其他错误答案放入其他的text中
	 * 完毕后设置题目数
	 */
	private void UI(int i)
	{
		int make = 0;
		for (int j = 0; j < 4; j++) {
			if (j == i) {
				textArray.get(j).setText(keyYES);
				duide = keyYES;
			}
			else {				
				textArray.get(j).setText(keyStrings[keyRandom[make]]);
				make++;
			}
		}
		titleTextView.setText(count+"");//设置题目数
		answerTextView.setText(quizString);//设置问题
		count++;//总题号++
		id++;
	}




	/**
	 * 退出按钮对话框
	 * i:参数为0时是退出，为1时是正确答案，为2时是错误，为3时是回答到100(最后一道题)的特别情况
	 */
	private void Dialog(int i){		

		dialog = new MyDialog(AnswerActivity.this);
		if (i != 3 ) {
			//dialog.setCanceledOnTouchOutside(true);//设置点击对话框外区域关闭对话框
		}

		TextView  title =(TextView)dialog.getTitle();
		TextView content = (TextView)dialog.getConten();
		Button ok = (Button)dialog.getOK();
		Button no = (Button)dialog.getNo();


		switch (i) {
		case 1:
			title.setPadding(0, 30, 0, 0);
			content.setVisibility(View.GONE);
			//no.setVisibility(View.GONE);
			title.setText(getResources().getString(R.string.answer_right));
			ok.setText("继续答题");
			no.setText("以后再玩");
			break;
		case 2:
			title.setPadding(0, 30, 0, 0);
			//content.setVisibility(View.GONE);
			//no.setVisibility(View.GONE);
			title.setText(getResources().getString(R.string.answer_wrong));
			content.setText("正确答案是:"+duide);
			content.setTextColor(0xF8F8FF00);
			ok.setText("继续答题");
			no.setText("以后再玩");
			break;
		case 3:
			System.out.println("key:"+keySelect+"    r:"+r);
			if (keySelect == r) {
				title.setPadding(0, 30, 0, 0);
				content.setVisibility(View.GONE);
				ok.setVisibility(View.GONE);
				title.setText(getResources().getString(R.string.answer_right));
				no.setText(getResources().getString(R.string.sure));
			}
			else {
				title.setPadding(0, 30, 0, 0);
				//content.setVisibility(View.GONE);
				ok.setVisibility(View.GONE);
				title.setText(getResources().getString(R.string.answer_wrong));
				content.setText("正确答案是:"+duide);
				content.setTextColor(0xF8F8FF00);
				no.setText(getResources().getString(R.string.sure));
			}
			break;
		default:
			break;
		}    

		dialog.setOnOkListenenr(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				Randoms();
			}
		});
		dialog.setOnNoListenenr(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pushIntegarl();
			}
		});
		dialog.show();
	}
	/**
	 * 根据id查询数据库数据
	 * @param id
	 */
	private void DB(int id)
	{
		//	数据总数830条
		//deal with database
		dbHelper = new DBManager(this);
		dbHelper.openDatabase();
		dbHelper.closeDatabase();

		database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
		Cursor cur = database.rawQuery("SELECT * FROM question WHERE id ='"+String.valueOf(id) +"'", null);
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					quizString = cur.getString(cur.getColumnIndex("topic"));//题目
					//   String condition = cur.getString(cur.getColumnIndex("condition"));//难度
					keyYES = cur.getString(cur.getColumnIndex("answer_a"));//正确答案
					keyStrings[0] = cur.getString(cur.getColumnIndex("answer_b"));//错误答案1
					keyStrings[1] = cur.getString(cur.getColumnIndex("answer_c"));//错误答案2
					keyStrings[2] = cur.getString(cur.getColumnIndex("answer_d"));//错误答案3
					keyStrings[3] = cur.getString(cur.getColumnIndex("answer_e"));//错误答案4
					keyStrings[4] = cur.getString(cur.getColumnIndex("answer_f"));//错误答案5
				} while (cur.moveToNext());
			}
		} else {
			System.out.println("null");
		}
		cur.close();
		database.close();
		handler.sendEmptyMessage(1);		
	}

	/**
	 * push到分数页面
	 */
	private void pushIntegarl()
	{
		Intent intent =new Intent(AnswerActivity.this, IntegralActivity.class);
		intent.putExtra("fraction", String.valueOf(fraction));//传递分数
		startActivity(intent);
		finish();
	}


	/**
	 * 确定按钮事件  方法二
	 */
	private void clickButtonTwo()
	{
		//选择的答案号是否与正确答案号一致,一致则增加分数
		if (keySelect == 5) {
			showTextToast(getString(R.string.answer_point));	

		}
		else {

			if(count<102){	
				exitSum = exitSum + add;


				if(count<21){
					if (keySelect == r) {
						Dialog(1); //回答正确时的对话框
						fraction++;		
						okThread(String.valueOf(Id));
					}else{	
						Dialog(2); //回答错误时的对话框
						noThread(String.valueOf(Id));
					}
				}else{
					Dialog(3);  //回答到100道题的对话框
				}	



			}else {
				pushIntegarl();
			}



			keySelect = 5;
			Layout(keySelect);
			oKButton.setText(getResources().getString(R.string.answer_button));

		}

	}

	/**
	 * 屏蔽返回按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
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



	private void share()
	{
		shareResult();
	//	mController.openShare(AnswerActivity.this, false);
	}

	/**
	 * 分享此页面
	 */
	private void shareResult() {
		// TODO Auto-generated method stub

		
		String shareString = "哎呀口腔问答!->" +
				"问题："+answerTextView.getText().toString()
				+"A:"+ A_TextView.getText().toString()
				+"B:"+ B_TextView.getText().toString()
				+"C:"+ C_TextView.getText().toString()
				+"D:"+ D_TextView.getText().toString()+"    http://shouji.baidu.com/software/item?docid=7820651&from=as";
		

		WeiXinShareContent weixinContent = new WeiXinShareContent();
		
		weixinContent.setTitle("口腔知识问答");
		
		weixinContent.setShareContent(shareString);
		
		weixinContent.setTargetUrl("http://www.whuss.com/");
		
		mController.setShareMedia(weixinContent);
		
		
		
		
		CircleShareContent circleMedia = new CircleShareContent();
		circleMedia.setShareContent(shareString);
		//设置朋友圈title
		circleMedia.setTitle("口腔知识问答");

		circleMedia.setTargetUrl("http://www.whuss.com/");
		mController.setShareMedia(circleMedia);
		mController.openShare(AnswerActivity.this, false);
		
		mController.setShareContent(shareString);

		
		//mController.setShareContent("友盟社会化组件（SDK）让移动应用快速整合社交分享功能，http://www.umeng.com/social");
		
	}
	
	
	
	
	
	
	
	
	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Answer_Button_exit://退出按钮
			//Dialog(0);
			share();
			break;
		case R.id.Answer_Button_OK://确定按钮
			clickButtonTwo();			
			break;
		case R.id.Answer_Layout_A://答案A
			Layout(0);
			keySelect = 0;
			oKButton.setText("A:确定");
			break;
		case R.id.Answer_Layout_B://答案B
			Layout(1);
			keySelect = 1;
			oKButton.setText("B:确定");
			break;
		case R.id.Answer_Layout_C://答案C
			Layout(2);
			keySelect = 2;
			oKButton.setText("C:确定");
			break;
		case R.id.Answer_Layout_D://答案D
			Layout(3);
			keySelect = 3;
			oKButton.setText("D:确定");
			break;
		default:
			break;
		}
	}

	/**
	 * 显示toast方法
	 * @param msg 传递的string值
	 */
	private void showTextToast(String msg) {
		if (toast == null) {
			toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
		} else {
			toast.setText(msg);
		}
		toast.show();
	}

	private void Layout(int i)
	{
		if (i== 5) {
			for (int j = 0; j < imagsArray.size(); j++) {
				imagsArray.get(j).setImageResource(images_no[j]);
			}
		}
		else {
			for (int j = 0; j < imagsArray.size(); j++) {
				if (j!=i) {
					imagsArray.get(j).setImageResource(images_no[j]);
				}
				else {
					imagsArray.get(j).setImageResource(images_ok[j]);
				}
			}
		}

	}
	
	
	
}





