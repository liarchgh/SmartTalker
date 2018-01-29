package com.neo.mytalker.fragments;

import java.util.ArrayList;

import com.neo.mytalker.R;
import com.neo.mytalker.R.drawable;
import com.neo.mytalker.activity.ChatActivity;
import com.neo.mytalker.activity.ChatRulesActivity;
import com.neo.mytalker.activity.HelpActivity;
import com.neo.mytalker.adapter.ChatMenuAdapter;
import com.neo.mytalker.adapter.ChatMenuPageAdapter;
import com.neo.mytalker.entity.GlobalSettings;
import com.neo.mytalker.entity.MenuFunctionItem;
import com.neo.mytalker.entity.MusicItemData;
import com.neo.mytalker.entity.MusicPlayerDialog;
import com.neo.mytalker.entity.QualitySettingsDialog;
import com.neo.mytalker.myinterface.ThemeInterface;
import com.neo.mytalker.myinterface.MikuProgressDialog;
import com.neo.mytalker.myinterface.SettingDialog;
import android.accounts.Account;
import android.accounts.OnAccountsUpdateListener;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChatMenuFragment extends Fragment implements OnItemClickListener, ThemeInterface {
	TextView mSendTextView, mChooseMoreFunBtn;
	OnClickListener mOnClickListener;
	View mView;
	ArrayList<View> mPagerView;
	ArrayList<ArrayList<MenuFunctionItem>> mFunctionListItemList = null;
	ChatMenuAdapter mChatMenuAdapter;
	GridView mGridView;
	ArrayList<ChatMenuAdapter> mChatMenuAdapterList;
	OnItemClickListener mOnItemClickListener;
	ArrayList<ImageView> mPointViews;
	LinearLayout mLayoutPoint;
	ViewPager mViewPager;
	int current;// 当前的point
	OnPageChangeListener mOnPageChangeListener;
	ViewGroup mViewGroup;
	Context mContext;
	ChatActivity mChatActivity;
	Intent intent;
	private ImageView mMoreBtn;
	private boolean isMenuOn;
	MusicPlayerDialog.Builder mBuilder;
	MusicPlayerDialog mMusicPlayerDialog;
	public QualitySettingsDialog mQualitySettingsDialog;
    MikuProgressDialog mMikuProgressDialog;
	SettingDialog mSettingDialog;
	SettingDialog.Builder mSettingBuilder;
	public ChatMenuFragment(ChatActivity activity) {
		mChatActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return init(inflater, container);
	}

	public View init(LayoutInflater inflater, ViewGroup container) {
		mView = inflater.inflate(R.layout.fragment_chat_menu, container, false);
		mViewGroup = container;
		initData();
		initView();
		initFunctionViewPager();
		initPointView();
		initFunctionData();
		return mView;
	}

	public void initData() {
		mFunctionListItemList = new ArrayList<ArrayList<MenuFunctionItem>>();
		ArrayList<MenuFunctionItem> list = new ArrayList<MenuFunctionItem>();
		int[] funImgId = new int[] { R.drawable.rules, R.drawable.music, R.drawable.dance, R.drawable.help,
				R.drawable.settings, R.drawable.settings };
		String[] funName = new String[] { "规则管理", "音乐搜索", "极乐净土", "使用帮助", "系统设置"};
		for (int i = 0; i < funName.length; i++) {
			MenuFunctionItem entity = new MenuFunctionItem();
			entity.setIcon(funImgId[i]);
			entity.setName(funName[i]);
			entity.setPosition(i);
			list.add(entity);
		}
		mFunctionListItemList.add(list);
	}

	public void initView() {
		mContext = mView.getContext();
		mLayoutPoint = (LinearLayout) mView.findViewById(R.id.mp_image);
		mViewPager = (ViewPager) mView.findViewById(R.id.message_plus_viewpager);
	}

	private void initFunctionViewPager() {
		// TODO Auto-generated method stub
		mPagerView = new ArrayList<View>();
		View nullViewLeft = new View(mContext);
		nullViewLeft.setBackgroundColor(Color.WHITE);
		mPagerView.add(nullViewLeft);

		mChatMenuAdapterList = new ArrayList<ChatMenuAdapter>();
		for (int i = 0; i < mFunctionListItemList.size(); i++) {
			mGridView = new GridView(mContext);
			mGridView.setOnItemClickListener(this);
			mGridView.setNumColumns(4);
			mGridView.setBackgroundColor(Color.WHITE);
			mGridView.setHorizontalSpacing(1);
			mGridView.setSelector(new ColorDrawable(Color.BLUE));
			mGridView.setVerticalSpacing(1);
			mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			mGridView.setCacheColorHint(0);
			mGridView.setPadding(5, 5, 5, 5);
			mGridView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			mGridView.setGravity(Gravity.CENTER);
			mChatMenuAdapter = new ChatMenuAdapter(mFunctionListItemList.get(i), mContext);
			mGridView.setAdapter(mChatMenuAdapter);
			mChatMenuAdapterList.add(mChatMenuAdapter);
			mPagerView.add(mGridView);
		}

		View nullViewRight = new View(mContext);
		nullViewRight.setBackgroundColor(Color.WHITE);
		mPagerView.add(nullViewRight);
	}

	private void initPointView() {
		// TODO Auto-generated method stub
		mPointViews = new ArrayList<ImageView>();
		mLayoutPoint.removeAllViews();
		ImageView imageView;
		for (int i = 0; i < mPagerView.size(); i++) {
			imageView = new ImageView(mContext);
			imageView.setBackgroundResource(drawable.d1);
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			layoutParams.leftMargin = 10;
			layoutParams.rightMargin = 10;
			layoutParams.width = 8;
			layoutParams.height = 8;
			mLayoutPoint.addView(imageView, layoutParams);
			if (i == 0 || i == mPagerView.size() - 1) {
				imageView.setVisibility(View.GONE);
			}
			if (i == 1) {
				imageView.setBackgroundResource(R.drawable.d2);
			}
			mPointViews.add(imageView);
		}
	}

	@Override
	public View getView() {
		// TODO Auto-generated method stub
		return mView;
	}

	private void initFunctionData() {
		// TODO Auto-generated method stub
		mViewPager.setAdapter(new ChatMenuPageAdapter(mPagerView));
		mViewPager.setCurrentItem(1);
		current = 0;
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				// TODO Auto-generated method stub
				current = index - 1;
				drawPoint(index);
				if (index == 0 || index == mPagerView.size() - 1) {
					if (index == 0) {
						mViewPager.setCurrentItem(index + 1);
						mPointViews.get(index).setBackgroundResource(drawable.d1);
					} else {
						mViewPager.setCurrentItem(index - 1);
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
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		MenuFunctionItem item = mFunctionListItemList.get(current).get(position);
		switch (position) {
		case 0:
			mMikuProgressDialog = new MikuProgressDialog(mContext, "正在加载中...");
			mMikuProgressDialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					jumpToRulesActivity();
					handler.sendEmptyMessage(0);
				}
				
			}).start();
			break;
		case 1:
			ToggleMenu(false);
			mMusicPlayerDialog = new MusicPlayerDialog.Builder(mContext, null).createListDialog();
			mMusicPlayerDialog.show();
			mChatActivity.mChatBarFrag.SetText("如何播放音乐");
			mChatActivity.mChatBarFrag.SendText();
			mChatActivity.mChatBarFrag.SetText("播放音乐");
			break;
		case 2:
			ToggleMenu(false);
			mChatActivity.mChatBarFrag.SetText("跳支舞吧");
			mChatActivity.mChatBarFrag.SendText();
			mChatActivity.Dance();
			break;
		case 3:
			mMikuProgressDialog = new MikuProgressDialog(mContext, "正在加载中...");
			mMikuProgressDialog.show();
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					jumpToHelpWebView();
					handler.sendEmptyMessage(0);
				}
				
			}).start();
			break;
		case 4:
/*jumpToSettingActivity();*/
			mSettingDialog = new SettingDialog.Builder(mContext, null,mChatActivity).createSettingDialog();
			mSettingDialog.show();
			break;

		}
	}
/***
	 * 跳转到规则管理界面
	 * **/
	public void jumpToRulesActivity() {
		intent = new Intent();
		intent.setClass(mContext, ChatRulesActivity.class);
		startActivity(intent);
	}
	
	/***
	 * 跳转到使用帮助界面
	 * **/
	public void jumpToHelpWebView() {
		intent = new Intent();
		intent.setClass(mContext, HelpActivity.class);
		startActivity(intent);
	}
	
	/***
	 * 跳转到使用帮助界面
	 * **/
	/*public void jumpToSettingActivity() {
		intent = new Intent();
		intent.setClass(mContext, SettingActivity.class);
		startActivity(intent);
	}*/
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg.what == 0) {
				mMikuProgressDialog.dismiss();
			}
		}
	};
	public void drawPoint(int index) {
		for (int i = 1; i < mPointViews.size(); i++) {
			if (i == index) {
				mPointViews.get(i).setBackgroundResource(R.drawable.d2);
			} else {
				mPointViews.get(i).setBackgroundResource(R.drawable.d1);
			}
		}
	}

	/// Hide or show
	public void ToggleMenu(boolean status) {
		// Toast.makeText(mContext, "toggle"+status, Toast.LENGTH_LONG).show();
		if (status) {
			mViewGroup.setVisibility(View.VISIBLE);
			mViewPager.setVisibility(View.VISIBLE);
			isMenuOn = true;

		} else {
			mViewPager.setVisibility(View.GONE);
			mViewGroup.setVisibility(View.GONE);
			isMenuOn = false;
		}
		if (mMoreBtn != null) {
			if (isMenuOn) {
				mMoreBtn.setImageResource(R.drawable.ic_remove_white_48dp);
			} else {
				mMoreBtn.setImageResource(R.drawable.ic_add_white_48dp);
			}

		}
	}

	public void ToggleMenu() {
		isMenuOn = !isMenuOn;
		ToggleMenu(isMenuOn);
	}

	public void ToggleMenu(boolean status, ImageView text) {
		// Toast.makeText(mContext, "toggle"+status, Toast.LENGTH_LONG).show();
		mMoreBtn = text;
		ToggleMenu(status);
	}

	public void ToggleMenu(ImageView text) {
		// Toast.makeText(mContext, "toggle"+status, Toast.LENGTH_LONG).show();
		mMoreBtn = text;
		ToggleMenu();
	}

	@Override
	public void ChangeThemeColor() {
		for (ChatMenuAdapter c : mChatMenuAdapterList) {
			c.notifyDataSetChanged();
		}
	}
}
