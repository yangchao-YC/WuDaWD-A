package com.evebit.ui;


import com.evebit.wudawenda.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RedeemDialog extends Dialog {
	
	private Context context;
	private Button leftButton,rightButton; //兑换页面好几个按钮，分为左边和右边的
	private TextView titleTextView,contenTextView,costTextView,remainTextView;
	private TextView contentTempleTextView, consumTempleTextView, consumScoreTempleTextView, remainTempleTextView,remianScoreTempleTextView,hint1TextView,hint2TextView;
	private TextView successProTempleTextView, successProInfoTextView, sucessRecieveTextView, successRecieveInfoTextView,sucessPhoneTextView, successPhoneInfoTextView,successAddressTextView,successAddressInfoTextView,successHintTextView;
	private EditText phoneEditText,nameEditText,addressEditText;
	private LinearLayout redeemProcessLinearLayout;
	private RelativeLayout redeemSuccessRelativeLayout;
	private ImageView itemImageView,successItemImageView,redeemStampImageView;

	Typeface typeface;
	
	public RedeemDialog(Context context) {
		super(context,R.style.MyDialog);
		this.context = context;
		setRedeemDialog();
		// TODO Auto-generated constructor stub
	}

	private void setRedeemDialog() {
		// TODO Auto-generated method stub
		
	    typeface = Typeface.createFromAsset(context.getAssets(),"hkhbt.ttf");//设置字体
		
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_redeem, null);
		leftButton = (Button)view.findViewById(R.id.redeem_dialog_left);
		rightButton = (Button)view.findViewById(R.id.redeem_dialog_right);
		titleTextView = (TextView)view.findViewById(R.id.redeem_title);
		contenTextView = (TextView)view.findViewById(R.id.redeem_content);
		costTextView = (TextView)view.findViewById(R.id.redeem_score_cost);
		remainTextView = (TextView)view.findViewById(R.id.redeem_remain_score);
		phoneEditText = (EditText)view.findViewById(R.id.redeem_edit_phone);
		nameEditText = (EditText)view.findViewById(R.id.redeem_edit_name);
		addressEditText = (EditText)view.findViewById(R.id.redeem_edit_address);
		redeemProcessLinearLayout = (LinearLayout)view.findViewById(R.id.redeem_dialog_process);
		itemImageView = (ImageView)view.findViewById(R.id.redeem_dialog_image);
		
		contentTempleTextView = (TextView)view.findViewById(R.id.redeem_content_temple);
		consumTempleTextView = (TextView)view.findViewById(R.id.redeem_score_consum);
		consumScoreTempleTextView = (TextView)view.findViewById(R.id.redeem_score_temple);
		remainTempleTextView = (TextView)view.findViewById(R.id.redeem_remain_temple);
		remianScoreTempleTextView = (TextView)view.findViewById(R.id.redeem_remain_score_temple);
		hint1TextView = (TextView)view.findViewById(R.id.redeem_process_hint1);
		hint2TextView = (TextView)view.findViewById(R.id.redeem_process_hint2);
		
		successProTempleTextView = (TextView)view.findViewById(R.id.redeem_success_pro);
		successProInfoTextView = (TextView)view.findViewById(R.id.redeem_success_pro_info);
		sucessRecieveTextView = (TextView)view.findViewById(R.id.redeem_success_reciever);
		successRecieveInfoTextView = (TextView)view.findViewById(R.id.redeem_success_reciever_info);
		sucessPhoneTextView = (TextView)view.findViewById(R.id.redeem_success_phone); 
		successPhoneInfoTextView = (TextView)view.findViewById(R.id.redeem_success_phone_info);
		successAddressTextView = (TextView)view.findViewById(R.id.redeem_success_address);
		successAddressInfoTextView = (TextView)view.findViewById(R.id.redeem_success_address_info);
		successHintTextView = (TextView)view.findViewById(R.id.redeem_success_hint);
		
		redeemSuccessRelativeLayout = (RelativeLayout)view.findViewById(R.id.redeem_dialog_success);
		successItemImageView = (ImageView)view.findViewById(R.id.redeem_success_image);
		
		redeemStampImageView = (ImageView)view.findViewById(R.id.redeem_stamp);
		
		
		leftButton.setTypeface(typeface);
		rightButton.setTypeface(typeface);
		titleTextView.setTypeface(typeface);
		contenTextView.setTypeface(typeface);
		costTextView.setTypeface(typeface);
		remainTextView.setTypeface(typeface);
		phoneEditText.setTypeface(typeface);
		nameEditText.setTypeface(typeface);
		addressEditText.setTypeface(typeface);
		contentTempleTextView.setTypeface(typeface);
		consumTempleTextView.setTypeface(typeface);
		consumScoreTempleTextView.setTypeface(typeface);
		remainTempleTextView.setTypeface(typeface);
		remianScoreTempleTextView.setTypeface(typeface);
		hint1TextView.setTypeface(typeface);
		hint2TextView.setTypeface(typeface);
		successProTempleTextView.setTypeface(typeface);
		successProInfoTextView.setTypeface(typeface);
		sucessRecieveTextView.setTypeface(typeface);
		successRecieveInfoTextView.setTypeface(typeface);
		sucessPhoneTextView.setTypeface(typeface);
		successPhoneInfoTextView.setTypeface(typeface);
		successAddressTextView.setTypeface(typeface);
		successAddressInfoTextView.setTypeface(typeface);
		successHintTextView.setTypeface(typeface);
		
		super.setContentView(view);
		
	}
	
	
	public View getTitle()
	{
		return titleTextView;
	}
	
	public View getConten()
	{
		return contenTextView;
	}
	
	public View getCost(){
		return costTextView;
	}
	
    public View getRemain(){
		return remainTextView;
	}
    
    
	public View getPhone(){
		return phoneEditText;
	}
		
	public View getName(){
		return nameEditText;
	}
	
	public View getAddress(){
		return addressEditText;
	}

	public View getRedeemLayout(){
		return redeemProcessLinearLayout;
	}
	
	public View getLeftButton(){
		return leftButton;
	}
	
	public View getRightButton(){
		return rightButton;
	}
	
	public View getItemImage(){
		return itemImageView;
	}
	
	public View getContentTemple(){
		return contentTempleTextView;
	}
	
	public View getConsumTemple(){
		return consumTempleTextView;
	}
	
	public View getConsumScoreTemple(){
		return consumScoreTempleTextView;
	}
	
	public View getRemainTemple(){
		return remainTempleTextView;
	}
	
	public View getRemainScoreTemple(){
		return remianScoreTempleTextView;
	}
	
	public View getHint1(){
		return hint1TextView;
	}
	
	public View getHint2(){
		return hint2TextView;
	}
	
	public View getPro(){
		return successProTempleTextView;
	}
	
	public View getProInfo(){
		return successProInfoTextView;
	}
	
	public View getSuccessReciever(){
		return sucessRecieveTextView;
	}
	
	public View getRecieverInfo(){
		return successRecieveInfoTextView;
	}
	
	public View getSuccessPhone(){
		return sucessPhoneTextView;
	}
	
	public View getPhoneInfo(){
		return successPhoneInfoTextView;
	}
	
	public View getSuccessAddress(){
		return successAddressTextView;
	}
	
	public View getAddressInfo(){
		return successAddressInfoTextView;
	}
	
	public View getSuccessHint(){
		return successHintTextView;
	}
	
	public View getRedeemSuccess(){
		return redeemSuccessRelativeLayout;
	}
	
	public View getSuccessItemImage(){
		return successItemImageView;
	}
	
	public View getRedeemStamp(){
		return redeemStampImageView;
	}
	
	/**
	 * 左按钮事件
	 * @param listener
	 */
	public void setOnOkListenenr(View.OnClickListener listener) {
		leftButton.setOnClickListener(listener);
	}
	/**
	 * 右按钮事件
	 * @param listener
	 */
	public void setOnNoListenenr(View.OnClickListener listener) {
		rightButton.setOnClickListener(listener);
	}
}
