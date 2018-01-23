package com.neo.mytalker.adapter;

import java.util.ArrayList;
import java.util.List;

import com.neo.mytalker.R;
import com.neo.mytalker.entity.MenuFunctionItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ChatMenuAdapter extends BaseAdapter{

	ArrayList<MenuFunctionItem> mMenuFunctionItemList;
	Context mContext;
	
	public ChatMenuAdapter(ArrayList<MenuFunctionItem> list, Context c) {
		mMenuFunctionItemList = list;
		mContext = c;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMenuFunctionItemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mMenuFunctionItemList.get(position);
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
			convertView = creatView(parent);
		}
		bindViewWithData(position, convertView);
		return convertView;
	}
	
	public void addItem(MenuFunctionItem m) {
		mMenuFunctionItemList.add(m);
	}
	
	public void removeItem(int position) {
		mMenuFunctionItemList.remove(position);
	}
	
	private View creatView(ViewGroup parent) {
		// TODO Auto-generated method stub
		View convertView;
		ViewHolder vh = new ViewHolder();
		convertView = LayoutInflater.from(mContext).inflate(R.layout.message_function_layout, parent, false);
		vh.mImageView = (ImageView)convertView.findViewById(R.id.message_function_btn);
		vh.mTextView = (TextView)convertView.findViewById(R.id.message_function_name);
		convertView.setTag(vh);
		return convertView;
	}
	
	private void bindViewWithData(int position, View convertView) {
		ViewHolder vh = (ViewHolder) convertView.getTag();
		vh.mImageView.setImageResource(mMenuFunctionItemList.get(position).getIcon());
		vh.mTextView.setText(mMenuFunctionItemList.get(position).getName());
	}

	class ViewHolder {
		ImageView mImageView;
		TextView mTextView;
	}

}
