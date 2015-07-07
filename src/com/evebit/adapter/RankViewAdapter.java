package com.evebit.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.whuss.oralanswers.R;
import com.umeng.socialize.net.v;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.opengl.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RankViewAdapter extends BaseAdapter {
	
	
	private Context context;
	private ArrayList<HashMap<String, Object>> list;    //��������Ϣ�б� 
    private LayoutInflater listContainer;           //��ͼ����   
    
    public final class ListItemView{  
    	//�Զ���ؼ�����     
    	public TextView list_rank_name;
    	public TextView list_rank_score;
    	public TextView list_rank_temple;
    	public ImageView list_rank_xingL;
    	public ImageView list_rank_xingR;
          
 }     
	
  public RankViewAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
		// TODO Auto-generated constructor stub	
		this.context=context;
	    this.list=list;	 
	    listContainer = LayoutInflater.from(context);   //������ͼ����������������   
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
    public View getView(int position, View convertView, ViewGroup parent) {   
        // TODO Auto-generated method stub       
        //�Զ�����ͼ   
        ListItemView  listItemView = null;   
        if (convertView == null) {   
            listItemView = new ListItemView();    
            //��ȡlist_item�����ļ�����ͼ   
            convertView = listContainer.inflate(R.layout.list_rank, null);   
            //��ȡ�ؼ�����   
            listItemView.list_rank_name = (TextView)convertView.findViewById(R.id.list_rank_name);   
            listItemView.list_rank_score = (TextView)convertView.findViewById(R.id.list_rank_score);   
            listItemView.list_rank_temple = (TextView)convertView.findViewById(R.id.list_rank_temple);   
            listItemView.list_rank_xingL = (ImageView)convertView.findViewById(R.id.list_rank_xingL);   
            listItemView.list_rank_xingR = (ImageView)convertView.findViewById(R.id.list_rank_xingR);   
           
            //���ÿؼ�����convertView   
            convertView.setTag(listItemView);   
        }else {   
            listItemView = (ListItemView)convertView.getTag();   
        }   

        Typeface typeface;
        typeface = Typeface.createFromAsset(context.getAssets(),"hkhbt.ttf");//��������
       
        listItemView.list_rank_name.setText((String) list.get(position).get("username"));   
        listItemView.list_rank_score.setText((String) list.get(position).get("integral"));   
        
        if (list.get(position).get("state").equals(true)) {
        	listItemView.list_rank_xingL.setVisibility(View.VISIBLE);
        	listItemView.list_rank_xingR.setVisibility(View.VISIBLE);
        	listItemView.list_rank_name.setTextColor(Color.YELLOW);   
            listItemView.list_rank_score.setTextColor(Color.YELLOW);
            listItemView.list_rank_temple.setTextColor(Color.YELLOW);
		}else {
			listItemView.list_rank_xingL.setVisibility(View.GONE);
        	listItemView.list_rank_xingR.setVisibility(View.GONE);
        	listItemView.list_rank_name.setTextColor(Color.WHITE);   
            listItemView.list_rank_score.setTextColor(Color.WHITE); 
            listItemView.list_rank_temple.setTextColor(Color.WHITE);
		}
     
        listItemView.list_rank_name.setTypeface(typeface);
        listItemView.list_rank_score.setTypeface(typeface);
        listItemView.list_rank_temple.setTypeface(typeface);
        
        return convertView;   
    }


	  
}
