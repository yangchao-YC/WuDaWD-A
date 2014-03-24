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
	private ArrayList<HashMap<String, Object>> list;    //户排名信息列表 
    private LayoutInflater listContainer;           //视图容器   
    MyDialog dialog; // 积分是否满足兑换 弹窗
    RedeemDialog redeemDialog; // 积分足够兑换，是否确定兑换 弹窗
	RedeemDialog redeemSucessDialog;//积分成功的弹窗
    private String score;
    public ImageLoader imageLoader; 
    private String username; //用户名
    private String redeemName;
    private String name;
    private int redeemId;
    private int listId;
    private String phone;
    private String redeemAddress;
    private String address;
    private String integral;
    private String leftScore;
    //商品兑换的url
    private String urlRedeem = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&prize=on";
  //自己的积分排名url
  	private String urlScoreSelf = "http://121.199.29.181/demo/wudawenda/index.php?option=com_content&rankings=on";
  	
    public final class ListItemView{  
    	//自定义控件集合     
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
	    listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文 
	    imageLoader = new ImageLoader(context);
    }
    
    
    
    
    
    /**
	 * 点击列表对话框
	 * @param i    参数为1时是弹出商品，为2时分数不够无法兑换, 3是没有登录
	 * @param j    参数为j时这一列的商品索引
	 */
	
	private void dialog(final int i, final int j){
		
		  dialog = new MyDialog(context);
		 
		  LinearLayout redeem_DialogLayout = (LinearLayout)dialog.getRedeem();
		  LinearLayout bg_DialogLayout = (LinearLayout)dialog.getBG();
	
		  TextView  title = (TextView)dialog.getTitle(); //标题
		  TextView content = (TextView)dialog.getConten(); //内容
		  ImageView image = (ImageView)dialog.getImage();
		  Button ok = (Button)dialog.getOK();
		  Button no = (Button)dialog.getNo();
		  
		  TextView needTextView = (TextView)dialog.getNeed(); //需要多少积分
		  TextView haveTextView = (TextView)dialog.getHave(); //拥有多少积分
		  TextView leftTextView = (TextView)dialog.getLeft(); //剩下多少积分
		  
		  
		  
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
						//已登录，点击确定兑换，j为点击商品的索引				
					    redeemDialog(1,j);
					}else if (i== 3) {
						//没有登录，弹出窗口
						goLoginPage();
					}
					
				}
			});
			dialog.show();
		 
	}
	
	/**
	 * 积分兑换的弹框
	 * @param i为跳出弹框的类型，1为积分兑换过程，填写个人信息的过程，2为兑换成功的界面
	 * @param j为点击商品的索引
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
			   //i为1 积分足够兑换
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
					Normal normal = new Normal(context);// 判断是否有网络连接				
					redeemName  = nameEditText.getText().toString();
					redeemAddress = addressEditText.getText().toString(); 
					if(phoneEditText.getText().toString().equals("")||nameEditText.getText().toString().equals("")||addressEditText.getText().toString().equals("")){
						//如果输入信息为空
						Toast.makeText(context.getApplicationContext(), context.getResources().getString(R.string.redeem_info_null), Toast.LENGTH_SHORT).show();
					}else if(!normal.note_Intent(context.getApplicationContext())){
						//如果没连网
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
	 * 兑换
	 * @param j 商品list中的索引
	 */
	private void redeemThread() {
		// TODO Auto-generated method stub

		new Thread(){
			public void run() {
				Test_Bean_TianQi data;
				try {
					
					//上传用户名，商品id, 送货信息（真实姓名，手机号，送货地址） 返回兑换是否成功信息
					data = DataManeger.getTestData_TianQi(urlRedeem);
					Test_Model_TianQi  test_Model_User = data.getData();	
					
					if (test_Model_User.getErrorid().equals("1")) {
					   //1为兑换成功
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
	 * 通过handler告知用户兑换是否成功
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
	 * 积分成功的弹框
	 * @param j为用户希望兑换的商品索引
	 * @param phone 积分兑换页面填写的手机号
	 * @param name 积分兑换页面填写的名字
	 * @param address 积分兑换页面填写的地址
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
					//确定
					redeemSucessDialog.dismiss();
				
				}

				
			});
		   redeemSucessDialog.setOnNoListenenr(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub	
					//继续答题
					redeemSucessDialog.dismiss();
					goAnswerPage();
					
				}
			});
		   
		   redeemSucessDialog.show();
		   
		   
	}
    
    /**
     * 跳转至问答页面
     */
	public void goAnswerPage(){
		Intent intent = new Intent(context, AnswerActivity.class);
		context.startActivity(intent);    
	}
	
	/**
	 * 更新用户积分
	 * @param score
	 */
	private void goUpdateUI(String score) {
		// TODO Auto-generated method stub	
		context.setSelfScore(score);
	}
	
	
	public void goLoginPage(){
		Intent intent = new Intent(context, SignActivity.class);
		intent.putExtra("sign_type", "3");//传递跳转到登录页面的类型, 1代表积分页面跳转，2代表直接登录，3代表从积分商城跳转
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
     * ListView Item设置  
     */  
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		  ListItemView  listItemView = null;   
	        if (convertView == null) {   
	            listItemView = new ListItemView();    
	            //获取list_item布局文件的视图   
	            convertView = listContainer.inflate(R.layout.list_store, null);   
	            //获取控件对象   
	            listItemView.store_listview_title = (TextView)convertView.findViewById(R.id.Store_listview_title);   
	            listItemView.store_listview_score = (TextView)convertView.findViewById(R.id.Store_listview_score);   
	            listItemView.store_listview_intro = (TextView)convertView.findViewById(R.id.Store_listview_intro);   
	            listItemView.store_listview_image = (ImageView)convertView.findViewById(R.id.Store_listview_image);   
	            listItemView.redeemButton = (Button)convertView.findViewById(R.id.Store_listview_button); 
	            //设置控件集到convertView   
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
				    
					//如果未登录
					
					if (username.equals("")) {
						dialog(3,0);
						
					}else {				
						
						if (Integer.parseInt(score) < Integer.parseInt((String) list.get(position).get("score"))) {
							//总积分小于所选商品积分,不能兑换
							dialog(2,0);
						}else {
							//总积分大于所选商品积分,跳出兑换弹窗
							dialog(1, position);
						}
					}
					
				}
			});
			
	        return convertView;   
	}

}
