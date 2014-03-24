package com.evebit.adapter;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import com.evebit.json.DataManeger;
import com.evebit.json.Normal;
import com.evebit.json.Test_Bean;
import com.evebit.json.Test_Bean_TianQi;
import com.evebit.json.Test_Model;
import com.evebit.json.Test_Model_TianQi;
import com.evebit.json.Y_Exception;
import com.evebit.ui.MyDialog;
import com.evebit.ui.RedeemDialog;
import com.evebit.wudawenda.AnswerActivity;
import com.evebit.wudawenda.IntegralActivity;
import com.evebit.wudawenda.R;
import com.evebit.wudawenda.SignActivity;
import com.evebit.wudawenda.StoreActivity;

import android.R.integer;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ListViewAdapter extends BaseAdapter {
		
	//private Context context;
	StoreActivity context;
	private ArrayList<HashMap<String, Object>> list;    //��������Ϣ�б� 
    private LayoutInflater listContainer;           //��ͼ����   
    MyDialog dialog; // �����Ƿ�����һ� ����
    RedeemDialog redeemDialog; // �����㹻�һ����Ƿ�ȷ���һ� ����
	RedeemDialog redeemSucessDialog;//���ֳɹ��ĵ���
    private String score;
    public ImageLoader imageLoader; 
    private String username; //�û���
    private String redeemName;
    private String name;
    private int redeemId;
    private int listId;
    private String phone;
    private String redeemAddress;
    private String address;
    private String integral;
    private String leftScore;
    //��Ʒ�һ���url
    private String urlRedeem = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&prize=on";
  //�Լ��Ļ�������url
  	private String urlScoreSelf = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&rankings=on";
  	
    public final class ListItemView{  
    	//�Զ���ؼ�����     
    	public TextView store_listview_title;
    	public TextView store_listview_score;
    	public TextView store_listview_intro;
    	public ImageView store_listview_image;
        public Button redeemButton;
        
          
 }     
    
    public ListViewAdapter(StoreActivity context, ArrayList<HashMap<String, Object>> list, String score,String username){
    	this.context=context;
	    this.list = list;	 
	    this.score = score;
	    this.username = username;
	    listContainer = LayoutInflater.from(context);   //������ͼ���������������� 
	    imageLoader = new ImageLoader(context);
    }
    
    
    
    
    
    /**
	 * ����б�Ի���
	 * @param i    ����Ϊ1ʱ�ǵ�����Ʒ��Ϊ2ʱ���������޷��һ�, 3��û�е�¼
	 * @param j    ����Ϊjʱ��һ�е���Ʒ����
	 */
	
	private void dialog(final int i, final int j){
		
		  dialog = new MyDialog(context);
		 
		  LinearLayout redeem_DialogLayout = (LinearLayout)dialog.getRedeem();
		  LinearLayout bg_DialogLayout = (LinearLayout)dialog.getBG();
	
		  TextView  title = (TextView)dialog.getTitle(); //����
		  TextView content = (TextView)dialog.getConten(); //����
		  ImageView image = (ImageView)dialog.getImage();
		  Button ok = (Button)dialog.getOK();
		  Button no = (Button)dialog.getNo();
		  
		  TextView needTextView = (TextView)dialog.getNeed(); //��Ҫ���ٻ���
		  TextView haveTextView = (TextView)dialog.getHave(); //ӵ�ж��ٻ���
		  TextView leftTextView = (TextView)dialog.getLeft(); //ʣ�¶��ٻ���
		  
		  
		  
		  switch (i) {
			case 1:
				title.setPadding(0, 10, 0, 0);
				title.setText(content.getResources().getString(R.string.redeem_score));				
				redeem_DialogLayout.setVisibility(View.VISIBLE);
				ok.setText(content.getResources().getString(R.string.review));
				no.setText(content.getResources().getString(R.string.redeem));
				imageLoader.DisplayImage((String) list.get(j).get("image"), image);
				bg_DialogLayout.setBackgroundResource(R.drawable.dialogbg_big);
				content.setText(list.get(j).get("introtext").toString());
				needTextView.setText(String.valueOf(Integer.parseInt(list.get(j).get("score").toString())));
				haveTextView.setText(score);		
				leftTextView.setText(String.valueOf(Integer.valueOf(score)-Integer.parseInt((String)list.get(j).get("score"))));
			    break;			   
			case 2:
				title.setPadding(0, 10, 0, 0);
				redeem_DialogLayout.setVisibility(View.GONE);
				title.setText(content.getResources().getString(R.string.sorry));
				content.setVisibility(View.VISIBLE);
				content.setText(content.getResources().getString(R.string.store_not_enough));
				bg_DialogLayout.setBackgroundResource(R.drawable.exit);
				no.setVisibility(View.GONE);
				ok.setText(content.getResources().getString(R.string.sure));
				break;	
			case 3:
				title.setPadding(0, 10, 0, 0);
				redeem_DialogLayout.setVisibility(View.GONE);
				title.setText(content.getResources().getString(R.string.login_not));
				content.setVisibility(View.GONE);
				bg_DialogLayout.setBackgroundResource(R.drawable.exit);
				ok.setText(content.getResources().getString(R.string.cancle));
				no.setText(content.getResources().getString(R.string.login));
				break;
		   default:
			   break;
			   
		  }
		  
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
					dialog.dismiss();
					if (i == 1) {
						//�ѵ�¼�����ȷ���һ���jΪ�����Ʒ������				
					    redeemDialog(1,j);
					}else if (i== 3) {
						//û�е�¼����������
						goLoginPage();
					}
					
				}
			});
			dialog.show();
		 
	}
	
	/**
	 * ���ֶһ��ĵ���
	 * @param iΪ������������ͣ�1Ϊ���ֶһ����̣���д������Ϣ�Ĺ��̣�2Ϊ�һ��ɹ��Ľ���
	 * @param jΪ�����Ʒ������
	*/

	private void redeemDialog(int i, final int j){
			
		   redeemDialog = new RedeemDialog(context);
		 
		   TextView  title =(TextView)redeemDialog.getTitle();
		   TextView content = (TextView)redeemDialog.getConten();	
		 
		   ImageView itemImageView = (ImageView)redeemDialog.getItemImage();
		   TextView costTextView = (TextView)redeemDialog.getCost();
		   TextView remainTextView = (TextView)redeemDialog.getRemain();
		   final EditText phoneEditText = (EditText)redeemDialog.getPhone();
		   final EditText nameEditText = (EditText)redeemDialog.getName();
		   final EditText addressEditText = (EditText)redeemDialog.getAddress();
		   LinearLayout redeemProcessLinearLayout = (LinearLayout)redeemDialog.getRedeemLayout();
		   
		   RelativeLayout redeemSuccessRelativeLayout = (RelativeLayout)redeemDialog.getRedeemSuccess();
		   
   	
		   
		   phoneEditText.setKeyListener(new DigitsKeyListener(false, true));
		   nameEditText.setInputType(InputType.TYPE_CLASS_TEXT);
		   addressEditText.setInputType(InputType.TYPE_CLASS_TEXT);;
		   		   
		   switch (i) {
		   case 1:
			   //iΪ1 �����㹻�һ�
			   title.setPadding(0, 10, 0, 0);
			   imageLoader.DisplayImage((String) list.get(j).get("image"), itemImageView);
			   costTextView.setText(String.valueOf(Integer.parseInt(list.get(j).get("score").toString())));
			   remainTextView.setText(String.valueOf(Integer.parseInt(score)-Integer.parseInt(list.get(j).get("score").toString())));
			   content.setText(list.get(j).get("title").toString());
			   redeemProcessLinearLayout.setVisibility(View.VISIBLE);
			   redeemSuccessRelativeLayout.setVisibility(View.GONE);
			   break;
		   case 2:
			   
			   break;
		    default:
			   break;
		}
		   
		   
		   redeemDialog.setOnOkListenenr(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					redeemDialog.dismiss();
				}
			});
		   redeemDialog.setOnNoListenenr(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub	
					Normal normal = new Normal(context);// �ж��Ƿ�����������				
					redeemName  = nameEditText.getText().toString();
					redeemAddress = addressEditText.getText().toString(); 
					if(phoneEditText.getText().toString().equals("")||nameEditText.getText().toString().equals("")||addressEditText.getText().toString().equals("")){
						//���������ϢΪ��
						Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.redeem_info_null), Toast.LENGTH_SHORT).show();
					}else if(!normal.note_Intent(context.getApplicationContext())){
						//���û����
						Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.intenet_no), Toast.LENGTH_SHORT).show();
					}else{
						
						try {
							username = URLEncoder.encode(username,"UTF-8");
							name  = URLEncoder.encode(nameEditText.getText().toString(),"UTF-8");
							address = URLEncoder.encode(addressEditText.getText().toString(),"UTF-8"); 
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						phone = phoneEditText.getText().toString();
						listId= j;
						redeemId = Integer.parseInt((String) list.get(j).get("id"));
						integral = (String) list.get(j).get("score");
						leftScore = String.valueOf(Integer.parseInt(score)-Integer.parseInt(list.get(j).get("score").toString()));
						urlRedeem = urlRedeem + "&username="+ username +"&lpid="+ redeemId +"&integral="+ integral + "&name="+ name +"&phone="+ phone +"&address="+address;
						redeemThread();	
						
					}
					
			
				}
			
			});	   
		   redeemDialog.show();	
	}
	
	/**
	 * 
	 * �һ�
	 * @param j ��Ʒlist�е�����
	 */
	private void redeemThread() {
		// TODO Auto-generated method stub

		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					
					//�ϴ��û�������Ʒid, �ͻ���Ϣ����ʵ�������ֻ��ţ��ͻ���ַ�� ���ضһ��Ƿ�ɹ���Ϣ
					data = DataManeger.getTestData_TianQi(urlRedeem);
					Test_Model_TianQi  test_Model_User = data.getData();	
					
					if (test_Model_User.getErrorid().equals("1")) {
					   //1Ϊ�һ��ɹ�
						handler.sendEmptyMessage(1);
						goUpdateUI(leftScore);
					}else {
						
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
	 * ͨ��handler��֪�û��һ��Ƿ�ɹ�
	 */
	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 1:		
				redeemDialog.dismiss();					
				redeemSucessDialog(listId,phone,redeemName,redeemAddress);
				break;
			case 2:
				Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.redeem_info_wrong), Toast.LENGTH_SHORT).show();
				
				break;
			default:
				break;
			}
		}

	
	};
	

	
	/**
	 * ���ֳɹ��ĵ���
	 * @param jΪ�û�ϣ���һ�����Ʒ����
	 * @param phone ���ֶһ�ҳ����д���ֻ���
	 * @param name ���ֶһ�ҳ����д������
	 * @param address ���ֶһ�ҳ����д�ĵ�ַ
	 */
	private void redeemSucessDialog(final int j,String phone,String name,String address){
		
		
		   redeemSucessDialog = new RedeemDialog(context);
		
		   TextView  title =(TextView)redeemSucessDialog.getTitle();
		   LinearLayout redeemProcessLinearLayout = (LinearLayout)redeemSucessDialog.getRedeemLayout();
		   TextView successProInfoTextView = (TextView)redeemSucessDialog.getProInfo();
		   TextView successRecieveInfoTextView = (TextView)redeemSucessDialog.getRecieverInfo();
		   TextView successPhoneInfoTextView = (TextView)redeemSucessDialog.getPhoneInfo();
		   TextView successAddressInfoTextView = (TextView)redeemSucessDialog.getAddressInfo();
		   Button leftButton = (Button)redeemSucessDialog.getLeftButton();
		   Button rightButton = (Button)redeemSucessDialog.getRightButton();
		  
		   ImageView stampImageView = (ImageView)redeemSucessDialog.getRedeemStamp();	   
		   ImageView itemSuccessImageView = (ImageView)redeemSucessDialog.getSuccessItemImage();
		   RelativeLayout redeemSuccessRelativeLayout = (RelativeLayout)redeemSucessDialog.getRedeemSuccess();
		   
			   title.setPadding(0, 10, 0, 0);
			   redeemProcessLinearLayout.setVisibility(View.GONE);
			   redeemSuccessRelativeLayout.setVisibility(View.VISIBLE);
			   title.setText(context.getResources().getString(R.string.redeem_success));		   
			   successProInfoTextView.setText(list.get(j).get("title").toString());
			   imageLoader.DisplayImage((String) list.get(j).get("image"), itemSuccessImageView);
			   successRecieveInfoTextView.setText(name);
			   successPhoneInfoTextView.setText(phone);
			   successAddressInfoTextView.setText(address);
			   stampImageView.setBackgroundResource(R.drawable.redeem_stamp);
			   leftButton.setText(context.getResources().getString(R.string.sure));
			   rightButton.setText(context.getResources().getString(R.string.redeem_continue));
		
		   
		   redeemSucessDialog.setOnOkListenenr(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//ȷ��
					redeemSucessDialog.dismiss();
				
				}

				
			});
		   redeemSucessDialog.setOnNoListenenr(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub	
					//��������
					redeemSucessDialog.dismiss();
					goAnswerPage();
					
				}
			});
		   
		   redeemSucessDialog.show();
		   
		   
	}
    
    /**
     * ��ת���ʴ�ҳ��
     */
	public void goAnswerPage(){
		Intent intent = new Intent(context, AnswerActivity.class);
		context.startActivity(intent);    
	}
	
	/**
	 * �����û�����
	 * @param score
	 */
	private void goUpdateUI(String score) {
		// TODO Auto-generated method stub	
		context.setSelfScore(score);
	}
	
	
	public void goLoginPage(){
		Intent intent = new Intent(context, SignActivity.class);
		intent.putExtra("sign_type", "3");//������ת����¼ҳ�������, 1�������ҳ����ת��2����ֱ�ӵ�¼��3����ӻ����̳���ת
		context.startActivity(intent);    
	}
    
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	
	
	 /**  
     * ListView Item����  
     */  
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		  ListItemView  listItemView = null;   
	        if (convertView == null) {   
	            listItemView = new ListItemView();    
	            //��ȡlist_item�����ļ�����ͼ   
	            convertView = listContainer.inflate(R.layout.list_store, null);   
	            //��ȡ�ؼ�����   
	            listItemView.store_listview_title = (TextView)convertView.findViewById(R.id.Store_listview_title);   
	            listItemView.store_listview_score = (TextView)convertView.findViewById(R.id.Store_listview_score);   
	            listItemView.store_listview_intro = (TextView)convertView.findViewById(R.id.Store_listview_intro);   
	            listItemView.store_listview_image = (ImageView)convertView.findViewById(R.id.Store_listview_image);   
	            listItemView.redeemButton = (Button)convertView.findViewById(R.id.Store_listview_button); 
	            //���ÿؼ�����convertView   
	            convertView.setTag(listItemView);   
	        }else {   
	            listItemView = (ListItemView)convertView.getTag();   
	        }   

	      
	        listItemView.store_listview_title.setText((String) list.get(position).get("title"));   
	        listItemView.store_listview_score.setText((String) list.get(position).get("score"));  
	        listItemView.store_listview_intro.setText((String) list.get(position).get("introtext")); 
	        imageLoader.DisplayImage((String) list.get(position).get("image"), listItemView.store_listview_image);
	     
	        listItemView.redeemButton.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
				    
					//���δ��¼
					
					if (username.equals("")) {
						dialog(3,0);
						
					}else {				
						
						if (Integer.parseInt(score) < Integer.parseInt((String) list.get(position).get("score"))) {
							//�ܻ���С����ѡ��Ʒ����,���ܶһ�
							dialog(2,0);
						}else {
							//�ܻ��ִ�����ѡ��Ʒ����,�����һ�����
							dialog(1, position);
						}
					}
					
				}
			});
			
	        return convertView;   
	}

}
