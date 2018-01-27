package com.neo.mytalker.fragments;

import com.neo.mytalker.R;
import com.neo.mytalker.activity.ChatActivity;
import com.neo.mytalker.activity.FeatureShowActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FeatureFinalPageFragment extends Fragment {

	private View mRoot;
	ObjectAnimator animator1,animator2,animator3;
	private String mText;
	private TextView desc;
	private FeatureShowActivity mFeatureShowActivity;
	public FeatureFinalPageFragment(String text,FeatureShowActivity featureShowAcitivity)
	{
		mText=text;
		mFeatureShowActivity=featureShowAcitivity;
		Log.i("ZX","InitFrag");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

			mRoot = inflater.inflate(R.layout.fragment_feature_finalpage, container, false);
			FindContent();
			InitContent();
			//InitAnimation();
			Log.i("ZX","CreateView");
			return mRoot;
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		//InitAnimation();
	}
	public void FindContent()
	{
		desc=(TextView) mRoot.findViewById(R.id.feature_description);
	}
	public void InitContent()
	{
		desc.setText(mText);
		
	}
	public void InitAnimation()
	{
		desc.setAlpha(0);
		animator1 = ObjectAnimator.ofFloat(desc, "alpha", 0f, 1f);
		animator1.setDuration(1000);
		animator2 = ObjectAnimator.ofFloat(desc, "alpha", 1f, 1f);
		animator2.setDuration(2000);
		animator3 = ObjectAnimator.ofFloat(desc, "alpha", 1f, 0f);
		animator3.setDuration(1000);
		animator1.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				animator2.start();

			}
		});
		animator2.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				animator3.start();

			}
		});
		animator3.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				Intent myIntent = new Intent();
				myIntent.setClass(mFeatureShowActivity, ChatActivity.class);
				mFeatureShowActivity.startActivity(myIntent);
				mFeatureShowActivity.finish();
			}
		});
		animator1.start();
		Log.i("ZX","InitStart");
	}
}
