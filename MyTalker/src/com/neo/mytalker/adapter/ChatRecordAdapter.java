package com.neo.mytalker.adapter;

import java.util.ArrayList;

import com.neo.mytalker.R;
import com.neo.mytalker.entity.ChatRecordData;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChatRecordAdapter extends BaseAdapter {

	ArrayList<ChatRecordData> al;
	Activity context;
	int resId;
	View srcView;
	ViewGroup parent;
	public ChatRecordAdapter(Activity ccontext,ArrayList<ChatRecordData> data,int rresId)
	{
		al=data;
		resId=rresId;
		context=ccontext;
				
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return al.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return al.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		this.parent = parent;
		if(convertView==null)
		{
			convertView=newView(parent);
		}
		
		bindView(position,convertView);
		return convertView;
		
	}
	private View newView(ViewGroup parent)
	{
		View convertView=LayoutInflater.from(context).inflate(resId,parent,false);
		ViewHolder vh=new ViewHolder();
		//TextView head=(TextView) (convertView.findViewById(R.id.msg_part_headicon));
		TextView msg=(TextView) (convertView.findViewById(R.id.msg_part_msg));
		//vh.tv_head=head;
		vh.tv_msg=msg;
		convertView.setTag(vh);
		return convertView;

	}
	private void bindView(int position,View convertView)
	{
		float dp2px= context.getResources().getDisplayMetrics().density;
		ViewHolder vh=(ViewHolder) convertView.getTag();
		if(al.size() > 0) {
			if(al.get(position).isMe)
			{
				
				RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	//			lp.addRule(RelativeLayout.LEFT_OF,R.id.msg_part_headicon);
				lp.setMargins((int)(10*dp2px), (int)(10*dp2px), (int)(10*dp2px), (int)(10*dp2px));
				lp.addRule(RelativeLayout.ALIGN_PARENT_END);
				vh.tv_msg.setLayoutParams(lp);
				vh.tv_msg.setBackgroundResource(R.drawable.msg_bg);
				vh.tv_msg.setTextColor(Color.parseColor("#ffffff"));
	//			RelativeLayout.LayoutParams lp2=new RelativeLayout.LayoutParams((int)(40*dp2px),(int)(40*dp2px));
	//			lp2.addRule(RelativeLayout.ALIGN_PARENT_END);
	//			lp2.setMargins((int)(10*dp2px), (int)(10*dp2px), (int)(10*dp2px), (int)(10*dp2px));
	//			vh.tv_head.setLayoutParams(lp2);
			}else {
				RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
	//			lp.addRule(RelativeLayout.RIGHT_OF,R.id.msg_part_headicon);
				lp.setMargins((int)(10*dp2px), (int)(10*dp2px), (int)(10*dp2px), (int)(10*dp2px));
				lp.addRule(RelativeLayout.ALIGN_PARENT_START);
				vh.tv_msg.setLayoutParams(lp);
				vh.tv_msg.setBackgroundResource(R.drawable.msg_op_bg);
				vh.tv_msg.setTextColor(Color.parseColor("#000000"));
	//			RelativeLayout.LayoutParams lp2=new RelativeLayout.LayoutParams((int)(40*dp2px),(int)(40*dp2px));
	//			lp2.addRule(RelativeLayout.ALIGN_PARENT_START);
	//			lp2.setMargins((int)(10*dp2px), (int)(10*dp2px), (int)(10*dp2px), (int)(10*dp2px));
	//			vh.tv_head.setLayoutParams(lp2);
			}
			vh.tv_msg.setText(al.get(position).msg);
		}
	}
	class ViewHolder
	{
		TextView tv_head;
		TextView tv_msg;
	}

}
