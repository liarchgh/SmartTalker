package com.neo.mytalker.adapter;

import java.util.ArrayList;
import java.util.List;

import com.neo.mytalker.R;
import com.neo.mytalker.entity.GlobalSettings;
import com.neo.mytalker.entity.SettingEntity;
import com.neo.mytalker.myinterface.ThemeInterface;

import android.R.drawable;
import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SettingAdapter extends BaseAdapter implements SectionIndexer, ThemeInterface{

	List<SettingEntity> SettingEntityList;
	Context mContext;
	ImageView mImageView;
	TextView mTextView;
	
	public SettingAdapter(Context mContext, List<SettingEntity> SettingEntityList) {
		this.mContext = mContext;
		this.SettingEntityList = SettingEntityList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return SettingEntityList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return SettingEntityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if(convertView == null) {
			convertView = createView(parent);
		}
		bindViewWithData(position, convertView);
		ChangeThemeColor();
		return convertView;
	}
	
	private View createView(ViewGroup parent) {
		// TODO Auto-generated method stub
		View convertView;
		ViewHolder vh = new ViewHolder();
		convertView = LayoutInflater.from(mContext).inflate(R.layout.son_of_setting_list, parent, false);
		vh.mSettingTitle = (TextView) convertView.findViewById(R.id.settinglog);
		vh.mSettingName = (TextView) convertView.findViewById(R.id.setting_name);
		vh.mSettingImage = (ImageView) convertView.findViewById(R.id.setting_img);
		mImageView = vh.mSettingImage;
		mTextView = vh.mSettingTitle;
		convertView.setTag(vh);
		return convertView;
	}
	
	private void bindViewWithData(int position, View convertView) {
		ViewHolder vh = (ViewHolder) convertView.getTag();
		vh.mSettingImage.setImageResource(SettingEntityList.get(position).getImgId());
		vh.mSettingName.setText(SettingEntityList.get(position).getName());
		vh.mSettingTitle.setText(SettingEntityList.get(position).getTitle());
		int sectionIndex = getSectionForPosition(position);
		if(position == getPositionForSection(sectionIndex)) {
			vh.mSettingTitle.setVisibility(View.VISIBLE);
			vh.mSettingTitle.setText(SettingEntityList.get(position).getTitle());
		}else {
			vh.mSettingTitle.setVisibility(View.GONE);
		}
	}
	
	public class ViewHolder{
		ImageView mSettingImage;
		TextView mSettingName;
		TextView mSettingTitle;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		// TODO Auto-generated method stub
		for(int i = 0; i<getCount(); i++) {
			int temp = SettingEntityList.get(i).getTitleId();
			if(temp == sectionIndex) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return SettingEntityList.get(position).getTitleId();
	}

	@Override
	public void ChangeThemeColor() {
		// TODO Auto-generated method stub
		int brighterColor = getBrighterColor(GlobalSettings.THEME_COLOR);
		mImageView.setBackgroundColor(GlobalSettings.THEME_COLOR);
		mTextView.setBackgroundColor(brighterColor);
	}
	
	public int getBrighterColor(int color) {
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);
		hsv[1] = hsv[1] - 0.3f;
		hsv[2] = hsv[2] + 0.3f;
		int brighterColor = Color.HSVToColor(hsv);
		return brighterColor;
	}
}
