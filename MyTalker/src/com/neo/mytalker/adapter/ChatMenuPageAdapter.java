package com.neo.mytalker.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class ChatMenuPageAdapter extends PagerAdapter {

	List<View> mViewList;
	
	public ChatMenuPageAdapter(List<View> mV) {
		mViewList = mV;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mViewList.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeViewAt(position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(mViewList.get(position));
		return mViewList.get(position);
	}

	
}
