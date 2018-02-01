package com.neo.mytalker.entity;

import java.util.List;

import com.neo.mytalker.R;
import com.neo.mytalker.activity.ChatActivity;
import com.neo.mytalker.adapter.MusicItemAdapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class MusicPlayerDialog extends Dialog{
	public MusicPlayerDialog(Context context) {
		super(context);
		setCanceledOnTouchOutside(false);
	}

	public MusicPlayerDialog(Context context, int themeResId) {
		super(context, themeResId);
	}

	
	public static class Builder{
		private ListView mMusicList;
//		private List<MusicItemData> mMusicItemList;
		private List<MusicEntity> mMusicItemList;
		private MusicItemAdapter mMusicListAdapter;
		private ChatActivity mContext;
		public View mView;
		public MusicPlayerDialog dialog;
		
		
		
//		public Builder(Context context,List<MusicItemData> musicdat) {
		public Builder(ChatActivity context,List<MusicEntity> musicdat) {
			mContext = context;
			dialog = new MusicPlayerDialog(context, R.style.Dialog);
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mView = inflater.inflate(R.layout.fragment_musicplayer, null);
			((GradientDrawable) mView.getBackground()).setColor(GlobalSettings.THEME_COLOR);
			mMusicList=(ListView) mView.findViewById(R.id.music_list);
//			InitMusicData();
			mMusicItemList=musicdat;
			mMusicListAdapter=new MusicItemAdapter(mMusicItemList,mView.getContext());
			mMusicList.setAdapter(mMusicListAdapter);
			mMusicList.setLongClickable(true);
			mMusicList.setOnItemLongClickListener(new OnItemLongClickListener() {
				
				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
//					mMusicItemList.get(position).play(mContext);
					MusicItemAdapter.ViewHolder vh=(MusicItemAdapter.ViewHolder) view.getTag();
					vh.mName.setSelected(true);
					return true;
				}
			});
			final ChatActivity tempCA = context;
			mMusicList.setOnItemClickListener(new OnItemClickListener() {

				@SuppressWarnings("deprecation")
				@Override
				public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
					// TODO Auto-generated method stub
					
					final MusicEntity me = mMusicItemList.get(position);
					me.getAlbumImage();
					AlertDialog.Builder builder=new AlertDialog.Builder(view.getContext());  //先得到构造器  
			        builder.setTitle(me.getMusicName()); //设置标题  
			        builder.setMessage("专辑名："+me.getAlbumName()
						+"\n艺术家："+me.getArtistsNamesToString()); //设置内容 
			        builder.setIcon(new BitmapDrawable(tempCA.getResources(), me.getAlbumImage()));//设置图标，图片id即可
			        builder.setIcon(R.drawable.iclauncher);//设置图标，图片id即可  
			        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮  
			            @Override  
			            public void onClick(DialogInterface dialog, int which) {  
			            	me.play(mContext);
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
			  
//			        builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {//设置忽略按钮  
//			            @Override  
//			            public void onClick(DialogInterface dialog, int which) {  
//			                dialog.dismiss();  
//			                //Toast.makeText(MainActivity.this, "忽略" + which, Toast.LENGTH_SHORT).show();  
//			            }  
//			        });  
			        //参数都设置完成了，创建并显示出来  
			        MusicPlayerDialog.Builder.this.createListDialog().dismiss();
			        builder.create().show();  
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
		
//		private void InitMusicData()
//		{
//			mMusicItemList=new ArrayList<MusicEntity>();
//			for(int i=0;i<100;i++)
//			{
//				MusicEntity tmp=new MusicEntity();
//				tmp.isPlaying=(i==1?true:false);
//				tmp.name="testMusic"+i+"----------------------------------------------";
//				mMusicItemList.add(tmp);
//			}
//
//		}
		
		
		public MusicPlayerDialog createListDialog() {
			return dialog;
		} 
	
	}
}
