package com.neo.mytalker.entity;

import java.util.ArrayList;
import java.util.List;

import com.neo.mytalker.R;
import com.neo.mytalker.adapter.MusicItemAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MusicPlayerDialog extends Dialog{
	public MusicPlayerDialog(Context context) {
		super(context);
		setCanceledOnTouchOutside(true);
	}

	public MusicPlayerDialog(Context context, int themeResId) {
		super(context, themeResId);
	}

	
	public static class Builder{
		private ListView mMusicList;
		private List<MusicItemData> mMusicItemList;
		private MusicItemAdapter mMusicListAdapter;
		private Context mContext;
		public View mView;
		public MusicPlayerDialog dialog;
		
		
		
		public Builder(Context context,ArrayList<MusicItemData> musicdat) {
			dialog = new MusicPlayerDialog(context, R.style.Dialog);
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mView = inflater.inflate(R.layout.fragment_musicplayer, null);
			mMusicList=(ListView) mView.findViewById(R.id.music_list);
			InitMusicData();
			//mMusicItemList=musicdat;
			mMusicListAdapter=new MusicItemAdapter(mMusicItemList,mView.getContext());
			mMusicList.setAdapter(mMusicListAdapter);
			mMusicList.setLongClickable(true);
			mMusicList.setOnItemLongClickListener(new OnItemLongClickListener() {
				
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					MusicItemAdapter.ViewHolder vh=(MusicItemAdapter.ViewHolder) view.getTag();
					vh.mName.setSelected(true);
					return false;
				}
			});
			mMusicList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					
					AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());  //先得到构造器  
			        builder.setTitle("你点击了第"+position+"条"); //设置标题  
			        builder.setMessage("id为"+id); //设置内容  
			        builder.setIcon(R.drawable.iclauncher);//设置图标，图片id即可  
			        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮  
			            @Override  
			            public void onClick(DialogInterface dialog, int which) {  
			                dialog.dismiss(); //关闭dialog  
			                //Toast.makeText(MainActivity.this, "确认" + which, Toast.LENGTH_SHORT).show();  
			            }  
			        });  
			        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮  
			            @Override  
			            public void onClick(DialogInterface dialog, int which) {  
			                dialog.dismiss();  
			                ///Toast.makeText(MainActivity.this, "取消" + which, Toast.LENGTH_SHORT).show();  
			            }  
			        });  
			  
			        builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {//设置忽略按钮  
			            @Override  
			            public void onClick(DialogInterface dialog, int which) {  
			                dialog.dismiss();  
			                //Toast.makeText(MainActivity.this, "忽略" + which, Toast.LENGTH_SHORT).show();  
			            }  
			        });  
			        //参数都设置完成了，创建并显示出来  
			        builder.create().show();  
				}
				
			});
			
			
			TextView prev=(TextView) mView.findViewById(R.id.music_prev);
			prev.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
				}
				
			});
			
			TextView next=(TextView) mView.findViewById(R.id.music_next);
			
			next.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					
					
					
				}
				
			});
			
			TextView play=(TextView) mView.findViewById(R.id.music_state);
			
			play.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					
					
					
				}
				
			});
			
			
			
			
			
			
			float dp2px= context.getResources().getDisplayMetrics().density;
			//lp.setMargins((int)(40*dp2px), (int)(40*dp2px), (int)(40*dp2px), (int)(40*dp2px));
			DisplayMetrics dm = new DisplayMetrics();    
			dm = context.getResources().getDisplayMetrics();    
			float density = dm.density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）    
			int densityDPI = dm.densityDpi; // 屏幕密度（每寸像素：120/160/240/320）        
			int screenWidth = dm.widthPixels; // 屏幕宽（像素，如：3200px）    
			int screenHeight = dm.heightPixels; // 屏幕高（像素，如：1280px）    
			RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(screenWidth-(int)(90*dp2px), screenHeight-(int)(150*dp2px));
            dialog.addContentView(mView, lp);
		}
		
		private void InitMusicData()
		{
			mMusicItemList=new ArrayList<MusicItemData>();
			for(int i=0;i<100;i++)
			{
				MusicItemData tmp=new MusicItemData();
				tmp.isPlaying=(i==1?true:false);
				tmp.name="testMusic"+i+"----------------------------------------------";
				mMusicItemList.add(tmp);
			}

		}
		
		
		public MusicPlayerDialog createListDialog() {
			return dialog;
		} 
	
	}
}
/*
private ListView mMusicList;
private List<MusicItemData> mMusicItemList;
private MusicItemAdapter mMusicListAdapter;

private Context mContext;
private String content;
private OnCloseListener listener;
private String positiveName;
private String negativeName;
private String title;

public MusicPlayerDialog(Context context) {
	super(context);
	this.mContext = context;
}

public MusicPlayerDialog(Context context, int themeResId, String content) {
	super(context, themeResId);
	this.mContext = context;
	this.content = content;
}

public MusicPlayerDialog(Context context, int themeResId, String content, OnCloseListener listener) {
	super(context, themeResId);
	this.mContext = context;
	this.content = content;
	this.listener = listener;
}

protected MusicPlayerDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
	super(context, cancelable, cancelListener);
	this.mContext = context;
}

public MusicPlayerDialog setTitle(String title) {
	this.title = title;
	return this;
}

public MusicPlayerDialog setPositiveButton(String name) {
	this.positiveName = name;
	return this;
}

public MusicPlayerDialog setNegativeButton(String name) {
	this.negativeName = name;
	return this;
}

@Override
protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.fragment_musicplayer);
	setCanceledOnTouchOutside(true);
	initView();
}

private void initView() {
//	contentTxt = (TextView) findViewById(R.id.content);
//	titleTxt = (TextView) findViewById(R.id.title);
//	submitTxt = (TextView) findViewById(R.id.submit);
//	submitTxt.setOnClickListener(this);
//	cancelTxt = (TextView) findViewById(R.id.cancel);
//	cancelTxt.setOnClickListener(this);
//
//	contentTxt.setText(content);
//	if (!TextUtils.isEmpty(positiveName)) {
//		submitTxt.setText(positiveName);
//	}
//
//	if (!TextUtils.isEmpty(negativeName)) {
//		cancelTxt.setText(negativeName);
//	}
//
//	if (!TextUtils.isEmpty(title)) {
//		titleTxt.setText(title);
//	}
	mMusicList=(ListView) findViewById(R.id.music_list);
	InitMusicData();
	mMusicListAdapter=new MusicItemAdapter(mMusicItemList,this.getContext());
	mMusicList.setAdapter(mMusicListAdapter);
}
private void InitMusicData()
{
	mMusicItemList=new ArrayList<MusicItemData>();
	for(int i=0;i<100;i++)
	{
		MusicItemData tmp=new MusicItemData();
		tmp.isPlaying=(i==1?true:false);
		tmp.name="testMusic"+i;
	}

}
@Override
public void onClick(View v) {
//	switch (v.getId()) {
//	case R.id.cancel:
//		if (listener != null) {
//			listener.onClick(this, false);
//		}
//		this.dismiss();
//		break;
//	case R.id.submit:
//		if (listener != null) {
//			listener.onClick(this, true);
//		}
//		break;
//	}
}

public interface OnCloseListener {
	void onClick(Dialog dialog, boolean confirm);
}*/