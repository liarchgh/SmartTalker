package com.neo.mytalker.adapter;

import java.util.List;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class FeaturesPagerAdapter extends FragmentPagerAdapter {
	private List<Fragment> mFragments;
	public FeaturesPagerAdapter(FragmentManager fm,List<Fragment> vs){
		super(fm);
		// TODO Auto-generated constructor stub
		mFragments=vs;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return mFragments.get(arg0);	
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mFragments.size();
	}

}

