package com.neo.mytalker.fragments;

import com.neo.mytalker.R;
import android.content.Context;
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

public class FeaturePageFragment extends Fragment {

	private View mRoot;
	ObjectAnimator animator1,animator2;
	private String mText;
	private TextView desc;
	private ImageView bg;
	private int mResId;
	private Animator animator4;
	private ObjectAnimator animator3;
	public FeaturePageFragment(String text,int resId)
	{
		mText=text;
		mResId=resId;
//		Log.i("ZX","InitFrag");
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

			mRoot = inflater.inflate(R.layout.fragment_feature_page1, container, false);
			FindContent();
			InitContent();
			//InitAnimation();
//			Log.i("ZX","CreateView");
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
		bg=(ImageView) mRoot.findViewById(R.id.feature_bg);
		desc=(TextView) mRoot.findViewById(R.id.feature_description);
	}
	public void InitContent()
	{
		bg.setImageResource(mResId);
		desc.setText(mText);
		
	}
	public void InitAnimation()
	{
		desc.setAlpha(0);
		bg.setAlpha(0f);
		animator1 = ObjectAnimator.ofFloat(bg, "alpha", 0f, 1f);
		animator1.setDuration(1000);
		animator2 = ObjectAnimator.ofFloat(desc, "alpha", 0f, 1f);
		animator2.setDuration(1000);
		animator3 = ObjectAnimator.ofFloat(mRoot, "alpha", 1f, 1f);
		animator3.setDuration(2000);
		animator4 = ObjectAnimator.ofFloat(mRoot, "alpha", 1f, 0f);
		animator4.setDuration(1000);
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
				animator4.start();

			}
		});
		animator4.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				mRoot.setAlpha(1);

			}
		});
		animator1.start();
//		Log.i("ZX","InitStart");
	}
}
