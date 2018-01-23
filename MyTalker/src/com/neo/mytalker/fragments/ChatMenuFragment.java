package com.neo.mytalker.fragments;

import java.util.ArrayList;

import com.neo.mytalker.R;
import com.neo.mytalker.R.drawable;
import com.neo.mytalker.adapter.ChatMenuAdapter;
import com.neo.mytalker.adapter.ChatMenuPageAdapter;
import com.neo.mytalker.entity.MenuFunctionItem;
import com.neo.mytalker.myinterface.FunctionOnClickListener;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChatMenuFragment extends Fragment implements OnItemClickListener{
	TextView mSendTextView, mChooseMoreFunBtn;
	OnClickListener mOnClickListener;
	View mMessagePlusFun, mView;
	ArrayList<View> mPagerView;
	ArrayList<ArrayList<MenuFunctionItem>> mFunctionListItemList = null;
	ChatMenuAdapter mChatMenuAdapter;
	GridView mGridView;
	ArrayList<ChatMenuAdapter> mChatMenuAdapterList;
	OnItemClickListener mOnItemClickListener;
	ArrayList<ImageView> mPointViews;
	LinearLayout mLayoutPoint;
	ViewPager mViewPager;
	int current;//当前的point
	OnPageChangeListener mOnPageChangeListener;
	FunctionOnClickListener mFunctionOnClickListener;
	ViewGroup mViewGroup;
	Context mContext;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mView = inflater.inflate(R.layout.activity_chat_menu, container, false);
		initData();
		initView(inflater, container);
		initFunctionViewPager();
		initPointView();
		initFunctionData();
		return mView;
	}

	public void initData() {
		mFunctionListItemList = new ArrayList<ArrayList<MenuFunctionItem>>();
		ArrayList<MenuFunctionItem> list = new ArrayList<MenuFunctionItem>();
		int[] funImgId = new int[] {R.drawable.chat_tool_photo,
				R.drawable.chat_tool_send_file, R.drawable.chat_tool_camera,
				R.drawable.chat_tool_funny_face};
		String[] funName = new String[] {"规则管理","音乐搜索","使用帮助","系统设置"};
		for(int i = 0; i<4; i++) {
			MenuFunctionItem entity = new MenuFunctionItem();
			entity.setIcon(funImgId[i]);
			entity.setName(funName[i]);
			entity.setPosition(i);
			list.add(entity);
		}
		mFunctionListItemList.add(list);
	}
	
	public void initView(LayoutInflater inflater, ViewGroup container) {
		mMessagePlusFun = inflater.inflate(R.layout.activity_chat_menu, container, false);
		mContext = mMessagePlusFun.getContext();
		mLayoutPoint = (LinearLayout) mMessagePlusFun.findViewById(R.id.mp_image);
		mViewPager = (ViewPager) mMessagePlusFun.findViewById(R.id.message_plus_viewpager);
	}

/*	public void initSetOnClick() {
		mSendTextView.setOnClickListener(this);
		mChooseMoreFunBtn.setOnClickListener(this);
	}
	*/
	/*@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()) {
			case R.id.chat_bottombar_send:
			//	Toast.makeText(this, "hahahaha", Toast.LENGTH_LONG);
				mEditChatText.setText("");
				if(mMessagePlusFun.getVisibility() == View.VISIBLE) {
					Log.i("getVisibility", "send" + "");
					mMessagePlusFun.setVisibility(View.GONE);
				}
				break;
			case R.id.chat_bottombar_morefun:
				initFunctionViewPager();
				initPointView();
				initFunctionData();
				if(mMessagePlusFun.getVisibility() == View.VISIBLE) {
					Log.i("getVisibility", "more" + "");
					mMessagePlusFun.setVisibility(View.GONE);
				}else {
					Log.i("getVisibility", "more" + "");
					mMessagePlusFun.setVisibility(View.VISIBLE);
				}
				break;
		}
	}*/

	private void initFunctionViewPager() {
		// TODO Auto-generated method stub
		mPagerView = new ArrayList<View>();
		View nullViewLeft = new View(mContext);
		nullViewLeft.setBackgroundColor(Color.TRANSPARENT);
		mPagerView.add(nullViewLeft);
		
		mChatMenuAdapterList = new ArrayList<ChatMenuAdapter>();
		for(int i = 1; i<mFunctionListItemList.size(); i++) {
			mGridView = new GridView(mContext);
			mChatMenuAdapter = new ChatMenuAdapter(mFunctionListItemList.get(i), mContext);
			mGridView.setAdapter(mChatMenuAdapter);
			mGridView.setOnItemClickListener(this);
			mGridView.setNumColumns(4);
			mGridView.setBackgroundColor(Color.TRANSPARENT);
			mGridView.setHorizontalSpacing(1);
			mGridView.setVerticalSpacing(1);
			mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			mGridView.setCacheColorHint(0);
			mGridView.setPadding(5, 0, 5, 0);
			mGridView.setSelector(new ColorDrawable(Color.GRAY));
			mGridView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			mGridView.setGravity(Gravity.CENTER);
			mPagerView.add(mGridView);
		}
		View nullViewRight = new View(mContext);
		nullViewRight.setBackgroundColor(Color.TRANSPARENT);
		mPagerView.add(nullViewRight);
	}
	
	private void initPointView() {
		// TODO Auto-generated method stub
		mPointViews = new ArrayList<ImageView>();
		mLayoutPoint.removeAllViews();
		ImageView imageView;
		for(int i = 0; i<mPagerView.size(); i++) {
			imageView = new ImageView(mContext);
			imageView.setBackgroundResource(drawable.d1);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			layoutParams.width = 8;
			layoutParams.height = 8;
			mLayoutPoint.addView(imageView, layoutParams);
			if(i==0 ||i==mPagerView.size()-1) {
				imageView.setVisibility(View.GONE);
			}
			if(i==1) {
				imageView.setBackgroundResource(R.drawable.d2);
			}
			mPointViews.add(imageView);
		}
	}

	private void initFunctionData() {
		// TODO Auto-generated method stub
		mViewPager.setAdapter(new ChatMenuPageAdapter(mPagerView));
		mViewPager.setCurrentItem(1);
		current = 0;
		mViewPager.setOnPageChangeListener(mOnPageChangeListener);
		mOnPageChangeListener = new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int index) {
				// TODO Auto-generated method stub
				current = index - 1;
				drawPoint(index);
				if(index == 0 || index == mPagerView.size()-1) {
					if(index == 0) {
						mViewPager.setCurrentItem(index+1);
						mPointViews.get(index).setBackgroundResource(drawable.d1);
					}else {
						mViewPager.setCurrentItem(index-1);
						mPointViews.get(index).setBackgroundResource(drawable.d2);
					}
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		};
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		MenuFunctionItem item = mFunctionListItemList.get(current).get(position);
		if(mFunctionOnClickListener != null) {
			mFunctionOnClickListener.onClick(item);
		}
	}
	
	public void drawPoint(int index) {
		for(int i = 1; i<mPointViews.size(); i++) {
			if(i == index) {
				mPointViews.get(i).setBackgroundResource(R.drawable.d2);
			}else {
				mPointViews.get(i).setBackgroundResource(R.drawable.d1);
			}
		}
	}
	
}
