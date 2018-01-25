package com.neo.mytalker.activity;

import java.util.ArrayList;
import java.util.List;

import com.neo.mytalker.R;
import com.neo.mytalker.adapter.FeaturesPagerAdapter;
import com.neo.mytalker.fragments.FeaturePageFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class FeatureShowActivity extends FragmentActivity {
//	Button mQuitAppButton;
	SharedPreferences mSharedPreferences;
	int count;
	Intent myIntent;
	SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_feature);
		
		mSharedPreferences = getSharedPreferences("AppCountss", MODE_PRIVATE);
		count = mSharedPreferences.getInt("count", 0);
		
		
		
		/************************************
		editor = mSharedPreferences.edit();
		editor.putInt("count", 0);
		editor.commit();
		count = mSharedPreferences.getInt("count", 0);
		Log.i("DHJ", String.valueOf(count));
		*************************************/

		if(count == 0) {
			setAnimation();
			editor = mSharedPreferences.edit();
			editor.putInt("count", 1);
			editor.commit();
				
		}else {
			Log.i("DHJ", String.valueOf(count));
			myIntent = new Intent();
			myIntent.setClass(FeatureShowActivity.this, ChatActivity.class);
			startActivity(myIntent);
			this.finish();
		}
	
	}
	
	public void jump2Home(View v) {
		Intent it = new Intent();
		it.setClass(FeatureShowActivity.this, ChatActivity.class);
		FeatureShowActivity.this.startActivity(it);
	}
	
	public void quitApp(View v) {
		this.finish();
	}
	
	public void setAnimation()
	{
		View logo=findViewById(R.id.feature_splash);
		ObjectAnimator animator1 = ObjectAnimator.ofFloat(logo, "alpha", 1f, 0f);
		animator1.setDuration(1000);
		animator1.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				initViewPager();

			}

			@Override
			public void onAnimationStart(Animator animation) {

			}
		});
		animator1.start();
		
	}
	public void initViewPager()
	{
		Log.i("DHJ", String.valueOf(count));
		List<FeaturePageFragment> vs = new ArrayList<FeaturePageFragment>();
		vs.add(new FeaturePageFragment("在这里你可以发送和我的对话内容",R.drawable.feat_1));
		vs.add(new FeaturePageFragment("点击+号打开功能面板\r\n享受更优质的功能服务",R.drawable.feat_2));
		ViewPager vp = (ViewPager)FeatureShowActivity.this.findViewById(R.id.featureShowPages);
		vp.setAdapter(new FeaturesPagerAdapter(getSupportFragmentManager(),vs));
	}
}
