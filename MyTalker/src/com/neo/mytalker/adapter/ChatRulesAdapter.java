package com.neo.mytalker.adapter;

import java.util.ArrayList;
import java.util.List;

import com.neo.mytalker.R;
import com.neo.mytalker.entity.ChatDialogEntity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ChatRulesAdapter extends BaseAdapter implements SectionIndexer{

	List<ChatDialogEntity> mChatDialogEntityList;
	Context mContext;
	
	public ChatRulesAdapter(List<ChatDialogEntity> mChatDialogEntityListTemp, Context context) {
		mChatDialogEntityList = mChatDialogEntityListTemp;
		mContext = context;
	}
	
	public void updateData(List<ChatDialogEntity> mChatDialogEntityListTemp) {
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
		return mChatDialogEntityList.get(position).getId();
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
		int sectionIndex = getSectionForPosition(position);
		if(position == getPositionForSection(sectionIndex)) {
			vh.mCatalog.setVisibility(View.VISIBLE);
			vh.mCatalog.setText(mChatDialogEntityList.get(position).getSortLetters());
		}else {
			vh.mCatalog.setVisibility(View.GONE);
		}
	}
	
	private View createView(ViewGroup parent) {
		// TODO Auto-generated method stub
		View convertView;
		ViewHolder vh = new ViewHolder();
		convertView = LayoutInflater.from(mContext).inflate(R.layout.son_of_dialog_list_view_layout, parent, false);
		vh.mQuestion = (TextView) convertView.findViewById(R.id.question_dialog);
		vh.mAnswer = (TextView) convertView.findViewById(R.id.answer_dialog);
		vh.mCatalog = (TextView) convertView.findViewById(R.id.catalog);
		convertView.setTag(vh);
		return convertView;
	}
	
	public class ViewHolder{
		TextView mQuestion, mAnswer, mCatalog;
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
			String sortStr = mChatDialogEntityList.get(i).getSortLetters();
			char firstChat = sortStr.toUpperCase().charAt(0);
			if(firstChat == sectionIndex) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		//获取当前的数据的首字母的ASC码
		return mChatDialogEntityList.get(position).getSortLetters().charAt(0);
	}
	
	//提取英文首字母，非字母的数字用“#”
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}
}
