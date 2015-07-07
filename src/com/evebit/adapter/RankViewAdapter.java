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
	private ArrayList<HashMap<String, Object>> list;    //户排名信息列表 
    private LayoutInflater listContainer;           //视图容器   
    
    public final class ListItemView{  
    	//自定义控件集合     
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
	    listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文   
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
    public View getView(int position, View convertView, ViewGroup parent) {   
        // TODO Auto-generated method stub       
        //自定义视图   
        ListItemView  listItemView = null;   
        if (convertView == null) {   
            listItemView = new ListItemView();    
            //获取list_item布局文件的视图   
            convertView = listContainer.inflate(R.layout.list_rank, null);   
            //获取控件对象   
            listItemView.list_rank_name = (TextView)convertView.findViewById(R.id.list_rank_name);   
            listItemView.list_rank_score = (TextView)convertView.findViewById(R.id.list_rank_score);   
            listItemView.list_rank_temple = (TextView)convertView.findViewById(R.id.list_rank_temple);   
            listItemView.list_rank_xingL = (ImageView)convertView.findViewById(R.id.list_rank_xingL);   
            listItemView.list_rank_xingR = (ImageView)convertView.findViewById(R.id.list_rank_xingR);   
           
            //设置控件集到convertView   
            convertView.setTag(listItemView);   
        }else {   
            listItemView = (ListItemView)convertView.getTag();   
        }   

        Typeface typeface;
        typeface = Typeface.createFromAsset(context.getAssets(),"hkhbt.ttf");//设置字体
       
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
