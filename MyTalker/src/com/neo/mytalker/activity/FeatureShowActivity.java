package com.neo.mytalker.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.neo.mytalker.R;
import com.neo.mytalker.adapter.FeaturesPagerAdapter;
import com.neo.mytalker.fragments.FeatureFinalPageFragment;
import com.neo.mytalker.fragments.FeaturePageFragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;

public class FeatureShowActivity extends FragmentActivity {
	// Button mQuitAppButton;
	SharedPreferences mSharedPreferences;
	int count;
	Intent myIntent;
	SharedPreferences.Editor editor;
	List<Fragment> mVs;
	ViewPager mVp;
	FeaturesPagerAdapter mFpa;
	Timer mTimer;
	boolean isAuto = true;

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

		if (count == 0) {
			setAnimation();
			editor = mSharedPreferences.edit();
			editor.putInt("count", 1);
			editor.commit();
			// initViewPager();
		} else {
//			Log.i("DHJ", String.valueOf(count));
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

	public void setAnimation() {
		View logo = findViewById(R.id.feature_splash);
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

	public void initViewPager() {
//		Log.i("DHJ", String.valueOf(count));
		mVs = new ArrayList<Fragment>();
		mVs.add(new FeaturePageFragment("在这里你可以发送和我的对话内容", R.drawable.feat_1));
		mVs.add(new FeaturePageFragment("点击+号打开功能面板\r\n享受更优质的功能服务", R.drawable.feat_2));
		mVs.add(new FeatureFinalPageFragment("让我们开始吧", this));
		mVp = (ViewPager) findViewById(R.id.feature_show_pages);
		mFpa = new FeaturesPagerAdapter(getSupportFragmentManager(), mVs);
		mVp.setAdapter(mFpa);
		mVp.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				isAuto = false;
				mTimer.cancel();
				return false;
			}

		});
		mVp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				if (mVp.getCurrentItem() < mVs.size()-1) {
					if (isAuto) {

						((FeaturePageFragment) mVs.get(arg0)).InitAnimation();
					}
				} else {
					((FeatureFinalPageFragment) mVs.get(arg0)).InitAnimation();
				}
			}
		});
		isAuto = true;
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if (mVp.getCurrentItem() < mVp.getChildCount()) {
							mVp.setCurrentItem(mVp.getCurrentItem() + 1);
						} else {
							mTimer.cancel();
							isAuto = false;
						}
					}
				});

			}
		}, 5000, 5000);
		((FeaturePageFragment) mVs.get(0)).InitAnimation();
	}
}
