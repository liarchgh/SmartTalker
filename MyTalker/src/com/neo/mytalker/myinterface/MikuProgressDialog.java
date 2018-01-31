package com.neo.mytalker.myinterface;

import com.neo.mytalker.R;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MikuProgressDialog extends ProgressDialog{
	
	private Context mContext;
	private String mLoadingTip;
	private TextView mTextView;	
	private ImageView mImageView;
	private AnimationDrawable mAnimationDrawable;
	private int mRid;
	
	public MikuProgressDialog(Context context, String content, int id) {
		super(context);
		mContext = context;
		mLoadingTip = content;
		mRid = id;
		setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}
	
	
	public void initView() {
		setContentView(R.layout.dialog_miku_progress);
		mTextView = (TextView) findViewById(R.id.loading_text);
		mImageView = (ImageView) findViewById(R.id.loading_img);
	}
	
	public void initData() {
		mTextView.setText(mLoadingTip);
		mImageView.setBackgroundResource(mRid);
	//	mImageView.setImageResource(R.anim.miku_frame);
		mAnimationDrawable = (AnimationDrawable) mImageView.getBackground();
	//	mAnimationDrawable.start();
		mImageView.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				mAnimationDrawable.start();
			}
		});
		
	}
	
}
