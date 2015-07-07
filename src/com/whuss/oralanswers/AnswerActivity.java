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
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.UMSsoHandler;
import com.umeng.socialize.controller.UMWXHandler;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.TencentWBSsoHandler;

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
 * �ʴ�ҳ��
 * @author yangchao
 *
 *����߼���
 *1���������20����ͬ�����ִ������� random1 ����һ���������1-sum��165��֮�䣬����ڶ���ǰ�Ὣsum+=166��  �ڶ����� (sum-166)-sum֮��
  2��ÿ�θ���random1[id]ȡ��һ����Ŀ�����Ĵ𰸣�id����������20ʱ���¸�ֵΪ0
  3����a������������keyYES��
  4�������𰸷�����ʱ����keyStrings��
  5���������0-4֮���3�����ַ���keyRandom�ڣ����ڴ��Ҵ���𰸵�˳���������ȡ��Ҫ��ʾ��3����
  6������һ��0-3��֮���������������r�ڣ�����������ֽ���a�����Ӧ��textview�У�������ȷ��ѡ��ʱ��ѡ��ı���keySelect����ƥ��
  7������keySelect��ʼֵΪ5��ÿ�ε��ȷ���󶼻���и��Ӷ�Ӧѡ���ֵ�����ȷ����ὫkeySelect����ȷ�𰸵ı���r����ƥ�䣬�����ȷ�򽫼�������ı���fraction��1
  8�����ȷ����ť�ὫkeySelect���¸�ֵΪ5�����ҵ���Layout������������ˢ��ͼƬ
  9:����˳���ť����ȷ����ť��100���������ʾ�÷�ҳ�棬���Ҵ��ݷ�������fraction��
 */

/**
 * 
 * @author yangchao
 *��������
 *1��ʹ�÷���Randoms����������������һ����1-8֮�䣨����exitsum����8������֮��ÿ�η�Χ�ڣ�exitsum-add��-exitsum�䣨ÿ�ε��ʱ�Ὣexitsum����add��
 *2���˷��������һ�β���ʱ���ܻ���������831����Ϊ831���ֶ��޸ĳ�835
 *�˺����ӷ���һ�ĵ�3����ʼ
 */

public class AnswerActivity extends Activity implements android.view.View.OnClickListener{

	private Button oKButton,exitButton;
	public DBManager dbHelper;
	private SQLiteDatabase database;
	private Toast toast = null;

	private TextView titleTextView,answerTextView;//������Ŀ������
	private TextView A_TextView,B_TextView,C_TextView,D_TextView;//4��������
	private ImageView A_ImageView,B_ImageView,C_ImageView,D_ImageView;//4���𰸱�ʶͼƬ
	private LinearLayout A_Layout,B_Layout,C_Layout,D_Layout;//4���𰸰����ؼ���������¼���


	MyDialog dialog;

	private int count = 1;
	private int id = 0;//��ǰ���
	private int r ;//��ȷ�����
	private int keySelect = 5;//���ѡ��Ĵ����
	private int fraction = 0;//��¼����
	private int sum = 82;//������Ŀ��
	private int size = 10;//���鳤��
	private int sumAdd= 83;//���ӷ�Χ

	private int exitSum = 7;//������������Χ
	private int add = 8;//����������ֵ
	private int Id ;//��������ѯ���ݿ�ֵ
	private String duide= "" ;
	private int[] random1=new int[size];//����һ��nλ����int���� �����洢0��n֮��������
	private int [] images_ok = {R.drawable.a_ok,R.drawable.b_ok,R.drawable.c_ok,R.drawable.d_ok};
	private int [] images_no = {R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d};

	/**
	 * ������ʾ����
	 */
	private String [] keyStrings = new String[5];//���5�������
	private String keyYES = null;//�����ȷ��

	private int [] keyRandom = new int[3];//��Ŵ���������
	private String quizString = null; //�������



	private ArrayList<ImageView> imagsArray = new ArrayList<ImageView>();
	private ArrayList<TextView> textArray = new ArrayList<TextView>();
	Typeface typeface;
	List<Integer> Sumlist = new ArrayList<Integer>();

	//������
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_answe);

		typeface = Typeface.createFromAsset(getAssets(), "hkhbt.ttf");//�����Զ�������
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




		mController.getConfig().supportQQPlatform(AnswerActivity.this, "http://www.umeng.com/social");  		
		// wx967daebe835fbeac������΢�ſ���ƽ̨ע��Ӧ�õ�AppID, ������Ҫ�滻����ע���AppID
		String appID = "wx3371792d0858282a";
		// ΢��ͼ�ķ����������һ��url 
		String contentUrl = "http://www.umeng.com/social";
		// ���΢��ƽ̨������1Ϊ��ǰActivity, ����2Ϊ�û������AppID, ����3Ϊ�������������ת����Ŀ��url
		UMWXHandler wxHandler = mController.getConfig().supportWXPlatform(AnswerActivity.this,appID, contentUrl);
		//���÷������
		wxHandler.setWXTitle("��ѽ��ǻ�ʴ�");
		// ֧��΢������Ȧ
		UMWXHandler circleHandler = mController.getConfig().supportWXCirclePlatform(AnswerActivity.this,appID, contentUrl) ;
		circleHandler.setCircleTitle("��ѽ��ǻ�ʴ�");

		//qq�ռ�
		mController.getConfig().setSsoHandler(new QZoneSsoHandler(AnswerActivity.this));
		//����΢��
		mController.getConfig().setSsoHandler(new SinaSsoHandler());
		//������Ѷ΢��SSO handler


		mController.getConfig().removePlatform(SHARE_MEDIA.DOUBAN,SHARE_MEDIA.RENREN,SHARE_MEDIA.SMS);
		mController.getConfig().setSsoHandler(new TencentWBSsoHandler());


		for (int i = 1; i <= 183; i++) {
			Sumlist.add(i);
		}

		Randoms();


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
	 * ȡֵ
	 * @param stare ��ʼ��Χ
	 * @param sum   ������Χ
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
	 * ���
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
	 * @param n			���鳤��
	 * @param randoms	���������
	 * @param stare		��ʼ��Χ
	 * @param sum		������Χ
	 * @param what		ֵ֪ͨ
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
	 * whatֵΪ0����ѯ���ݿ�
	 * Ϊ1���������3��0-5֮���������keyRandom���飬����һ��0-3֮��������r��������ȷ�𰸵ı�ʶ��֪ͨ����UI���и��£����ݲ���r
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
	 * ����UI
	 * @param i  ��ȷ�����
	 * ������ȷ����Ž���ȷ�𰸷����Ӧ��text��
	 * Ȼ����������𰸷���������text��
	 * ��Ϻ�������Ŀ��
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
		titleTextView.setText(count+"");//������Ŀ��
		answerTextView.setText(quizString);//��������
		count++;//�����++
		id++;
	}




	/**
	 * �˳���ť�Ի���
	 * i:����Ϊ0ʱ���˳���Ϊ1ʱ����ȷ�𰸣�Ϊ2ʱ�Ǵ���Ϊ3ʱ�ǻش�100(���һ����)���ر����
	 */
	private void Dialog(int i){		

		dialog = new MyDialog(AnswerActivity.this);
		if (i != 3 ) {
			//dialog.setCanceledOnTouchOutside(true);//���õ���Ի���������رնԻ���
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
			ok.setText("��������");
			no.setText("�Ժ�����");
			break;
		case 2:
			title.setPadding(0, 30, 0, 0);
			//content.setVisibility(View.GONE);
			//no.setVisibility(View.GONE);
			title.setText(getResources().getString(R.string.answer_wrong));
			content.setText("��ȷ����:"+duide);
			content.setTextColor(0xF8F8FF00);
			ok.setText("��������");
			no.setText("�Ժ�����");
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
				content.setText("��ȷ����:"+duide);
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
	 * ����id��ѯ���ݿ�����
	 * @param id
	 */
	private void DB(int id)
	{
		//	��������830��
		//deal with database
		dbHelper = new DBManager(this);
		dbHelper.openDatabase();
		dbHelper.closeDatabase();

		database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/" + DBManager.DB_NAME, null);
		Cursor cur = database.rawQuery("SELECT * FROM question WHERE id ='"+String.valueOf(id) +"'", null);
		if (cur != null) {
			if (cur.moveToFirst()) {
				do {
					quizString = cur.getString(cur.getColumnIndex("topic"));//��Ŀ
					//   String condition = cur.getString(cur.getColumnIndex("condition"));//�Ѷ�
					keyYES = cur.getString(cur.getColumnIndex("answer_a"));//��ȷ��
					keyStrings[0] = cur.getString(cur.getColumnIndex("answer_b"));//�����1
					keyStrings[1] = cur.getString(cur.getColumnIndex("answer_c"));//�����2
					keyStrings[2] = cur.getString(cur.getColumnIndex("answer_d"));//�����3
					keyStrings[3] = cur.getString(cur.getColumnIndex("answer_e"));//�����4
					keyStrings[4] = cur.getString(cur.getColumnIndex("answer_f"));//�����5
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
	 * push������ҳ��
	 */
	private void pushIntegarl()
	{
		Intent intent =new Intent(AnswerActivity.this, IntegralActivity.class);
		intent.putExtra("fraction", String.valueOf(fraction));//���ݷ���
		startActivity(intent);
		finish();
	}


	/**
	 * ȷ����ť�¼�  ������
	 */
	private void clickButtonTwo()
	{
		//ѡ��Ĵ𰸺��Ƿ�����ȷ�𰸺�һ��,һ�������ӷ���
		if (keySelect == 5) {
			showTextToast(getString(R.string.answer_point));	

		}
		else {

			if(count<102){	
				exitSum = exitSum + add;


				if(count<21){
					if (keySelect == r) {
						Dialog(1); //�ش���ȷʱ�ĶԻ���
						fraction++;		
						okThread(String.valueOf(Id));
					}else{	
						Dialog(2); //�ش����ʱ�ĶԻ���
						noThread(String.valueOf(Id));
					}
				}else{
					Dialog(3);  //�ش�100����ĶԻ���
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
	 * ���η��ذ�ť
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
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



	private void share()
	{
		shareResult();
		mController.openShare(AnswerActivity.this, false);
	}

	/**
	 * �����ҳ��
	 */
	private void shareResult() {
		// TODO Auto-generated method stub
		mController.setShareContent("��ѽ��ǻ�ʴ�!------>" +
				"���⣺"+answerTextView.getText().toString()
				+"A:"+ A_TextView.getText().toString()
				+"B:"+ B_TextView.getText().toString()
				+"C:"+ C_TextView.getText().toString()
				+"D:"+ D_TextView.getText().toString());

	}
	
	
	/**
	 * ����¼�
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.Answer_Button_exit://�˳���ť
			//Dialog(0);
			share();
			break;
		case R.id.Answer_Button_OK://ȷ����ť
			clickButtonTwo();			
			break;
		case R.id.Answer_Layout_A://��A
			Layout(0);
			keySelect = 0;
			oKButton.setText("A:ȷ��");
			break;
		case R.id.Answer_Layout_B://��B
			Layout(1);
			keySelect = 1;
			oKButton.setText("B:ȷ��");
			break;
		case R.id.Answer_Layout_C://��C
			Layout(2);
			keySelect = 2;
			oKButton.setText("C:ȷ��");
			break;
		case R.id.Answer_Layout_D://��D
			Layout(3);
			keySelect = 3;
			oKButton.setText("D:ȷ��");
			break;
		default:
			break;
		}
	}

	/**
	 * ��ʾtoast����
	 * @param msg ���ݵ�stringֵ
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
