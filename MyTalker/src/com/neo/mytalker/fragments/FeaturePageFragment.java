package com.neo.mytalker.fragments;

import com.neo.mytalker.R;
import com.neo.mytalker.activity.ChatActivity;
import com.neo.mytalker.util.ChatWithTalker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FeaturePageFragment extends Fragment {

	private View mRoot;
	ObjectAnimator animator1,animator2;
	private String mText;
	private TextView desc;
	private ImageView bg;
	private int mResId;
	public FeaturePageFragment(String text,int resId)
	{
		mText=text;
		mResId=resId;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub

			LayoutInflater mLayoutInflater = inflater;
			mRoot = inflater.inflate(R.layout.fragment_feature_page1, container, false);
			Context mContext = container.getContext();
			FindContent();
			InitContent();
			InitAnimation();
			return mRoot;
	}
	public void FindContent()
	{
		bg=(ImageView) mRoot.findViewById(R.id.feature_bg);
		desc=(TextView) mRoot.findViewById(R.id.feature_description);
	}
	public void InitContent()
	{
//		bg.setImageResource(mResId);
//		desc.setText(mText);
	}
	public void InitAnimation()
	{
		animator1 = ObjectAnimator.ofFloat(bg, "alpha", 0f, 1f);
		animator1.setDuration(1000);
		animator2 = ObjectAnimator.ofFloat(desc, "alpha", 0f, 1f);
		animator2.setDuration(1000);
		animator1.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				animator2.start();

			}
		});
		animator1.start();
		Log.i("ZX","InitStart");
	}
	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return mRoot;
	}
	
}
