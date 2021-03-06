/*******************************************************************
 * titleId代表顺序
 * 0:3D设置
 * 1:缓存设置
 * 2:系统设置
 *******************************************************************/
package com.neo.mytalker.myinterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.neo.mytalker.R;
import com.neo.mytalker.R.drawable;
import com.neo.mytalker.activity.ChatActivity;
import com.neo.mytalker.adapter.SettingAdapter;
import com.neo.mytalker.entity.GlobalSettings;
import com.neo.mytalker.entity.QualitySettingsDialog;
import com.neo.mytalker.entity.SettingEntity;
import com.neo.mytalker.myinterface.ColorPickerDialog.OnColorChangedListener;
import com.neo.mytalker.util.AskAndAnswer;
import com.neo.mytalker.util.ChatRulesManager;
import com.neo.mytalker.util.MusicManager;
import com.neo.mytalker.entity.GlobalSettings;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SettingDialog extends Dialog{
	public static QualitySettingsDialog mQualitySettingsDialog;
	private static final String HINT_DELETE_CHAT_HISTORY = "聊天记录已删除",
			HINT_DELETE_MUSIC_ALL = "音乐缓存已删除";
	
	public SettingDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setCanceledOnTouchOutside(true);
	}

	public SettingDialog(Context context, int themeResId) {
		super(context, themeResId);
		// TODO Auto-generated constructor stub
	}

	public static class Builder implements ThemeInterface{
		private ListView mSettingList;
		private List<SettingEntity> mSettingEntityList;
		private SettingAdapter mSettingAdapter;
		private Context mContext;
		private SettingEntity mSettingEntity;
		public View mView, mSonView;
		private TextView mTextView, mSettingLog;
		public SettingDialog dialog;
		public ColorPickerDialog mColorPickerDialog;
		private ChatActivity mChatActivity;
		
		public Builder(Context context, List<SettingEntity> mSettingEntityListTemp,ChatActivity chatActivity) {
			dialog = new SettingDialog(context, R.style.Dialog);
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mView = inflater.inflate(R.layout.dialog_setting, null);

			mSonView = inflater.inflate(R.layout.son_of_setting_list, null);
			
			mTextView = (TextView) mView.findViewById(R.id.setting_text);
			mSettingLog = (TextView) mSonView.findViewById(R.id.settinglog);
			mChatActivity=chatActivity;
			mSettingList = (ListView) mView.findViewById(R.id.setting_list);
			mContext = mView.getContext();
			initSettingData();
			Collections.sort(mSettingEntityList, new Comparator<SettingEntity>() {

				@Override
				public int compare(SettingEntity lhs, SettingEntity rhs) {
					// TODO Auto-generated method stub
					if(lhs.getTitleId() > rhs.getTitleId()) {
						return 1;
					}else if(lhs.getTitleId() == rhs.getTitleId()) {
						return 0;
					}else {
						return -1;
					}
				}
			});
			mSettingAdapter = new SettingAdapter(mContext, mSettingEntityList);
			mSettingList.setAdapter(mSettingAdapter);
			mSettingList.setOnItemClickListener(new OnItemClickListener() {
				
				

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					switch(position) {
					case 0:
						// TODO:Remove this to enable UNITY
						if (!mChatActivity.SIMPLE_MODE) {
							mChatActivity.mChatMenuFragment.ToggleMenu(false);
							mQualitySettingsDialog = new QualitySettingsDialog.Builder(mContext, mChatActivity)
									.setPositiveButton(new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											mChatActivity.SaveQualityLevel();
											mQualitySettingsDialog.dismiss();
										}
									}).setNegativeButton(new View.OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											mChatActivity.RevertQualityLevel();
											mQualitySettingsDialog.dismiss();
										}
									}).createListDialog();
						}
						mQualitySettingsDialog.show();
						dialog.dismiss();
						break;
					case 1:
						//To 清除聊天历史
//						new ChatRulesManager(mContext, 0).deleteAnswerById(-1);;
						new AskAndAnswer(mContext, 0).deleteAnswerById(-1);
						mChatActivity.mChatRecFrag.CleanRecord();
						Toast.makeText(mContext, HINT_DELETE_CHAT_HISTORY, Toast.LENGTH_LONG).show();
						dialog.dismiss();
						break;
					case 2:
						//To 清除音乐历史
						MusicManager.deleteAllMusicDownloaded();
						Toast.makeText(mContext, HINT_DELETE_MUSIC_ALL, Toast.LENGTH_LONG).show();
						dialog.dismiss();
						break;
					case 3:
/**
						 * 更换主题
						 ***/
						mColorPickerDialog = new ColorPickerDialog(mContext, R.style.Dialog, GlobalSettings.THEME_COLOR, "在圆盘上滑动以选择颜色，点击圆盘中心以确定", new OnColorChangedListener() {
							
							@Override
							public void colorChanged(int color) {
								// TODO Auto-generated method stub
								mChatActivity.ChangeThemeColor(color);
							}
						});
						dialog.dismiss();
						mColorPickerDialog.show();
				//		mColorPickerDialog.getWindow().setBackgroundDrawableResource(Color.WHITE);
						mColorPickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
						break;
					}
				}
			});
			dialog.setContentView(mView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
			ChangeThemeColor();

		}

		public SettingDialog createSettingDialog() {
			return dialog;
		}
	
		private void initSettingData() {
			// TODO Auto-generated method stub
			mSettingEntityList = new ArrayList<SettingEntity>();
			mSettingEntity = new SettingEntity("画质调节", "3D设置", 0, drawable.settings, 0);
			mSettingEntityList.add(mSettingEntity);
			mSettingEntity = new SettingEntity("清除聊天记录", "缓存设置", 1, drawable.settings, 1);
			mSettingEntityList.add(mSettingEntity);
			mSettingEntity = new SettingEntity("清除音乐缓存", "缓存设置", 2, drawable.settings, 1);
			mSettingEntityList.add(mSettingEntity);
			mSettingEntity = new SettingEntity("更换主题", "系统设置", 3, drawable.settings, 2);
			mSettingEntityList.add(mSettingEntity);
			
		}

		public int getBrighterColor(int color) {
			float[] hsv = new float[3];
			Color.colorToHSV(color, hsv);
			hsv[1] = hsv[1] - 0.1f;
			hsv[2] = hsv[2] + 0.1f;
			int brighterColor = Color.HSVToColor(hsv);
			return brighterColor;
		}
	
		@Override
		public void ChangeThemeColor() {
			// TODO Auto-generated method stub
			
			//Toast.makeText(mContext, "" + String.format("#%06X", 0xFFFFFF & GlobalSettings.THEME_COLOR), Toast.LENGTH_SHORT).show();
		//	int brighterColor = getBrighterColor(GlobalSettings.THEME_COLOR);
		//	Toast.makeText(mContext, "" + String.format("#%06X", 0xFFFFFF & brighterColor), Toast.LENGTH_SHORT).show();
		//	mTextView.setBackgroundColor(GlobalSettings.THEME_COLOR);
			((GradientDrawable) mTextView.getBackground()).setColor(GlobalSettings.THEME_COLOR);
		//	mSettingList.setBackgroundColor(GlobalSettings.THEME_COLOR);
		//	mSettingLog.setBackgroundColor(GlobalSettings.THEME_COLOR);
		//	mSettingAdapter.notifyDataSetChanged();
		}
	}
	
}