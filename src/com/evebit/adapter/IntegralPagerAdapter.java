package com.evebit.adapter;

import java.util.ArrayList;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class IntegralPagerAdapter extends PagerAdapter {
	
	private ArrayList<View> pageViews;
	
	public IntegralPagerAdapter(ArrayList<View>pageViews){
		this.pageViews = pageViews;
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return pageViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub
		((ViewPager) container).removeView(pageViews.get(position));
	}

	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return super.getItemPosition(object);
	}

	@Override
	public Parcelable saveState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void finishUpdate(View container) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startUpdate(View container) {
		// TODO Auto-generated method stub
	
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		((ViewPager) container).addView(pageViews.get(position)); 
        return pageViews.get(position); 
	}
	
	
	
	
	

}
