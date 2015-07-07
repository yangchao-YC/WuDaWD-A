package com.evebit.ui;




import com.whuss.oralanswers.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MyDialog extends Dialog{
	private Context context;
	private Button okButton,noButton;
	private TextView titleTextView,contenTextView;
	private TextView needTemple1TextView,needTemple2TextView,haveTemple1TextView,haveTemple2TextView,leftTemple1TextView;
	private TextView needTextView,haveTextView,lefTextView;
	private LinearLayout redeemLinearLayout,bgLineagrLayout;	
	private ImageView redeemImageView;
	Typeface typeface;
   
	public MyDialog(Context context) {
		// TODO Auto-generated constructor stub
		super(context,R.style.MyDialog);
		this.context = context;
		setMyDialog();
	}

	private void setMyDialog()
	{
		 typeface = Typeface.createFromAsset(context.getAssets(),"hkhbt.ttf");//设置字体
		View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_exit, null);
		okButton = (Button)view.findViewById(R.id.exit_ok);
		noButton = (Button)view.findViewById(R.id.exit_no);
		titleTextView = (TextView)view.findViewById(R.id.exit_title);
	
		contenTextView = (TextView)view.findViewById(R.id.exit_content);
		redeemLinearLayout = (LinearLayout)view.findViewById(R.id.Redeem_item);
		bgLineagrLayout = (LinearLayout)view.findViewById(R.id.exit_bg);
		redeemImageView = (ImageView)view.findViewById(R.id.Redeem_image);
		needTextView = (TextView)view.findViewById(R.id.Redeem_need_score);
		haveTextView = (TextView)view.findViewById(R.id.Redeem_have_score);
		lefTextView = (TextView)view.findViewById(R.id.Redeem_left_score);
		
		needTemple1TextView = (TextView)view.findViewById(R.id.Redeem_need_temple1);
		needTemple2TextView = (TextView)view.findViewById(R.id.Redeem_need_temple2);
		haveTemple1TextView = (TextView)view.findViewById(R.id.Redeem_have_temple1);
		haveTemple2TextView = (TextView)view.findViewById(R.id.Redeem_have_temple2);
		leftTemple1TextView = (TextView)view.findViewById(R.id.Redeem_left_temple1);
		
		
		okButton.setTypeface(typeface);
		noButton.setTypeface(typeface);
		titleTextView.setTypeface(typeface);
		contenTextView.setTypeface(typeface);
		needTextView.setTypeface(typeface);
		haveTextView.setTypeface(typeface);
		lefTextView.setTypeface(typeface);
		needTemple1TextView.setTypeface(typeface);
		needTemple2TextView.setTypeface(typeface);
		haveTemple1TextView.setTypeface(typeface);
		haveTemple2TextView.setTypeface(typeface);
		leftTemple1TextView.setTypeface(typeface);
		
		super.setContentView(view);
	}
	
	
	/**
	 * 设置title内容
	 * @return
	 */
	public View getTitle()
	{
		return titleTextView;
	}
	
	public View getConten()
	{
		return contenTextView;
	}
	
	public View getOK()
	{
		return okButton;
	}
	
	public View getNo()
	{
		return noButton;
	}
	
	public View getRedeem(){
		return redeemLinearLayout;
	}
	
	public View getImage(){
		return redeemImageView;
	}
	
	public View getBG(){
		return bgLineagrLayout;
	}
	
	public View getNeed(){
		return needTextView;
	}
	
	public View getHave(){
		return haveTextView;
	}
	
	public View getLeft(){
		return lefTextView;
	}
		
	public View getNeedTemple1(){
		return needTemple1TextView;
	}
	
	public View getNeedTemple2(){
		return needTemple2TextView;
	}
	
	public View getHaveTemple1(){
		return haveTemple1TextView;
	}
	
	public View getHaveTemple2(){
		return haveTemple2TextView;
	}
	
	public View getLeftTemple1(){
		return leftTemple1TextView;
	}
	
	

	/**
	 * OK按钮事件
	 * @param listener
	 */
	public void setOnOkListenenr(View.OnClickListener listener) {
		okButton.setOnClickListener(listener);
	}
	/**
	 * NO按钮事件
	 * @param listener
	 */
	public void setOnNoListenenr(View.OnClickListener listener) {
		noButton.setOnClickListener(listener);
	}

}
