package com.neo.mytalker.adapter;

import java.util.ArrayList;
import java.util.List;

import com.neo.mytalker.R;
import com.neo.mytalker.entity.MusicEntity;
import com.neo.mytalker.entity.MusicItemData;
import com.neo.mytalker.util.MusicManager;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;

public class MusicItemAdapter extends BaseAdapter {

	List<MusicEntity> mMusicItemList;
	Context mContext;

	public MusicItemAdapter(List<MusicEntity> musicItemList, Context c) {
		mMusicItemList = musicItemList;
		mContext = c;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mMusicItemList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mMusicItemList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		if (convertView == null) {
			convertView = createView(parent);
		}
		bindViewWithData(position, convertView);
		return convertView;
	}

	public void addItem(MusicEntity m) {
		mMusicItemList.add(m);
	}

	public void removeItem(int position) {
		mMusicItemList.remove(position);
	}

	private View createView(ViewGroup parent) {
		// TODO Auto-generated method stub
		View convertView;
		ViewHolder vh = new ViewHolder();
		convertView = LayoutInflater.from(mContext).inflate(R.layout.listpart_musicitem, parent, false);
		vh.mIsPlaying = (TextView) convertView.findViewById(R.id.music_isplaying);
		vh.mName = (TextView) convertView.findViewById(R.id.music_name);
		convertView.setTag(vh);
		return convertView;
	}

	private void bindViewWithData(int position, View convertView) {
		ViewHolder vh = (ViewHolder) convertView.getTag();
		MusicEntity me = mMusicItemList.get(position);
		if (me.isPlaying()) {
			vh.mIsPlaying.setVisibility(View.VISIBLE);
		} else {
			vh.mIsPlaying.setVisibility(View.INVISIBLE);
		}
		String ars = me.getArtistsNamesToString();
		if(ars == null || ars.equals("")) {
			ars = MusicManager.HINT_NO_ARTIST_INFORMATION;
		}
		vh.mName.setText(me.getMusicName()+"("+ars+")");
	}

	public class ViewHolder {
		public TextView mIsPlaying, mName;
	}

}
