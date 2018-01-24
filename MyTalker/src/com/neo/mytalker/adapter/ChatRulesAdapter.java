package com.neo.mytalker.adapter;

import java.util.ArrayList;

import com.neo.mytalker.R;
import com.neo.mytalker.entity.ChatDialogEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChatRulesAdapter extends BaseAdapter {

	ArrayList<ChatDialogEntity> mChatDialogEntityList;
	Context mContext;
	
	public ChatRulesAdapter(ArrayList<ChatDialogEntity> mChatDialogEntityListTemp, Context context) {
		mChatDialogEntityList = mChatDialogEntityListTemp;
		mContext = context;
	}
	
	public void updateData(ArrayList<ChatDialogEntity> mChatDialogEntityListTemp) {
//		mChatDialogEntityList = mChatDialogEntityListTemp;
		this.notifyDataSetChanged();
	}
	
	public void addItem(ChatDialogEntity c) {
		mChatDialogEntityList.add(c);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mChatDialogEntityList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mChatDialogEntityList.get(position);
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
		return convertView;
	}

	private void bindViewWithData(int position, View convertView) {
		// TODO Auto-generated method stub
		ViewHolder vh = (ViewHolder)convertView.getTag();
		vh.mQuestion.setText(mChatDialogEntityList.get(position).getQuestion());
		vh.mAnswer.setText(mChatDialogEntityList.get(position).getAnswer());
	}
	private View createView(ViewGroup parent) {
		// TODO Auto-generated method stub
		View convertView;
		ViewHolder vh = new ViewHolder();
		convertView = LayoutInflater.from(mContext).inflate(R.layout.son_of_dialog_list_view_layout, parent, false);
		vh.mQuestion = (TextView) convertView.findViewById(R.id.question_dialog);
		vh.mAnswer = (TextView) convertView.findViewById(R.id.answer_dialog);
		convertView.setTag(vh);
		return convertView;
	}

	public class ViewHolder{
		TextView mQuestion, mAnswer;
	}
}
