package com.neo.mytalker.activity;

import java.util.ArrayList;
import java.util.List;

import com.neo.mytalker.R;
import com.neo.mytalker.adapter.FeatureShowPageAdapter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class FeatureShowActivity extends Activity {
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
		
		
		
		/************************************/
		editor = mSharedPreferences.edit();
		editor.putInt("count", 0);
		editor.commit();
		count = mSharedPreferences.getInt("count", 0);
		Log.i("DHJ", String.valueOf(count));
		/*************************************/

		if(count == 0) {
			editor = mSharedPreferences.edit();
			editor.putInt("count", 1);
			editor.commit();
			Log.i("DHJ", String.valueOf(count));
			List<View>vs = new ArrayList<View>();
			LayoutInflater lif = FeatureShowActivity.this.getLayoutInflater();
			View v = lif.inflate(R.layout.single_image, null, false);
			Resources resources = FeatureShowActivity.this.getResources();
			Drawable drawable = resources.getDrawable(R.drawable.f0);
			((ImageView)v.findViewById(R.id.imageShow)).setImageDrawable(drawable);
			vs.add(v);

			v = lif.inflate(R.layout.single_image, null, false);
			drawable = resources.getDrawable(R.drawable.f1);
			((ImageView)v.findViewById(R.id.imageShow)).setImageDrawable(drawable);
			vs.add(v);

			v = lif.inflate(R.layout.single_image, null, false);
			drawable = resources.getDrawable(R.drawable.f2);
			((ImageView)v.findViewById(R.id.imageShow)).setImageDrawable(drawable);
			v.findViewById(R.id.jump_main_buttons).setVisibility(View.VISIBLE);
			v.findViewById(R.id.page_end).setVisibility(View.VISIBLE);
			vs.add(v);
			
			ViewPager vp = (ViewPager)FeatureShowActivity.this.findViewById(R.id.featureShowPages);
			vp.setAdapter(new FeatureShowPageAdapter(vs));
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
}
