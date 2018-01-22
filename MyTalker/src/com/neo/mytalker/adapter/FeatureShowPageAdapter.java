package com.neo.mytalker.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public class FeatureShowPageAdapter extends PagerAdapter {
	List<View>vs = null;
	
	public FeatureShowPageAdapter(List<View>vs) {
		this.vs = vs;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return vs.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(vs.get(position));
//		super.destroyItem(container, position, object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		View v = vs.get(position);
		container.addView(v);
//		return super.instantiateItem(container, position);
		return v;
	}
}
