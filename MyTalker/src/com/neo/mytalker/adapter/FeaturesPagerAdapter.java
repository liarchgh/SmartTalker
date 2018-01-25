package com.neo.mytalker.adapter;

import java.util.List;

import com.neo.mytalker.fragments.FeaturePageFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;


public class FeaturesPagerAdapter extends FragmentPagerAdapter {
	private List<FeaturePageFragment> mFragments;
	public FeaturesPagerAdapter(FragmentManager fm,List<FeaturePageFragment> vs){
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

