package com.neo.mytalker.entity;

import java.util.List;

import com.neo.mytalker.R;
import com.neo.mytalker.activity.ChatActivity;
import com.neo.mytalker.adapter.MusicItemAdapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QualitySettingsDialog extends Dialog {
	public QualitySettingsDialog(Context context) {
		super(context);
		setCanceledOnTouchOutside(true);
	}

	public QualitySettingsDialog(Context context, int themeResId) {
		super(context, themeResId);
	}

	public static class Builder {
		private ListView mMusicList;
		private List<MusicItemData> mMusicItemList;
		private MusicItemAdapter mMusicListAdapter;
		private Context mContext;
		public View mView;
		public QualitySettingsDialog dialog;
		private android.view.View.OnClickListener positiveButtonClickListener;
		private android.view.View.OnClickListener negativeButtonClickListener;
		private ChatActivity mChatActivity;
		private int oldQualitySettings = 3;
		private ImageView mDown;
		private ImageView mUp;
		private String[] mQualityStrings = { "极低画质", "低画质", "中画质", "高画质", "超高画质", "极高画质", };
		private TextView mText;
		private View mAccept;
		private View mDecline;
		private View mAutoHigh;
		private View mAutoLow;

		public Builder(Context context, ChatActivity chatActivity) {
			dialog = new QualitySettingsDialog(context, R.style.DialogNotDim);
			mChatActivity = chatActivity;
			mChatActivity.SaveOldQualityLevel();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mView = inflater.inflate(R.layout.dialog_qualitysettings, null);
			View mTopBar = mView.findViewById(R.id.quality_topbar);
			mTopBar.setBackgroundColor(GlobalSettings.THEME_COLOR);
			View mTopBar2 = mView.findViewById(R.id.quality_topbar2);
			mTopBar2.setBackgroundColor(GlobalSettings.THEME_COLOR);
			mText = (TextView) mView.findViewById(R.id.quality_now);
			mText.setText(mQualityStrings[mChatActivity.getCurrentQualityLevel()]);
			mDown = (ImageView) mView.findViewById(R.id.quality_down);
			mDown.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mChatActivity.getCurrentQualityLevel() > 0) {
						mChatActivity.SetQuality(mChatActivity.getCurrentQualityLevel() - 1);
					}
					if (mChatActivity.getCurrentQualityLevel() == 0) {
						mDown.setColorFilter(Color.GRAY);
					}
					mText.setText(mQualityStrings[mChatActivity.getCurrentQualityLevel()]);
					mUp.clearColorFilter();
				}

			});

			mUp = (ImageView) mView.findViewById(R.id.quality_up);

			mUp.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (mChatActivity.getCurrentQualityLevel() < 5) {
						mChatActivity.SetQuality(mChatActivity.getCurrentQualityLevel() + 1);
					}
					if (mChatActivity.getCurrentQualityLevel() == 5) {
						mUp.setColorFilter(Color.GRAY);
					}
					mText.setText(mQualityStrings[mChatActivity.getCurrentQualityLevel()]);
					mDown.clearColorFilter();

				}

			});

			
			mAutoHigh = mView.findViewById(R.id.quality_autohigh);
			mAutoHigh.setBackgroundColor(Color.WHITE);
			mAutoHigh.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mChatActivity.AutoAdjust(5,mText);
				}
			});
			
			mAutoLow = mView.findViewById(R.id.quality_autolow);
			mAutoLow.setBackgroundColor(Color.WHITE);
			mAutoLow.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mChatActivity.AutoAdjust(2,mText);
				}
			});
			
			
			
			mAccept = mView.findViewById(R.id.quality_accept);
			mAccept.setBackgroundColor(GlobalSettings.THEME_COLOR);

			
			mDecline = mView.findViewById(R.id.quality_decline);
			

			float dp2px = context.getResources().getDisplayMetrics().density;
			// lp.setMargins((int)(40*dp2px), (int)(40*dp2px), (int)(40*dp2px),
			// (int)(40*dp2px));
			DisplayMetrics dm = new DisplayMetrics();
			dm = context.getResources().getDisplayMetrics();
			float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
			int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）
			int screenWidth = dm.widthPixels; // 屏幕宽（像素，如：3200px）
			int screenHeight = dm.heightPixels; // 屏幕高（像素，如：1280px）
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(screenWidth, screenHeight);

			dialog.addContentView(mView, lp);
		}

		public QualitySettingsDialog createListDialog() {
			mAccept.setOnClickListener(positiveButtonClickListener);
			mDecline.setOnClickListener(negativeButtonClickListener);
			return dialog;
		}

		public Builder setPositiveButton(View.OnClickListener listener) {

			this.positiveButtonClickListener = listener;
			
			return this;
		}

		public Builder setNegativeButton(View.OnClickListener listener) {
			this.negativeButtonClickListener = listener;
			
			return this;
		}
	}
}
